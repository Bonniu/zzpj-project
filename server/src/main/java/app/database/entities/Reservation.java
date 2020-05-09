package app.database.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.Date;


@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Reservation {

    @Id
    private String id;
    private long guestId;
    private String roomId;
    private LocalDate startDate;
    private LocalDate endDate;
    private float totalPrice;
    private boolean isPayed;



}
