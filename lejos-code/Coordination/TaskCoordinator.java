package Coordination;

import Tasks.*;

public final class TaskCoordinator {
    public TaskCoordinator(RuntimeCoordinator runtime) {
        this.runtime = runtime;
        this.lineFollower = new LineFollower(runtime);
        this.symbolFetching = new SymbolFetching(runtime);
    }

    private LineFollower lineFollower;
    private SymbolFetching symbolFetching;

    private RuntimeCoordinator runtime;

    private boolean terminateRequired = false;

    public void executeFrequent() {
        LCDHelper.resetAppendedItems();
        if (!terminateRequired) {
            lineFollower.main();
            symbolFetching.main();
        }
    }

    public void executeCrutial() {
        terminateRequired = runtime.touchController.obstacleFound();
    }
}
