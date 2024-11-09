package fr.jamailun.ultimatespellsystem;

import fr.jamailun.ultimatespellsystem.dsl.errors.UssException;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.functions.FunctionArgument;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.functions.FunctionDefinition;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.functions.FunctionType;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypePrimitive;
import fr.jamailun.ultimatespellsystem.dsl.registries.FunctionDefinitionsRegistry;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

/**
 * Multiple tests for <b>valid</b> USS files.
 */
public class CorrectParsingTests extends ParsingTest {

    @Test
    void correctParsing() {
        for(File file : listTests("corrects")) {
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

    @Test
    void correctWithCustom() {
        // Register "custom_add"
        FunctionDefinition definition = new FunctionDefinition(
                "custom_add",
                FunctionType.accept(TypePrimitive.NUMBER),
                List.of(
                        new FunctionArgument(FunctionType.accept(TypePrimitive.NUMBER), "a", false),
                        new FunctionArgument(FunctionType.accept(TypePrimitive.NUMBER), "b", false)
                )
        );
        FunctionDefinitionsRegistry.register(definition);

        // Parse
        for(File file : listTests("corrects_with_custom")) {
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
