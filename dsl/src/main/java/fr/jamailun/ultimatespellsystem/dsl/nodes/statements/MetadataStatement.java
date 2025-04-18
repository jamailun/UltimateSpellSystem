package fr.jamailun.ultimatespellsystem.dsl.nodes.statements;

import fr.jamailun.ultimatespellsystem.dsl.metadata.MetadataRulesManager;
import fr.jamailun.ultimatespellsystem.dsl.nodes.StatementNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Duration;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.variables.TypesContext;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.*;
import fr.jamailun.ultimatespellsystem.dsl.visitor.StatementVisitor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringJoiner;

/**
 * A metadata statement of a spell.
 * Should not be "executed" as such.
 */
@RequiredArgsConstructor
@Getter
public class MetadataStatement extends StatementNode {

    private final TokenPosition position;
    private final @NotNull String name;
    private final @NotNull @Unmodifiable List<Object> params;

    /**
     * Parse a metadata statement.
     * @param tokens streams of tokens.
     * @return a new instance.
     */
    @PreviousIndicator(expected = TokenType.CHAR_AT)
    public static @NotNull MetadataStatement parseMetadata(@NotNull TokenStream tokens) {
        TokenPosition position = tokens.position();
        List<Object> parameters = new ArrayList<>();

        String name = tokens.nextOrThrow(TokenType.IDENTIFIER).getContentString();
        if(tokens.dropOptional(TokenType.BRACKET_OPEN)) {
            Object raw;
            while ((raw = readNextRaw(tokens)) != null) {
                parameters.add(raw);
                tokens.dropOptional(TokenType.COMMA);
            }
            tokens.dropOrThrow(TokenType.BRACKET_CLOSE);
        }

        return new MetadataStatement(position, name, Collections.unmodifiableList(parameters));
    }

    private static @Nullable Object readNextRaw(@NotNull TokenStream tokens) {
        Token token = tokens.peek();
        if(token.getType().isRawValue()) {
            tokens.drop();
            return switch(token.getType()) {
                case TRUE -> true;
                case FALSE -> false;
                case IDENTIFIER, VALUE_STRING -> token.getContentString();
                case VALUE_DURATION -> new Duration(token.getContentNumber(), token.getContentTimeUnit());
                case VALUE_NUMBER -> token.getContentNumber();
                default -> throw new RuntimeException("Not reachable (" + token + ")");
            };
        }
        return null;
    }

    @Override
    public void visit(@NotNull StatementVisitor visitor) {
        visitor.handleMetadata(this);
    }

    @Override
    public void validateTypes(@NotNull TypesContext context) {
        MetadataRulesManager.getRulesForName(getName())
                .forEach(rule -> rule.apply(context, this));
    }

    @Override
    public String toString() {
        String prefix = "@" + getName() + "(";
        StringJoiner sj = new StringJoiner(", ");
        params.forEach(p -> sj.add(p.toString()));
        return prefix + sj + ")";
    }
}
