package app.database.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Guest{

    @Id
    private long pesel;
    private String name;
    private String surname;
    private int phoneNumber;
    private int discount;


}
