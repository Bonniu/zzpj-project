package app.hotel.controllers;

import app.database.entities.Guest;
import app.hotel.Main;
import org.springframework.web.servlet.mvc.Controller;

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
            Main.setScene(url, width, height,object);
        } catch (IOException e) {
            System.err.println(url);
            e.printStackTrace();
        }
    }
}
