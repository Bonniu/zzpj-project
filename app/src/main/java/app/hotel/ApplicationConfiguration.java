package app.hotel;

import app.database.ServerConfiguration;
import app.database.api.CurrencyService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;


@Configuration
@Import(ServerConfiguration.class)
public class ApplicationConfiguration {

    ServerConfiguration serverConfiguration;

    public ApplicationConfiguration(ServerConfiguration serverConfiguration) {
        this.serverConfiguration = serverConfiguration;
    }

    @Bean
    public CurrencyService currencyService() {
        return new CurrencyService(serverConfiguration.restTemplate(), serverConfiguration.strategyFactory());
    }




}
