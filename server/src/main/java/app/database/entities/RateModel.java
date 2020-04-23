package app.database.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RateModel implements Serializable {

    @JsonProperty("PLN")
    private float PLN;
    @JsonProperty("USD")
    private float USD;
    @JsonProperty("AUD")
    private float AUD;
    @JsonProperty("CAD")
    private float CAD;
    @JsonProperty("MXN")
    private float MXN;

}
