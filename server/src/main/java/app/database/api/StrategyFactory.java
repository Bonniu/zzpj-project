package app.database.api;

import app.database.api.strategy.Exchange;
import app.database.api.strategy.StrategyName;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class StrategyFactory {

    private Map<String, Exchange> strategies;

    public StrategyFactory(Set<Exchange> strategySet) {
        strategies = new HashMap<String, Exchange>();
        strategySet.forEach(
                strategy ->strategies.put(strategy.getStrategyName(), strategy));
    }

    public Exchange findStrategy(String strategyName) {
        return strategies.get(strategyName);
    }

}
