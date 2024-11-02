package fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.litteral;

import fr.jamailun.ultimatespellsystem.dsl.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Type;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypePrimitive;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypesContext;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.PreviousIndicator;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenPosition;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenStream;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenType;
import fr.jamailun.ultimatespellsystem.dsl.visitor.ExpressionVisitor;
import lombok.Getter;

@Getter
public class LocationLiteral extends ExpressionNode {

    private final ExpressionNode world;
    private final ExpressionNode vectorX;
    private final ExpressionNode vectorY;
    private final ExpressionNode vectorZ;
    private final ExpressionNode yaw;
    private final ExpressionNode pitch;

    protected LocationLiteral(TokenPosition position, ExpressionNode world, ExpressionNode vectorX, ExpressionNode vectorY, ExpressionNode vectorZ, ExpressionNode yaw, ExpressionNode pitch) {
        super(position);
        this.world = world;
        this.vectorX = vectorX;
        this.vectorY = vectorY;
        this.vectorZ = vectorZ;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public boolean asYawAndPitch() {
        return yaw != null && pitch != null;
    }

    @Override
    public Type getExpressionType() {
        return TypePrimitive.LOCATION.asType();
    }

    @Override
    public void visit(ExpressionVisitor visitor) {
        visitor.handleLocationLiteral(this);
    }

    @Override
    public void validateTypes(TypesContext context) {
        assertExpressionType(world, context, TypePrimitive.STRING);
        assertExpressionType(vectorX, context, TypePrimitive.NUMBER);
        assertExpressionType(vectorY, context, TypePrimitive.NUMBER);
        assertExpressionType(vectorZ, context, TypePrimitive.NUMBER);

        if(asYawAndPitch()) {
            assertExpressionType(yaw, context, TypePrimitive.NUMBER);
            assertExpressionType(pitch, context, TypePrimitive.NUMBER);
        }
    }

    @PreviousIndicator(expected = TokenType.CHAR_AT)
    public static LocationLiteral readNextLocation(TokenStream tokens) {
        TokenPosition position = tokens.position();
        // Open
        tokens.dropOrThrow(TokenType.BRACKET_OPEN);

        // World + vector
        ExpressionNode world = ExpressionNode.readNextExpression(tokens);
        tokens.dropOrThrow(TokenType.COMMA);
        ExpressionNode x = ExpressionNode.readNextExpression(tokens);
        tokens.dropOrThrow(TokenType.COMMA);
        ExpressionNode y = ExpressionNode.readNextExpression(tokens);
        tokens.dropOrThrow(TokenType.COMMA);
        ExpressionNode z = ExpressionNode.readNextExpression(tokens);

        // Optional yaw + pitch
        ExpressionNode yaw = null;
        ExpressionNode pitch = null;
        if(tokens.dropOptional(TokenType.COMMA)) {
            yaw = ExpressionNode.readNextExpression(tokens);
        }
        if(tokens.dropOptional(TokenType.COMMA)) {
            pitch = ExpressionNode.readNextExpression(tokens);
        }

        // Close
        tokens.dropOrThrow(TokenType.BRACKET_CLOSE);

        return new LocationLiteral(position, world, x, y, z, yaw, pitch);
    }
}
