package fr.jamailun.ultimatespellsystem;

import fr.jamailun.ultimatespellsystem.dsl.errors.UssException;
import org.junit.jupiter.api.Test;

import java.io.File;

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

}
