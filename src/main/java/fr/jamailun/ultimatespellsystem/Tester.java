package fr.jamailun.ultimatespellsystem;

import fr.jamailun.ultimatespellsystem.dsl.tokenization.CharStream;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenStream;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.Tokenizer;

public class Tester {

    public static void main(String[] args) {
        String s = """
                # comment !
                send to %caster message "salut";
                send to %caster effect SPEED 3 for 5s;
                """;

        TokenStream tokens = Tokenizer.tokenize(CharStream.from(s));

        System.out.println(tokens);
    }

}
