package Coordination;

import Tasks.*;

public final class TaskCoordinator {
    public TaskCoordinator(RuntimeCoordinator runtime) {
        this.runtime = runtime;
        this.lineFollower = new LineFollower(runtime);
    }

    private LineFollower lineFollower;

    private RuntimeCoordinator runtime;

    private boolean terminateRequired = false;

    public void executeFrequent() {
        if (!terminateRequired) {
            lineFollower.main();
        }
    }

    public void executeCrutial() {
        terminateRequired = runtime.touchController.obstacleFound();
    }
}
