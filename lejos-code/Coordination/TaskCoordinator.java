package Coordination;

import Tasks.*;

/**
 * The TaskCoordinator class is responsible for managing the execution of tasks
 * within the system. It follows the Singleton design pattern to ensure that
 * only one instance of the TaskCoordinator exists. The class maintains the
 * current phase of the system and provides methods to execute tasks and update
 * the phase.
 * 
 * <p>
 * The TaskCoordinator initializes the system in the BOOTING phase and
 * transitions through different phases based on the system's state. It
 * retrieves and runs tasks corresponding to the current phase.
 * </p>
 * 
 * <p>
 * Usage example:
 * 
 * <pre>
 * {@code
 * TaskCoordinator coordinator = TaskCoordinator.getInstance();
 * coordinator.execute();
 * }
 * </pre>
 * </p>
 * 
 * @version 1.4
 * @author MarianMeißner
 */


 public class TaskCoordinator {
     /** Singleton instance of the TaskCoordinator */
     private static TaskCoordinator instance;
 
     /** The current phase of the system */
     private Phase currentPhase;
 
     /**
      * Private constructor to prevent external instantiation of TaskCoordinator.
      * Initializes the current phase to Phase.BOOTING.
      */
     private TaskCoordinator() {
         currentPhase = Phase.BOOTING; // Initial Phase can be BOOTING or another default phase
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
     public static void execute() {
         getInstance().updatePhase();
         getInstance().getCurrentTask().run();
     }
 
     /**
      * Updates the current phase of the system.
      * This method should be called regularly in the main while loop to ensure
      * the system transitions to the appropriate phase based on the current state.
      */
     private void updatePhase() {
         // Here, you could define logic to transition between phases based on conditions.
         // For example, if a sensor detects that the robot should stop or follow a line,
         // the phase could change.
         
         // For now, we are assuming that we are in the LINE_FOLLOWING phase.
         // In a more complex system, this logic would transition between different phases.
     }
 
     /**
      * Retrieves the current task based on the current phase.
      *
      * @return the current task, which is a new instance of LineFollower in this case.
      */
     private Task getCurrentTask() {
         // In this example, we default to the LineFollower task when in LINE_FOLLOWING phase.
         switch (currentPhase) {
             case BOOTING:
             default:
                 return new LineFollower(); // Default task (you can customize further)
         }
     }
 }