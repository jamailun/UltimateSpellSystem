package fr.jamailun.ultimatespellsystem.dsl;

import fr.jamailun.ultimatespellsystem.dsl.errors.SyntaxException;
import fr.jamailun.ultimatespellsystem.dsl.errors.TypeException;
import fr.jamailun.ultimatespellsystem.dsl.errors.UssException;
import org.junit.jupiter.api.Test;

import java.io.File;

/**
 * Multiple tests for <b>invalid</b> USS files.
 */
public class FailuresParsingTests extends ParsingTest {

    @Test
    void badSyntaxParsing() {
        String title = "[! EXPECTED SyntaxException] ";
        for(File file : listTests("bad_syntax")) {
            try {
                parseAndVerify(file);
                addFails(file, title + "Got not error.");
            } catch(SyntaxException err) {
                System.out.println("Error: " + err);
                addOk();
            } catch(UssException no) {
                no.printStackTrace();
                addFails(file, title + "Got " + toString(no));
            }
        }
        printResults();
    }
    @Test
    void badTypeParsing() {
        String title = "[! EXPECTED TypeException] ";
        for(File file : listTests("bad_type")) {
            try {
                parseAndVerify(file);
                addFails(file, title + "Got not error.");
            } catch(TypeException err) {
                System.out.println("Error: " + err);
                addOk();
            } catch(UssException no) {
                no.printStackTrace();
                addFails(file, title + "Got " + toString(no));
            }
        }
        printResults();
    }

}
