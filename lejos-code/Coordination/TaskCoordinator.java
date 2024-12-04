package Coordination;

import Tasks.*;

public class TaskCoordinator {
    public TaskCoordinator() {
    }

    private RuntimeCoordinator runtime = RuntimeCoordinator.getInstance();

    private LineFollower lineFollower = new LineFollower();
    private SymbolFetching symbolFetching = new SymbolFetching();

    public void executeFrequent() {
        lineFollower.run();
    }

    public void executeCrutial() {
        if (runtime.touchController.obstacleFound()) {
            lineFollower.terminate();
        } else {
            lineFollower.start();
        }
    }
}
