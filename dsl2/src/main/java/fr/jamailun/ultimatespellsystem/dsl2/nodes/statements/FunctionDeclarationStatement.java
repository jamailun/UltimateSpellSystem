package fr.jamailun.ultimatespellsystem.dsl2.nodes.statements;

import fr.jamailun.ultimatespellsystem.dsl2.nodes.StatementNode;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.type.variables.TypesContext;
import fr.jamailun.ultimatespellsystem.dsl2.tokenization.Token;
import fr.jamailun.ultimatespellsystem.dsl2.tokenization.TokenStream;
import fr.jamailun.ultimatespellsystem.dsl2.tokenization.TokenType;
import fr.jamailun.ultimatespellsystem.dsl2.visitor.StatementVisitor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class FunctionDeclarationStatement extends StatementNode {

  private final String functionReturnType;
  private final String functionName;
  private final List<FunctionParameter> parameters;
  private final List<StatementNode> statements;

  @Override
  public void visit(@NotNull StatementVisitor visitor) {
    //TODO
    throw new UnsupportedOperationException("Not implemented yet");
  }

  @Override
  public void validateTypes(@NotNull TypesContext context) {
    //TODO
    throw new UnsupportedOperationException("Not implemented yet");
  }

  public static @NotNull FunctionDeclarationStatement parseNextFunction(@NotNull Token typeIdentifier, @NotNull Token nameIdentifier, @NotNull TokenStream tokens) {
    String functionReturnType = typeIdentifier.getContentString();
    String functionName = nameIdentifier.getContentString();

    // on a déjà retiré le '('.

    List<FunctionParameter> parameters = new ArrayList<>();
    boolean first = true;
    while(! tokens.dropOptional(TokenType.BRACES_CLOSE)) {
      if(first) first = false; else tokens.dropOrThrow(TokenType.COMMA);
      // Each parameter
      Token identifierType = tokens.nextOrThrow(TokenType.IDENTIFIER);
      Token identifierName = tokens.nextOrThrow(TokenType.IDENTIFIER);
      parameters.add(new FunctionParameter(identifierType.getContentString(), identifierName.getContentString()));
    }

    // On ne veut pas un 'block-statement' mais nécessairement des statements
    tokens.dropOptional(TokenType.BRACKET_OPEN);
    List<StatementNode> statements = new ArrayList<>();
    while(! tokens.dropOptional(TokenType.BRACKET_CLOSE)) {
      statements.add(StatementNode.parseNextStatement(tokens));
      tokens.dropOptional(TokenType.SEMI_COLON);
    }

    return new FunctionDeclarationStatement(functionReturnType, functionName, parameters, statements);
  }

  public record FunctionParameter(String type, String name) {}
}
