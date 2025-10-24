package fr.jamailun.ultimatespellsystem.dsl2;

import fr.jamailun.ultimatespellsystem.dsl2.errors.UssException;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.io.File;

/**
 * Multiple tests for <b>valid</b> USS files.
 */
public class CorrectParsingTests extends ParsingTest {

    @Test
    void correctParsing() {
        testFolder("corrects/parsing");
    }

    @Test
    void correctBasics() {
        testFolder("corrects/basics");
    }

    @Test
    void correctLoops() {
        testFolder("corrects/loops");
    }

    @Test
    void correctLiterals() {
        testFolder("corrects/literals");
    }

    @Test
    void correctFunctions() {
        testFolder("corrects/functions");
    }

    @Test
    void correctArrays() {
        testFolder("corrects/arrays");
    }

    // --

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
