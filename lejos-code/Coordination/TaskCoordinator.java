package Coordination;

import Tasks.*;

public final class TaskCoordinator {
    public TaskCoordinator(RuntimeCoordinator runtime) {
        this.runtime = runtime;
        this.lineFollower = new LineFollower(runtime);
    }

    private LineFollower lineFollower;

    private RuntimeCoordinator runtime;

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
