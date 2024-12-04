package Tasks;

import Coordination.RuntimeCoordinator;

public interface Task {
    public RuntimeCoordinator runtime = RuntimeCoordinator.getInstance();
    public void run();
}