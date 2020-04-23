package app.hotel;

import app.database.api.CurrencyService;
import app.database.api.StrategyFactory;
import app.database.api.strategy.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.HashSet;
import java.util.Set;

@Configuration
public class ApplicationConfiguration {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }


    @Bean
    public StrategyFactory strategyFactory() {
        Set<Exchange> exchanges = new HashSet<>();
        exchanges.add(new USDExchange());
        exchanges.add(new PLNExchange());
        exchanges.add(new AUDExchange());
        exchanges.add(new MXNExchange());
        exchanges.add(new EURExchange());
        exchanges.add(new CADExchange());

        return new StrategyFactory(exchanges);
    }

    @Bean
    public CurrencyService currencyService() {
        return new CurrencyService(restTemplate(), strategyFactory());
    }
}
