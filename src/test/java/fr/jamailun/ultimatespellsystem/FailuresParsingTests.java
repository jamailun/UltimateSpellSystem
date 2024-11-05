package fr.jamailun.ultimatespellsystem;

import fr.jamailun.ultimatespellsystem.dsl.errors.SyntaxException;
import fr.jamailun.ultimatespellsystem.dsl.errors.TypeException;
import fr.jamailun.ultimatespellsystem.dsl.errors.UssException;
import org.junit.jupiter.api.Test;

import java.io.File;

public class FailuresParsingTests extends ParsingTest {

    @Test
    void badSyntaxParsing() {
        String title = "[! EXPECTED SyntaxException] ";
        for(File file : listTests("bad_syntax")) {
            try {
                parseAndVerify(file);
                addFails(file, title + "Got not error.");
            } catch(SyntaxException ignored) {
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
            } catch(TypeException ignored) {
                addOk();
            } catch(UssException no) {
                no.printStackTrace();
                addFails(file, title + "Got " + toString(no));
            }
        }
        printResults();
    }

}
