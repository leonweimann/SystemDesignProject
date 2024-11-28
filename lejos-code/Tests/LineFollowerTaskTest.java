package Tests;

import Coordination.RuntimeCoordinator;
import Tasks.LineFollower;

public class LineFollowerTaskTest extends Test {
    private LineFollower task = new LineFollower();

    @Override
    protected void setup() {
        // Nothing to setup
    }

    @Override
    protected boolean executionLoop() {
        attachMultiTesting();
        switch (currentTestCount) {
            case 0:
                task.run();
            case 1:
                task.searchLine();
                System.out.println("Finished searchLine()");
                currentTestCount++;
            case 2:
                System.out.println("No task..");
                RuntimeCoordinator.executionFrequencyDelay();
        }
        return true; // Continue execution
    }
}
