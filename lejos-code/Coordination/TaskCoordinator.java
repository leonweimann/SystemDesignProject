package Coordination;

import Tasks.*;

public class TaskCoordinator {
    public TaskCoordinator() {
    }

    private SymbolFetching symbolFetching = new SymbolFetching();

    public void executeTasks() {
        symbolFetching.run();

        LCDHelper.display("Tasks executing!");
    }
}
