package Tasks;

import Coordination.LCDHelper;
import Coordination.RuntimeCoordinator;
import Models.Symbol;

public class SymbolFetching extends Task {
    public SymbolFetching(RuntimeCoordinator runtime) {
        super(runtime);
    }

    @Override
    public void main() {
        Symbol symbol = runtime.lightController.readSymbol();
        LCDHelper.appendingToDisplay(symbol.debugDescription(), false, 1);
    }
}
