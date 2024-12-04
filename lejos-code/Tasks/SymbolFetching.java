package Tasks;

import Coordination.LCDHelper;
import Models.Symbol;

public class SymbolFetching extends Task {
    @Override
    public void main() {
        Symbol symbol = runtime.lightController.readSymbol();
        LCDHelper.display(symbol.debugDescription());
    }
}
