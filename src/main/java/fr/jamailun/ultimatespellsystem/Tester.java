package fr.jamailun.ultimatespellsystem;

import fr.jamailun.ultimatespellsystem.dsl.UltimateSpellSystemDSL;
import fr.jamailun.ultimatespellsystem.dsl.nodes.StatementNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypePrimitive;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypesContext;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.CharStream;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenStream;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.Tokenizer;

import java.util.List;

public class Tester {

    public static void main(String[] args) {
        String s = """
                # comment !
                
                define %players_around = all players within 50 around %caster;
                
                run after 5s: {
                    define %var = "test"
                    send to %caster message %var;
                }
                
                # send to %caster effect SPEED 2 for 3 minutes;
                """;

        TokenStream tokens = Tokenizer.tokenize(CharStream.from(s));
        System.out.println(tokens + "\n");

        List<StatementNode> nodes = UltimateSpellSystemDSL.parse(s);
        nodes.forEach(System.out::println);

        System.out.println("\n== TYPE VALIDATION ==\n");

        TypesContext context = new TypesContext();
        context.registerAbsolute("caster", TypePrimitive.ENTITY.asType());

        for(StatementNode node : nodes) {
            node.validateTypes(context);
            System.out.println(node);
        }
    }

}
