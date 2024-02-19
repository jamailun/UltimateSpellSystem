package fr.jamailun.ultimatespellsystem.dsl;

public record TokenPosition(int line, int col) {
    @Override
    public String toString() {
        return "("+line+":"+col+")";
    }
}
