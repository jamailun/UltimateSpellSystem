package fr.jamailun.ultimatespellsystem.dsl2.nodes.statements;

import fr.jamailun.ultimatespellsystem.dsl2.errors.SyntaxException;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.StatementNode;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.expressions.functions.FunctionArgument;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.expressions.functions.FunctionDefinition;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.type.Type;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.type.variables.TypesContext;
import fr.jamailun.ultimatespellsystem.dsl2.tokenization.Token;
import fr.jamailun.ultimatespellsystem.dsl2.tokenization.TokenPosition;
import fr.jamailun.ultimatespellsystem.dsl2.tokenization.TokenStream;
import fr.jamailun.ultimatespellsystem.dsl2.tokenization.TokenType;
import fr.jamailun.ultimatespellsystem.dsl2.visitor.StatementVisitor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class FunctionDeclarationStatement extends StatementNode {

  private final @NotNull TokenPosition position;
  private final String functionReturnType;
  private final String functionName;
  private final List<FunctionParameter> parameters;
  private final List<StatementNode> statements;

  public @NotNull FunctionDefinition asDefinition() {
    return new FunctionDefinition(
            functionName,
            Type.ofAny(functionReturnType),
            parameters.stream().map(FunctionParameter::asArgument).toList()
    );
  }

  @Override
  public void visit(@NotNull StatementVisitor visitor) {
    visitor.handleFunctionDeclaration(this);
  }

  public @NotNull Type getOutputType() {
    return Type.ofAny(functionReturnType);
  }

  @Override
  public void validateTypes(@NotNull TypesContext context) {
    // 1. Check function does not already exist
    FunctionDefinition existingFunction = context.findFunction(functionName);
    if (existingFunction != null) {
      throw new SyntaxException(position, "The function '" + functionName + "' has already been defined. Signature is " + existingFunction);
    }

    // 2. Register function and variables !
    context.registerFunction(this);
    for(FunctionParameter parameter : parameters) {
      context.registerVariable(position, parameter.name, parameter.getType());
    }

    // 3. Propagate to children
    TypesContext child = context.childContext();
    for(StatementNode statement : statements) {
      statement.validateTypes(child);
    }
  }

  public static @NotNull FunctionDeclarationStatement parseNextFunction(@NotNull Token typeIdentifier, @NotNull Token nameIdentifier, @NotNull TokenStream tokens) {
    String functionReturnType = typeIdentifier.getContentString();
    String functionName = nameIdentifier.getContentString();

    // on a déjà retiré le '('.

    List<FunctionParameter> parameters = new ArrayList<>();
    boolean first = true;
    while(! tokens.dropOptional(TokenType.BRACKET_CLOSE)) {
      if(first) first = false; else tokens.dropOrThrow(TokenType.COMMA, "Arguments need to be separated by a comma.");
      // Each parameter
      Token identifierType = tokens.nextOrThrow(TokenType.IDENTIFIER, "Missing parameter type in function '" + functionName + "'.");
      Token identifierName = tokens.nextOrThrow(TokenType.IDENTIFIER, "Missing parameter name in function '" + functionName + "'.");
      parameters.add(new FunctionParameter(identifierType.getContentString(), identifierName.getContentString()));
    }

    // On ne veut pas un 'block-statement' mais nécessairement des statements
    tokens.dropOrThrow(TokenType.BRACES_OPEN, "A function need to have statements !");
    List<StatementNode> statements = new ArrayList<>();
    while(! tokens.dropOptional(TokenType.BRACES_CLOSE)) {
      statements.add(StatementNode.parseNextStatement(tokens));
      tokens.dropOptional(TokenType.SEMI_COLON);
    }

    return new FunctionDeclarationStatement(typeIdentifier.pos(), functionReturnType, functionName, parameters, statements);
  }

  public record FunctionParameter(@NotNull String type, @NotNull String name) {
    public @NotNull Type getType() {
      return Type.ofAny(type);
    }
    @Contract(" -> new")
    public @NotNull FunctionArgument asArgument() {
      return new FunctionArgument(Type.ofAny(type), name, false);
    }
  }

  @Override
  public String toString() {
    return "FUNCTION " + functionReturnType + " " + functionName + "(" +
        parameters.toString() +
        ") {" +
        statements.toString() +
        "}";
  }
}
