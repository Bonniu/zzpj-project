package app.database.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;


@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Reservation {

    @Id
    private String id;
    private String guestId;
    private String roomId;
    private Date startDate;
    private Date endDate;
    private float totalPrice;
    private boolean isPayed;



}
