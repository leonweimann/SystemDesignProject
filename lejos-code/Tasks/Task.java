package Tasks;

import Coordination.RuntimeCoordinator;

public abstract class Task {
    public RuntimeCoordinator runtime = RuntimeCoordinator.getInstance();

    private boolean execute = true;

    public void run() {
        if (execute) {
            main();
        }
    }
    
    public abstract void main();

    public void start() {
        execute = true;
    }

    public void terminate() {
        execute = false;
    }
}