package app.hotel.reportmakers;

import app.database.entities.Reservation;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.stream.Collectors;

import static app.hotel.controllers.AuxiliaryController.generateError;

public class ReservationReport {

    private final LocalDate reportDateFrom;
    private final LocalDate reportDateTo;
    private final ArrayList<Reservation> reservations;
    private final Font polishFont;


    public ReservationReport(LocalDate reportDateFrom, LocalDate reportDateTo, ObservableList<Reservation> reservations) {
        this.reportDateFrom = reportDateFrom;
        this.reportDateTo = reportDateTo;

        this.reservations = reservations.stream()
                .filter(x -> !reportDateFrom.isAfter(x.getStartDate()))
                .filter(x -> !reportDateTo.isBefore(x.getEndDate()))
                .collect(Collectors.toCollection(ArrayList::new));
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
        if (reportDateFrom.isAfter(reportDateTo)) {
            generateError("Data początkowa musi być wcześniej niż data zakończenia");
            return;
        }
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
        addUnfinishedReservations(d);
        addFinishedReservations(d);
        addNotPaidReservations(d);
        addPaidReservations(d);
    }

    private void addHeading(Document d) {
        try {
            String s = "Raport dotyczący rezerwacji z dn. " + LocalDate.now().toString();
            Chunk chunk = new Chunk(s, polishFont);
            d.add(chunk);

            d.add(new Paragraph("")); //new line

            s = "\nPrzedział czasowy: " + reportDateFrom + " - " + reportDateTo;
            chunk = new Chunk(s, polishFont);
            d.add(chunk);

            d.add(new Paragraph("")); //new line
        } catch (DocumentException ignored) {

        }
    }

    private void addUnfinishedReservations(Document d) throws DocumentException {
        ArrayList<Reservation> notFinished = reservations.stream()
                .filter(x -> !x.getEndDate().isAfter(LocalDate.now()))
                .collect(Collectors.toCollection(ArrayList::new));
        List orderedList = new List(List.ORDERED);
        for (Reservation r : notFinished)
            orderedList.add(new ListItem(r.toString()));

        String paragraph = "Rezerwacje niezakończone - " + orderedList.size();
        addContent(paragraph, d, orderedList);
    }

    private void addFinishedReservations(Document d) throws DocumentException {
        ArrayList<Reservation> finished = reservations.stream()
                .filter(x -> x.getEndDate().isAfter(LocalDate.now()))
                .collect(Collectors.toCollection(ArrayList::new));
        List orderedList = new List(List.ORDERED);
        for (Reservation r : finished)
            orderedList.add(new ListItem(r.toString()));

        String paragraph = "Rezerwacje zakończone - " + orderedList.size();
        addContent(paragraph, d, orderedList);
    }

    private void addNotPaidReservations(Document d) throws DocumentException {
        ArrayList<Reservation> notPaid = reservations.stream()
                .filter(x -> !x.isPayed())
                .collect(Collectors.toCollection(ArrayList::new));
        List orderedList = new List(List.ORDERED);
        for (Reservation r : notPaid)
            orderedList.add(new ListItem(r.toString()));

        String paragraph = "Rezerwacje nieopłacone - " + orderedList.size();
        addContent(paragraph, d, orderedList);
    }

    private void addPaidReservations(Document d) throws DocumentException {
        ArrayList<Reservation> paid = reservations.stream()
                .filter(Reservation::isPayed)
                .collect(Collectors.toCollection(ArrayList::new));
        List orderedList = new List(List.ORDERED);
        for (Reservation r : paid)
            orderedList.add(new ListItem(r.toString()));
        d.add(orderedList);

        String paragraph = "Rezerwacje opłacone - " + orderedList.size();
        addContent(paragraph, d, orderedList);
    }

    private void addContent(String paragraphText, Document d, List orderedList) throws DocumentException {
        if (orderedList.size() > 0) {
            d.add(new Paragraph(paragraphText + ":", polishFont));
            d.add(orderedList);
        } else
            d.add(new Paragraph(paragraphText, polishFont));
    }

    private String getFileName() {
        int h = LocalDateTime.now().getHour();
        int m = LocalDateTime.now().getMinute();
        String dateAndClock = LocalDate.now().toString() + "_" + h + "-";
        if (m < 10)
            dateAndClock += "0";
        dateAndClock += m;
        String prefix = "reservation_report_";
        String suffix = ".pdf";
        return prefix + dateAndClock + suffix;
    }

}
