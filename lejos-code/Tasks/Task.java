package Tasks;

import Coordination.RuntimeCoordinator;

public abstract class Task {
    public Task(RuntimeCoordinator runtime) {
        this.runtime = runtime;
    }

    protected RuntimeCoordinator runtime;

    protected abstract void main();

    public final void run() {
        if (execute) {
            main();
        }
    }

    private boolean execute = true;

    public void terminate() {
        execute = false;
    }

    public void start() {
        execute = true;
    }

    public boolean isExecuting() {
        return execute;
    }
}