package fr.jamailun.ultimatespellsystem;

import fr.jamailun.ultimatespellsystem.dsl.UltimateSpellSystemDSL;
import fr.jamailun.ultimatespellsystem.dsl.nodes.StatementNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypePrimitive;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypesContext;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.CharStream;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenStream;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.Tokenizer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

public class ParsingTest {

    @Test
    void testParsingValid() {
        File resourcesDirectory = new File("src/test/resources");
        File validSpells = new File(resourcesDirectory, "parsing/corrects");
        Assertions.assertTrue(validSpells.exists() && validSpells.isDirectory());

        File[] children = validSpells.listFiles();
        if(children == null) {
            System.err.println("Pas de tests !");
            return;
        }

        boolean failed = false;
        for(File file : children) {
            System.out.println("\n\n ================[ " + file.getName() + "]================\n");
            try {
                // Tokenize
                TokenStream tokens = Tokenizer.tokenize(CharStream.from(file));
                System.out.println(tokens + "\n");

                // Parse
                List<StatementNode> nodes = UltimateSpellSystemDSL.parse(tokens);

                System.out.println(" ----------------------- ");

                // validate
                TypesContext context = new TypesContext();
                context.registerAbsolute("caster", TypePrimitive.ENTITY.asType());
                for(StatementNode node : nodes) {
                    node.validateTypes(context);
                    System.out.println("> " + node);
                }

            } catch(Exception e) {
                e.printStackTrace();
                failed = true;
            }
        }

        Assertions.assertFalse(failed, "One or more tests failed.");
    }

}
