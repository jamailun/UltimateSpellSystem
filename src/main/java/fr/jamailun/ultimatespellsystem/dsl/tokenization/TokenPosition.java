package fr.jamailun.ultimatespellsystem.dsl.tokenization;

public record TokenPosition(int line, int col) {
    @Override
    public String toString() {
        return "("+line+":"+col+")";
    }

    public static TokenPosition fake() {
        return new TokenPosition(0, 0);
    }

}
