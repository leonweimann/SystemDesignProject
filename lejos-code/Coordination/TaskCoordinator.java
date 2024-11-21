package Coordination;

import Tasks.*;

/**
 * The TaskCoordinator class is responsible for managing the current phase of
 * the system and providing the appropriate task based on the current phase.
 * 
 * <p>
 * This class follows the Singleton design pattern to ensure that only one
 * instance of TaskCoordinator exists throughout the application.
 * </p>
 * 
 * <p>
 * Usage:
 * </p>
 * 
 * <pre>
 * TaskCoordinator coordinator = TaskCoordinator.getInstance();
 * Phase currentPhase = coordinator.getPhase();
 * Task currentTask = coordinator.getCurrentTask();
 * </pre>
 * 
 * <p>
 * Phases are managed internally and can be updated using the
 * {@link #updatePhase()} method.
 * </p>
 * 
 * <p>
 * Tasks are determined based on the current phase and are returned by the
 * {@link #getCurrentTask()} method.
 * </p>
 * 
 * @author leonweimann
 * @version 1.0
 * 
 * @see Phase
 * @see Task
 */
public class TaskCoordinator {
    /** Singleton instance of the TaskCoordinator */
    public static TaskCoordinator instance;

    /** The current phase of the system */
    private Phase currentPhase;

    /**
     * Private constructor to prevent external insantiation of TaskCotordinator.
     * Initializes the current phase to Phase.BOOTING.
     */
    private TaskCoordinator() {
        currentPhase = Phase.BOOTING;
    }

    /**
     * Returns the singleton instance of TaskCoordinator.
     * If the instance does not exist, it is created.
     * 
     * @return The singleton instance of TaskCoordinator.
     */
    public static synchronized TaskCoordinator getInstance() {
        if (instance == null) {
            instance = new TaskCoordinator();
        }
        return instance;
    }

    /**
     * Returns the current phase of the system.
     * 
     * @return The current phase.
     */
    public Phase getPhase() {
        return currentPhase;
    }

    /**
     *
     * Updates the current phase of the system.
     * 
     * This method should be called regularly in the main while loop to ensure the
     * system transitions to the appropriate phase based on the current state.
     */
    public void updatePhase() {
        // TODO: Implement phase update logic here ...
    }

    /**
     * Retrieves the current task based on the current phase.
     *
     * @return the current task, which is a new instance of LineFollower by default.
     */
    public Task getCurrentTask() {
        // TODO: Handle all Phase cases ...
        switch (currentPhase) {
            default:
                return new LineFollower();
        }
    }
}