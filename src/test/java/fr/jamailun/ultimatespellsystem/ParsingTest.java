package fr.jamailun.ultimatespellsystem;

import fr.jamailun.ultimatespellsystem.dsl.UltimateSpellSystemDSL;
import fr.jamailun.ultimatespellsystem.dsl.errors.UssException;
import fr.jamailun.ultimatespellsystem.dsl.nodes.StatementNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypesContext;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.CharStream;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenStream;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.Tokenizer;
import fr.jamailun.ultimatespellsystem.dsl.validators.DslValidator;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;

import java.io.File;
import java.util.*;

/**
 * Test framework for parsing tests.
 */
abstract class ParsingTest {

    protected final File PARSINGS_FILE = new File("src/test/resources/parsing");

    protected @NotNull List<File> listTests(@NotNull String subFolder) {
        File directory = new File(PARSINGS_FILE, subFolder);
        Assertions.assertTrue(directory.exists() && directory.isDirectory(), "Directory '" + subFolder + "' does not exist.");

        File[] children = directory.listFiles();
        if(children == null) {
            return Collections.emptyList();
        }
        return List.of(children);
    }

    protected @NotNull String toString(@NotNull Exception e) {
        return e.getClass().getSimpleName() + " : " + e.getMessage();
    }

    protected int countOk = 0;
    protected final Map<File, String> failures = new HashMap<>();

    protected void addOk() {
        countOk++;
    }
    protected void addFails(@NotNull File test, @NotNull String error) {
        failures.put(test, error);
    }

    protected void parseAndVerify(@NotNull File file) throws UssException {
        System.out.println("\n\n ================[ " + file.getName() + "]================\n");
        // Tokenize
        TokenStream tokens = Tokenizer.tokenize(CharStream.from(file));
        System.out.println(tokens + "\n");

        // Parse
        List<StatementNode> nodes = UltimateSpellSystemDSL.parse(tokens);

        System.out.println(" ----------------------- ");

        // validate
        TypesContext context = new TypesContext();
        for (StatementNode node : nodes) {
            node.validateTypes(context);
            System.out.println("> " + node);
        }
    }

    public static final String RESET = "\033[0m";  // Text Reset
    public static final String RED_BOLD = "\033[1;31m";    // RED
    public static final String RED = "\033[0;31m";     // RED
    public static final String GREEN_BOLD = "\033[1;32m";  // GREEN
    public static final String WHITE_BOLD = "\033[1;37m";  // WHITE

    protected void printResults() {
        String color = failures.isEmpty() ? GREEN_BOLD : RED_BOLD;
        System.out.println("\n" + WHITE_BOLD + "==================== [" + color + " RESULTS " + WHITE_BOLD + "] ====================" + RESET + "\n");
        System.out.println(color + "Success : " + countOk + RESET);
        System.out.println(color + "Failures : " + failures.size() + RESET);
        if(!failures.isEmpty()) {
            System.out.println();
            failures.forEach((k, v) -> System.out.println(RED_BOLD + " - " + k.getName() + " > " + RED + v));
        }
        System.out.println("\n" + WHITE_BOLD + "=====================================================" + RESET + "\n");

        Assertions.assertTrue(failures.isEmpty());
    }

}
