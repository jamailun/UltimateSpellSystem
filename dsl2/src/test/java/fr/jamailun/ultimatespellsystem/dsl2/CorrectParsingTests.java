package fr.jamailun.ultimatespellsystem.dsl2;

import fr.jamailun.ultimatespellsystem.dsl2.errors.UssException;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.expressions.functions.FunctionDefinition;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.type.TypePrimitive;
import fr.jamailun.ultimatespellsystem.dsl2.registries.FunctionDefinitionsRegistry;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

/**
 * Multiple tests for <b>valid</b> USS files.
 */
public class CorrectParsingTests extends ParsingTest {

    @Test
    void correctBasics() {
        testFolder("corrects/basics");
    }
    @Test
    void correctStatements() {
        testFolder("corrects/statements");
    }
    @Test
    void correctBlocks() {
        testFolder("corrects/blocks");
    }
    @Test
    void correctOperators() {
        testFolder("corrects/operators");
    }
    @Test
    void correctMix() {
        testFolder("corrects/mix");
    }
    @Test
    void correctMetadata() {
        testFolder("corrects/metadata");
    }

    @Test
    void correctWithCustom() {
        // Register "custom_add"
        FunctionDefinition definition = new FunctionDefinition(
                "custom_add",
                TypePrimitive.NUMBER.asType(),
                List.of(
//                        new FunctionArgument(FunctionType.accept(TypePrimitive.NUMBER), "a", false),
//                        new FunctionArgument(FunctionType.accept(TypePrimitive.NUMBER), "b", false)
                )
        );
        FunctionDefinitionsRegistry.register(definition);

        // Callback
//        CallbackEventRegistry.register(
//                CallbackEvent.of("landed", TokenType.AT, TypePrimitive.LOCATION)
//        );

        // Parse
        testFolder("corrects_with_custom");
    }

    private void testFolder(@NotNull String folder) {
        for(File file : listTests(folder)) {
            try {
                parseAndVerify(file);
                addOk();
            } catch (UssException e) {
                e.printStackTrace();
                addFails(file, toString(e));
            }
        }
        printResults();
    }

}
