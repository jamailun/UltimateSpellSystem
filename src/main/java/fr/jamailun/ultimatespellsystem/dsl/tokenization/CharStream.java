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

    /**
     * Create a new character stream from a string.
     * @param string the string to use.
     * @return a new CharStream.
     */
    @Contract("_ -> new")
    public static @NotNull CharStream from(@NotNull String string) {
        return new CharStream((string + "\n").toCharArray());
    }

    /**
     * Create a new character stream from a file.
     * @param file the file to use.
     * @return a new CharStream.
     */
    @Contract("_ -> new")
    public static @NotNull CharStream from(@NotNull File file) {
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

    /**
     * Peek the next character.
     * @return the character on current index.
     */
    public char peek() {
        return chars[index];
    }

    /**
     * Get the current character, and move the index.
     * @return the character on current index.
     */
    public char next() {
        char c = chars[index ++];
        position.column++;
        if(c == '\n') {
            position.column = 1;
            position.line++;
        }
        return c;
    }

    /**
     * Drop the current character, by simply moving the index.
     */
    public void drop() {
        if(!hasMore())
            throw new RuntimeException("No more data.");
        index++;
    }

    /**
     * Test if more character are remaining.
     * @return {@code true} if more characters are remaining.
     */
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

    /**
     * Get the current position of the token.
     * @return the current position.
     */
    public @NotNull TokenPosition pos() {
        return position.pos();
    }

}
