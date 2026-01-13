package fr.jamailun.ultimatespellsystem.extension;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Multiple tests for <b>valid</b> USS files.
 */
class ExtensionParsingTests extends ParseAndCompileTest {

    @BeforeAll
    static void beforeAll() {
        ExtensionLoader.loadStatic();
    }

    @Test
    void testVariousFunctions() {
        testFolder("various", true);
    }

    @Test
    void testLists() {
        testFolder("lists", true);
    }

    @Test
    void testEntityTypes() {
        testFolder("entities", false);
    }


    @Override
    protected String baseDir() {
        return "extension";
    }
}
