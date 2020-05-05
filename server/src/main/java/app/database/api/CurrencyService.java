package app.database.api;

import app.database.entities.CurrencyRestModel;
import app.database.entities.RateModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Field;
import java.util.ArrayList;

@Service
public class CurrencyService {

    private static final String API_KEY = "5b34a02bed9a1306cfc730dcabc881ad";
    private final RestTemplate restTemplate;
    private CurrencyRestModel currencyRestModel;
    private final StrategyContext strategyContext;

    @Autowired
    public CurrencyService(RestTemplate restTemplate, StrategyContext strategyContext) {
        this.restTemplate = restTemplate;
        this.strategyContext = strategyContext;
    }

    public void getCurrency() {
        final String uri = "http://data.fixer.io/api/latest?access_key=" + API_KEY + "&symbols=USD,AUD,CAD,PLN,MXN&format=1";
        ResponseEntity<CurrencyRestModel> result = restTemplate.getForEntity(uri, CurrencyRestModel.class);
        currencyRestModel = result.getBody();
    }

    public ArrayList<String> getPossibleRates() {

       Field[] fields = RateModel.class.getDeclaredFields();
       ArrayList<String> arrayList = new ArrayList<>();
        for(Field f : fields) {
            arrayList.add(f.getName());
        }
        arrayList.add("EUR");
        return arrayList;
    }

    public StrategyContext getStrategyContext() {
        return strategyContext;
    }

    public CurrencyRestModel getCurrencyRestModel() {
        return currencyRestModel;
    }
}
