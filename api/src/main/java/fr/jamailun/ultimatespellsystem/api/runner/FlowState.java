package fr.jamailun.ultimatespellsystem.api.runner;

public enum FlowState {

    RUNNING,

    BROKEN,

    BROKEN_CONTINUE,

    STOPPED;

    public boolean isRunning() {
        return this == RUNNING;
    }
    public boolean isNotRunning() {
        return this != RUNNING;
    }

}
