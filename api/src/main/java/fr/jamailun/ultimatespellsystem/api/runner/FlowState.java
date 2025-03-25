package fr.jamailun.ultimatespellsystem.api.runner;

/**
 * State of the flow of a spell execution.
 */
public enum FlowState {

    /**
     * Spell is running normally.
     */
    RUNNING,

    /**
     * Flow is broken. The current iteration will be stopped.
     */
    BROKEN,

    /**
     * Flow is broken. The current iteration continue with the next element.
     */
    BROKEN_CONTINUE,

    /**
     * Flow is stopped. Not more statement should be executed.
     */
    STOPPED;

    /**
     * Test if the flow is running.
     * @return true if the flow may be stopped.
     */
    public boolean isNotRunning() {
        return this != RUNNING;
    }
}
