package app.database;


import app.database.api.StrategyContext;
import app.database.api.strategy.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.HashSet;
import java.util.Set;

@Configuration
public class ServerConfiguration {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public StrategyContext strategyFactory() {
        Set<Exchange> exchanges = new HashSet<>();
        exchanges.add(new USDExchange());
        exchanges.add(new PLNExchange());
        exchanges.add(new AUDExchange());
        exchanges.add(new MXNExchange());
        exchanges.add(new EURExchange());
        exchanges.add(new CADExchange());

        return new StrategyContext(exchanges);
    }
}
