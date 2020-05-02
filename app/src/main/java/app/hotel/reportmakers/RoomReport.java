package app.hotel.reportmakers;

import app.database.entities.Reservation;
import app.database.entities.Room;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.collections.ObservableList;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class RoomReport {

    private final ArrayList<Room> rooms;
    private final Font polishFont;


    public RoomReport(ObservableList<Room> rooms) {

        this.rooms = new ArrayList<>(rooms);
        Font font;
        try {
            font = new Font(BaseFont
                    .createFont(BaseFont.HELVETICA, BaseFont.CP1250, BaseFont.EMBEDDED), 16);

        } catch (Exception e) {
            font = new Font();
        }
        this.polishFont = font;
    }

    public void generateReport() {
        //TODO refactor (duplicate code)
        Document document = new Document();
        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(getFileName()));
            document.open();
            fillDocument(document);
            document.close();
            writer.close();
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
    }

    private void fillDocument(Document d) throws DocumentException {
        addHeading(d);
        addAvailableRooms(d);
        addUnavailableRooms(d);
    }

    private void addHeading(Document d) {
        try {
            String s = "Raport dotyczący pokoi (stan na dzień " + LocalDate.now().toString() + ")";
            d.add(new Chunk(s, polishFont));
            d.add(new Paragraph("")); //new line
        } catch (DocumentException ignored) {

        }
    }

    private void addAvailableRooms(Document d) throws DocumentException {
        long availableRooms = rooms.stream()
                .filter(Room::getState).count();

        String paragraph = "Wolne pokoje: " + availableRooms;
        d.add(new Paragraph(paragraph, polishFont));
    }

    private void addUnavailableRooms(Document d) throws DocumentException {
        long unavailableRooms = rooms.stream()
                .filter(x -> !x.getState()).count();

        String paragraph = "Niedostępne pokoje: " + unavailableRooms;
        d.add(new Paragraph(paragraph, polishFont));
    }

    private String getFileName() {
        int h = LocalDateTime.now().getHour();
        int m = LocalDateTime.now().getMinute();
        String dateAndClock = LocalDate.now().toString() + "_" + h + "-";
        if (m < 10)
            dateAndClock += "0";
        dateAndClock += m;
        String prefix = "room_report_";
        String suffix = ".pdf";
        return prefix + dateAndClock + suffix;
    }
}
