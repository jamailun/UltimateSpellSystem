package fr.jamailun.ultimatespellsystem.dsl2.nodes.expressions.litteral;

import fr.jamailun.ultimatespellsystem.dsl2.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.type.Type;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.type.TypePrimitive;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.type.variables.TypesContext;
import fr.jamailun.ultimatespellsystem.dsl2.tokenization.PreviousIndicator;
import fr.jamailun.ultimatespellsystem.dsl2.tokenization.TokenPosition;
import fr.jamailun.ultimatespellsystem.dsl2.tokenization.TokenStream;
import fr.jamailun.ultimatespellsystem.dsl2.tokenization.TokenType;
import fr.jamailun.ultimatespellsystem.dsl2.visitor.ExpressionVisitor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

/**
 * A raw {@link org.bukkit.Location} literal.
 */
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

    public boolean hasYawAndPitch() {
        return yaw != null && pitch != null;
    }

    @Override
    public @NotNull Type getExpressionType() {
        return Type.of("location");
    }

    @Override
    public void visit(@NotNull ExpressionVisitor visitor) {
        visitor.handleLocationLiteral(this);
    }

    @Override
    public void validateTypes(@NotNull TypesContext context) {
        assertExpressionType(world, context, TypePrimitive.STRING);
        assertExpressionType(vectorX, context, TypePrimitive.NUMBER);
        assertExpressionType(vectorY, context, TypePrimitive.NUMBER);
        assertExpressionType(vectorZ, context, TypePrimitive.NUMBER);

        if(hasYawAndPitch()) {
            assertExpressionType(yaw, context, TypePrimitive.NUMBER);
            assertExpressionType(pitch, context, TypePrimitive.NUMBER);
        }
    }

    /**
     * Parse a new raw location.
     * @param tokens stream of tokens.
     * @return a new instance.
     */
    @PreviousIndicator(expected = TokenType.CHAR_AT)
    public static @NotNull LocationLiteral readNextLocation(@NotNull TokenStream tokens) {
        TokenPosition position = tokens.position();
        // Open
        tokens.dropOrThrow(TokenType.BRACKET_OPEN);

        // World + vector
        ExpressionNode world = ExpressionNode.readNextExpression(tokens);
        tokens.dropOrThrow(TokenType.COMMA, "A location literal needs a X coordinate after the world.");
        ExpressionNode x = ExpressionNode.readNextExpression(tokens);
        tokens.dropOrThrow(TokenType.COMMA, "A location literal needs a Y coordinate after the world and X.");
        ExpressionNode y = ExpressionNode.readNextExpression(tokens);
        tokens.dropOrThrow(TokenType.COMMA, "A location literal needs a Z coordinate avec world and X + Y coordinates.");
        ExpressionNode z = ExpressionNode.readNextExpression(tokens);

        // Optional yaw + pitch
        ExpressionNode yaw = null;
        ExpressionNode pitch = null;
        if(tokens.dropOptional(TokenType.COMMA)) {
            yaw = ExpressionNode.readNextExpression(tokens);
            tokens.dropOrThrow(TokenType.COMMA, "In a location literal, after the yaw, a pitch value is required.");
            pitch = ExpressionNode.readNextExpression(tokens);
        }

        // Close
        tokens.dropOrThrow(TokenType.BRACKET_CLOSE, "A location literal needs a ')' at the end.");

        return new LocationLiteral(position, world, x, y, z, yaw, pitch);
    }

    @Override
    public String toString() {
        return "LOC<"+world+", ("+vectorX+","+vectorY+","+vectorZ+")>";
    }
}
