package app.hotel;

import app.database.api.CurrencyService;
import app.database.entities.RateModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ApplicationConfiguration {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public RateModel rateModel() {return new RateModel();}

    @Bean
    public CurrencyService currencyService() {
        return new CurrencyService(restTemplate(), rateModel());
    }
}
