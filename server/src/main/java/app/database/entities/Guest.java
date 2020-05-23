package app.database.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Guest {

    @Id
    private String IDcard;
    private String name;
    private String surname;
    private int phoneNumber;
    private int discount;


}
