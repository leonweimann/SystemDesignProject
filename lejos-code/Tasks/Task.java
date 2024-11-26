package Tasks;

import Coordination.RuntimeCoordinator;

/**
 * The {@code Task} class serves as an abstract base class for all tasks that can be executed
 * by the robot. It provides a reference to the {@code RuntimeCoordinator} instance and 
 * requires subclasses to implement the {@code run} method, which contains the task-specific 
 * execution logic.
 * 
 * Responsibilities:
 * - Provide a common interface for all tasks.
 * - Ensure that each task has access to the {@code RuntimeCoordinator}.
 * 
 * Subclasses must implement:
 * - {@code run()}: Defines the specific actions to be performed when the task is executed.
 * 
 * @author leonweimann
 * @version 1.2
 * 
 * @see RuntimeCoordinator
 */
public abstract class Task {
    protected RuntimeCoordinator runtime = RuntimeCoordinator.getInstance();

    public abstract void run();
}
