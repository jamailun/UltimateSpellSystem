package fr.jamailun.ultimatespellsystem.extension;

import org.junit.jupiter.api.Test;

/**
 * Multiple tests for <b>valid</b> USS files.
 */
class VanillaParsingTests extends ParseAndCompileTest {

    @Test
    void testLoops() {
        testFolder("loops", true);
    }

    @Test
    void testConsole() {
        testFolder("console", true);
    }

    @Override
    protected String baseDir() {
        return "vanilla";
    }
}
