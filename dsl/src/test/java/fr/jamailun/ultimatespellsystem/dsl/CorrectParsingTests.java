package fr.jamailun.ultimatespellsystem.dsl;

import fr.jamailun.ultimatespellsystem.dsl.errors.UssException;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.functions.FunctionArgument;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.functions.FunctionDefinition;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.functions.FunctionType;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypePrimitive;
import fr.jamailun.ultimatespellsystem.dsl.registries.FunctionDefinitionsRegistry;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

/**
 * Multiple tests for <b>valid</b> USS files.
 */
public class CorrectParsingTests extends ParsingTest {

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
                        new FunctionArgument(FunctionType.accept(TypePrimitive.NUMBER), "a", false),
                        new FunctionArgument(FunctionType.accept(TypePrimitive.NUMBER), "b", false)
                )
        );
        FunctionDefinitionsRegistry.register(definition);

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
