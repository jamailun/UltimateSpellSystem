package fr.jamailun.ultimatespellsystem.dsl.nodes.statements.blocks;

import fr.jamailun.ultimatespellsystem.dsl.errors.TypeException;
import fr.jamailun.ultimatespellsystem.dsl.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.StatementNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypesContext;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.PreviousIndicator;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.Token;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenStream;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenType;
import fr.jamailun.ultimatespellsystem.dsl.visitor.StatementVisitor;
import org.jetbrains.annotations.NotNull;

public class ForeachLoopStatement extends StatementNode {

    private final Token varName;
    private final ExpressionNode source;
    private final StatementNode child;

    public ForeachLoopStatement(Token varName, ExpressionNode source, StatementNode child) {
        this.varName = varName;
        this.source = source;
        this.child = child;
    }

    @Override
    public void validateTypes(TypesContext context) {
        TypesContext childContext = context.childContext();

        // Source
        source.validateTypes(context);
        if(!source.getExpressionType().isCollection()) {
            throw new TypeException(source, "A FOREACH statement must use a collection for the source.");
        }

        // declare variable
        childContext.registerVariable(varName.getContentString(), varName.pos(), source.getExpressionType().asMonoElement());

        // Validate child
        child.validateTypes(childContext);
    }

    @Override
    public void visit(@NotNull StatementVisitor visitor) {
        visitor.handleForeachLoop(this);
    }

    @PreviousIndicator(expected = TokenType.FOREACH) // FOREACH(<VARIABLE> : <SOURCE>) <CHILD>
    public static ForeachLoopStatement parseForLoop(TokenStream tokens) {
        tokens.dropOrThrow(TokenType.BRACKET_OPEN);

        Token varName = tokens.nextOrThrow(TokenType.VALUE_VARIABLE);
        tokens.dropOrThrow(TokenType.COLON);
        ExpressionNode source = ExpressionNode.readNextExpression(tokens);

        tokens.dropOrThrow(TokenType.BRACKET_CLOSE);
        StatementNode child = StatementNode.parseNextStatement(tokens);

        return new ForeachLoopStatement(varName, source, child);
    }

    public ExpressionNode getSource() {
        return source;
    }

    public String getVariableName() {
        return varName.getContentString();
    }

    public StatementNode getChild() {
        return child;
    }

    @Override
    public String toString() {
        return "FOREACH( %" + varName + " IN " + source + "): " + child;
    }
}
