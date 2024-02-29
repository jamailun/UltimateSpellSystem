package fr.jamailun.ultimatespellsystem.dsl.tokenization;

public record TokenPosition(int line, int col) {
    @Override
    public String toString() {
        if(line == -1 && col == -1)
            return "(?:?)";
        return "("+line+":"+col+")";
    }

    public static TokenPosition unknown() {
        return new TokenPosition(-1, -1);
    }

}
