package app.hotel.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

public class ReservationController {

    @FXML
    private TextField reservationGuestId;

    @FXML
    private TextField reservationRoomId;

    @FXML
    private TextField reservationStartDate;

    @FXML
    private TextField reservationEndDate;

    @FXML
    private TextField reservationTotalPrice;

    @FXML
    private TextField reservationDateFrom;

    @FXML
    private TextField reservationDateTo;


    public void addReservation() {
        //TODO
        System.out.println("reservation controller add");
        printTextFields();
        switchMainWindow();
    }

    public void modifyReservation() {
        //TODO
        System.out.println("reservation controller modify");
        printTextFields();
        switchMainWindow();
    }

    public void generateReport() {
        //TODO
        System.out.println("reservation controller generate report");
        String regex = "[0-3][0-9].[0-1][0-9].[0-9][0-9][0-9][0-9]";
        if (reservationDateFrom.getText().matches(regex) && reservationDateTo.getText().matches(regex))
            switchMainWindow();
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Błąd");
            alert.setHeaderText("Błąd");
            alert.setContentText("Co najmniej jedna z podanych dat nie zgadza się z podanym wzorem!");
            alert.showAndWait();
        }
    }

    public void printTextFields() {
        System.out.println(reservationGuestId.getText());
        System.out.println(reservationRoomId.getText());
        System.out.println(reservationStartDate.getText());
        System.out.println(reservationEndDate.getText());
        System.out.println(reservationTotalPrice.getText());

    }

    public void switchMainWindow() {
        AuxiliaryController.switchMainWindow();
    }
}
