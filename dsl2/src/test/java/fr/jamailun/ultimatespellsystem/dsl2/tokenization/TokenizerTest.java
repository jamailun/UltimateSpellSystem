package fr.jamailun.ultimatespellsystem.dsl2.tokenization;

import fr.jamailun.ultimatespellsystem.dsl2.nodes.type.Duration;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

class TokenizerTest {

  @Test
  void basic() {
    String input = "var toto = 123";
    assertTokenize(
        input,
        List.of(
            symbol(TokenType.VAR),
            identifier("toto"),
            symbol(TokenType.EQUAL),
            number(123)
        )
    );
  }

  @Test
  void comparison() {
    String input = """
        if(test() == true || false) {
          dur = 3s * dt;
          e = "\\\\";
        }
        """;
    assertTokenize(
        input,
        List.of(
            symbol(TokenType.IF),
            symbol(TokenType.BRACKET_OPEN),
            identifier("test"),
            symbol(TokenType.BRACKET_OPEN),
            symbol(TokenType.BRACKET_CLOSE),
            symbol(TokenType.COMP_EQ),
            bool(true),
            symbol(TokenType.OPE_OR),
            bool(false),
            symbol(TokenType.BRACKET_CLOSE),
            symbol(TokenType.BRACES_OPEN),
            identifier("dur"),
            symbol(TokenType.EQUAL),
            duration(new Duration(3, TimeUnit.SECONDS)),
            symbol(TokenType.OPE_MUL),
            identifier("dt"),
            symbol(TokenType.SEMI_COLON),
            identifier("e"),
            symbol(TokenType.EQUAL),
            string("\\"),
            symbol(TokenType.SEMI_COLON),
            symbol(TokenType.BRACES_CLOSE)
        )
    );
  }

  @Test
  void basicOperator() {
    String input = "z = (-1 + 3) * x";
    assertTokenize(
        input,
        List.of(
            identifier("z"),
            symbol(TokenType.EQUAL),
            symbol(TokenType.BRACKET_OPEN),
            symbol(TokenType.OPE_SUB),
            number(1),
            symbol(TokenType.OPE_ADD),
            number(3),
            symbol(TokenType.BRACKET_CLOSE),
            symbol(TokenType.OPE_MUL),
            identifier("x")
        )
    );
  }

  @Test
  void basicString() {
    String input = "str = \"val\\\"ue\";";
    assertTokenize(
        input,
        List.of(
            identifier("str"),
            symbol(TokenType.EQUAL),
            string("val\"ue"),
            symbol(TokenType.SEMI_COLON)
        )
    );
  }

  // ----------------------------------

  private static @NotNull List<Token> tokenize(@NotNull String input) {
    var output = Tokenizer.tokenize(CharStream.from(input));
    List<Token> tokens = new ArrayList<>();
    while(output.hasMore()) {
      tokens.add(output.next());
    }
    return tokens;
  }

  private static void assertTokenize(String input, @NotNull List<Token> tokens) {
    List<Token> outputTokens = tokenize(input);
    List<Token> expectedTokens = new ArrayList<>(tokens);
    expectedTokens.add(symbol(TokenType.EOF));
    Assertions.assertEquals(expectedTokens.size(), outputTokens.size(), "Tokens are not the same length. E=" +expectedTokens+", O="+outputTokens);
    for(int i = 0; i < expectedTokens.size(); i++) {
      Assertions.assertEquals(expectedTokens.get(i), outputTokens.get(i), "Token of index " + i + " was NOT equal.");
    }
  }

  private static Token bool(boolean v) {
    return Token.fromBoolean(v, TokenPosition.unknown());
  }
  private static Token string(String v) {
    return Token.fromString(v, TokenPosition.unknown());
  }
  private static Token number(double v) {
    return Token.fromNumber(v, TokenPosition.unknown());
  }
  private static Token duration(Duration v) {
    return Token.fromDuration(v.amount(), v.timeUnit(), TokenPosition.unknown());
  }
  private static Token identifier(String v) {
    return Token.fromIdentifier(v, TokenPosition.unknown());
  }
  private static Token symbol(TokenType v) {
    return new Token(v, TokenPosition.unknown());
  }

}
