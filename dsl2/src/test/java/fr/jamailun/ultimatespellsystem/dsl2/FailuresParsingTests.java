package fr.jamailun.ultimatespellsystem.dsl2;

import fr.jamailun.ultimatespellsystem.dsl2.errors.*;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.io.File;

/**
 * Multiple tests for <b>invalid</b> USS files.
 */
public class FailuresParsingTests extends ParsingTest {

    @Test
    void badType() {
        badParsing("invalid/bad_type", TypeException.class);
    }

    @Test
    void badSyntax() {
        badParsing("invalid/bad_syntax", SyntaxException.class);
    }

    @Test
    void badParsing() {
        badParsing("invalid/bad_parsing", ParsingException.class);
    }

    @Test
    void badTree() {
        badParsing("invalid/bad_tree", TreeValidationException.class);
    }

    private <T extends Exception> void badParsing(@NotNull String folder, @NotNull Class<T> clazz) {
        String title = "[! EXPECTED "+clazz.getSimpleName()+"] ";
        for(File file : listTests(folder)) {
            try {
                parseAndVerify(file);
                addFails(file, title + "Got not error.");
            } catch(Exception exception) {
                if(clazz.isAssignableFrom(exception.getClass())) {
                    System.out.println("Error: " + exception);
                    addOk();
                } else {
                    System.err.println("GOT " + exception.getClass() + ", expected " + clazz);
                    exception.printStackTrace();
                    addFails(file, title + "Got " + toString(exception));
                }
            }
        }
        printResults();
    }

}
