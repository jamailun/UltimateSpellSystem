package fr.jamailun.ultimatespellsystem.extension;

import fr.jamailun.ultimatespellsystem.api.runner.RuntimeStatement;
import fr.jamailun.ultimatespellsystem.dsl.errors.UssException;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

/**
 * Multiple tests for <b>valid</b> USS files.
 */
public class ExtensionParsingTests extends ParseAndCompileTest {

    @BeforeAll
    static void before() {
        ExtensionLoader.load();
    }

    @Test
    void testVariousFunctions() {
        testFolder("various", true);
    }

    @Test
    void testEntityTypes() {
        testFolder("entities", false);
    }

    private void testFolder(@NotNull String folder, boolean run) {
        for(File file : listTests(folder)) {
            try {
                List<RuntimeStatement> statements = parseAndVerify(file);
                if(run)
                    cast(statements);
                addOk();
            } catch (UssException e) {
                e.printStackTrace();
                addFails(file, toString(e));
            }
        }
        printResults();
    }

}
