package fr.jamailun.ultimatespellsystem.dsl.tokenization;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.StringJoiner;

/**
 * A stream of {@link Character}.
 */
public class CharStream {

    private final char[] chars;
    private int index = 0;
    private final PositionProvider position = new PositionProvider();

    public CharStream(char[] chars) {
        this.chars = chars;
    }

    @Contract("_ -> new")
    public static @NotNull CharStream from(String string) {
        return new CharStream((string + "\n").toCharArray());
    }

    public static @NotNull CharStream from(File file) {
        if(!file.exists())
            throw new RuntimeException("File " + file + " does not exist.");
        try(BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringJoiner sj = new StringJoiner("\n");
            String line;
            while ((line = reader.readLine()) != null) {
                sj.add(line);
            }
            sj.add("\n");
            String fileContent = sj.toString();
            return from(fileContent);
        } catch(IOException e) {
            throw new RuntimeException("Cannot read file "+ file, e);
        }
    }

    public char peek() {
        return chars[index];
    }

    public char next() {
        char c = chars[index ++];
        position.column++;
        if(c == '\n') {
            position.column = 1;
            position.line++;
        }
        return c;
    }

    public void drop() {
        if(!hasMore())
            throw new RuntimeException("No more data.");
        index++;
    }

    public boolean hasMore() {
        return index < chars.length - 1;
    }

    private static class PositionProvider {
        int column = 1;
        int line = 1;

        @Contract(" -> new")
        public @NotNull TokenPosition pos() {
            return new TokenPosition(line, column);
        }

    }

    public @NotNull TokenPosition pos() {
        return position.pos();
    }

}
