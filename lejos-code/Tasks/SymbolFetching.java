package Tasks;

import Coordination.LCDHelper;
import Models.Symbol;

public class SymbolFetching implements Task {
    @Override
    public void run() {
        Symbol symbol = runtime.lightController.readSymbol();
        LCDHelper.display(symbol.debugDescription());
    }
}
