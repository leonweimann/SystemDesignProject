package Coordination;

import Tasks.*;

/**
 * The TaskCoordinator class is responsible for managing the phases and tasks of the system.
 * It initializes the system in the BOOTING phase and provides methods to update the phase,
 * retrieve the current task, and execute the current task.
 * 
 * @author leonweimann
 * @version 1.4
 */
public class TaskCoordinator {
    /**
     * The current phase of the system.
     */
    private Phase currentPhase;

    /**
     * Creates a new TaskCoordinator instance with the current phase set to BOOTING.
     */
    public TaskCoordinator() {
        currentPhase = Phase.BOOTING;
    }

    /**
     * Returns the current phase of the system.
     * 
     * @return The current phase of the system.
     */
    public Phase getPhase() {
        return currentPhase;
    }

    /**
     * Executes the current task by updating the phase and running the task.
     * This method first updates the phase of the task coordinator and then
     * retrieves and runs the current task.
     */
    public void refresh() {
        updatePhase();
        getCurrentTask().run();
    }

    /**
     * Transitions the system from the BOOTING phase to the next appropriate phase.
     * This method should be called once the booting process is complete.
     */
    public void finishBooting() {
        // currentPhase = Phase. TODO: Set to first phase after booting ...
    }

    /**
     * Updates the current phase of the system.
     * 
     * This method should be called regularly in the main while loop to ensure the
     * system transitions to the appropriate phase based on the current state.
     */
    private void updatePhase() {
        // TODO: Implement phase update logic here ...
    }

    /**
     * Retrieves the current task based on the current phase.
     *
     * @return the current task, which is a new instance of LineFollower by default.
     */
    private Task getCurrentTask() {
        // TODO: Handle all Phase cases ...
        switch (currentPhase) {
            default:
                return new LineFollower();
        }
    }
}