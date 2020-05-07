package app.hotel.reportmakers;

import app.database.entities.Guest;
import app.database.entities.Reservation;
import app.database.entities.Room;
import app.hotel.services.implementation.GuestService;
import app.hotel.services.implementation.ReservationService;
import app.hotel.services.implementation.RoomService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.scene.control.Alert;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.stream.Collectors;

import static app.hotel.controllers.AuxiliaryController.generateAlert;

public class ReservationReport {

}
