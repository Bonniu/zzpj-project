package app.database.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@NoArgsConstructor
public class Room {

    @Id
    private String number;
    private int capacity;
    private float price;
    private String state;

    public Room(String number, int capacity, float price, String state) {
        this.number = number;
        this.capacity = capacity;
        this.price = price;
        setState(state);
    }

    public void setState(String state) {
        if (state.equals("dostępny") || state.equals("zajęty"))
            this.state = state;
        else
            this.state = "niedostępny";
    }


}
