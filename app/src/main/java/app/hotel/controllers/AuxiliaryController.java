package app.hotel.controllers;

import app.hotel.Main;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.net.URL;

public class AuxiliaryController {
    public static void switchMainWindow() {
        URL mainWindowLocation = Main.class.getResource("/" + "basic.fxml");
        changeScene(mainWindowLocation, 0, 0);
    }

    public static void changeScene(URL url, int width, int height) {
        try {
            Main.setScene(url, width, height);
        } catch (IOException e) {
            System.err.println(url);
            e.printStackTrace();
        }
    }

    public static void changeScene(URL url, int width, int height, Object object) {
        try {
            Main.setScene(url, width, height, object);
        } catch (IOException e) {
            System.err.println(url);
            e.printStackTrace();
        }
    }

    public static void generateError(String text) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Błąd");
        alert.setHeaderText("Błąd");
        alert.setContentText(text);
        alert.showAndWait();
    }

}
