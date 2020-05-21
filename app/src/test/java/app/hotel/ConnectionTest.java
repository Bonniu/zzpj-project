package app.hotel;

import app.database.entities.Guest;
import app.database.entities.Reservation;
import app.database.repositories.GuestRepository;
import app.database.repositories.ReservationRepository;
import com.mongodb.DBCollection;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.List;

import static com.mongodb.internal.connection.tlschannel.util.Util.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {App.class})
@EnableAutoConfiguration //mongoDB
@ActiveProfiles("test") // Like this
public class ConnectionTest{

    @Autowired
    private GuestRepository guestRepository;

    @Test
    public void givenGenericEntityRepository_whenSaveAndRetreiveEntity_thenOK() {
        Guest guest = guestRepository
                .save(new Guest("123455678912","Marek","Szafran",123456789, 2));
        List<Guest> foundEntity = guestRepository.findAll();

        assertNotNull(foundEntity);
        int last = foundEntity.size();
        assertEquals(guest.getPesel(), foundEntity.get(last-1).getPesel());
    }


}

