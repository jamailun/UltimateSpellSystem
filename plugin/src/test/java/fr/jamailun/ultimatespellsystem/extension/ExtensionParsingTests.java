package fr.jamailun.ultimatespellsystem.extension;

import fr.jamailun.ultimatespellsystem.dsl.errors.UssException;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;

/**
 * Multiple tests for <b>valid</b> USS files.
 */
public class ExtensionParsingTests extends ParsingTest {

    @BeforeAll
    static void before() {
        ExtensionLoader.load();
    }

    @Test
    void testVariousFunctions() {
        testFolder("various");
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
