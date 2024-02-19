package fr.jamailun.ultimatespellsystem;

import fr.jamailun.ultimatespellsystem.dsl.CharStream;
import fr.jamailun.ultimatespellsystem.dsl.TokenStream;
import fr.jamailun.ultimatespellsystem.dsl.Tokenizer;

public class Tester {

    public static void main(String[] args) {
        String s = """
                
                # comment !
                
                var a = "b";
                var x = a * (b - 2 / sigma[pi]);
                
                z == b;
                
                """;

        TokenStream tokens = Tokenizer.tokenize(CharStream.from(s));

        System.out.println(tokens);
    }

}
