package Coordination;

import Tasks.*;

public class TaskCoordinator {
    public static TaskCoordinator instance = new TaskCoordinator();

    private TaskCoordinator() {
        currentPhase = Phase.BOOTING;
    }

    private Phase currentPhase;

    public Phase getPhase() {
        return currentPhase;
    }

    public void updatePhase() {

    }

    // Handle all Phase cases ...
    public Task getCurrentTask() {
        switch (currentPhase) {
            default:
                return new LineFollower();
        }
    }
}