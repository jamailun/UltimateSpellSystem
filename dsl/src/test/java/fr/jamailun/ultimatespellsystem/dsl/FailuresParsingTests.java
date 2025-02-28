package fr.jamailun.ultimatespellsystem.dsl;

import fr.jamailun.ultimatespellsystem.dsl.errors.ParsingException;
import fr.jamailun.ultimatespellsystem.dsl.errors.SyntaxException;
import fr.jamailun.ultimatespellsystem.dsl.errors.TreeValidationException;
import fr.jamailun.ultimatespellsystem.dsl.errors.TypeException;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.io.File;

/**
 * Multiple tests for <b>invalid</b> USS files.
 */
public class FailuresParsingTests extends ParsingTest {

    @Test
    void badParsing() {
        badParsing("bad_parsing", ParsingException.class);
    }

    @Test
    void badSyntax() {
        badParsing("bad_syntax", SyntaxException.class);
    }

    @Test
    void badType() {
        badParsing("bad_type", TypeException.class);
    }

    @Test
    void badTreeValidation() {
        badParsing("bad_tree", TreeValidationException.class);
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
