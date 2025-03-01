package fr.jamailun.ultimatespellsystem.extension;

import fr.jamailun.ultimatespellsystem.api.runner.RuntimeStatement;
import fr.jamailun.ultimatespellsystem.dsl.UltimateSpellSystemDSL;
import fr.jamailun.ultimatespellsystem.dsl.errors.UssException;
import fr.jamailun.ultimatespellsystem.dsl.nodes.StatementNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.variables.TypesContext;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.CharStream;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenStream;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.Tokenizer;
import fr.jamailun.ultimatespellsystem.plugin.runner.builder.SpellBuilderVisitor;
import fr.jamailun.ultimatespellsystem.runner.framework.TestFramework;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;

import java.io.File;
import java.util.*;
import java.util.stream.Stream;

/**
 * Test framework for parsing tests.
 */
abstract class ParseAndCompileTest extends TestFramework {

    protected final File PARSINGS_FILE = new File("src/test/resources/extension-parsing");

    protected @NotNull List<File> listTests(@NotNull String subFolder) {
        File directory = new File(PARSINGS_FILE, subFolder);
        Assertions.assertTrue(directory.exists() && directory.isDirectory(), "Directory '" + subFolder + "' does not exist.");

        File[] children = directory.listFiles();
        if(children == null) {
            return Collections.emptyList();
        }
        return Stream.of(children)
                .filter(File::isFile)
                .sorted(Comparator.comparing(File::getName))
                .toList();
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

    protected List<RuntimeStatement> parseAndVerify(@NotNull File file) throws UssException {
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
            System.out.println("> " + node);
            node.validateTypes(context);
        }
        return SpellBuilderVisitor.build(nodes);
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

        if(!failures.isEmpty())
            System.exit(42);
    }

}
