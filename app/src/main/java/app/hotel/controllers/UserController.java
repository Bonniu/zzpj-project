package app.hotel.controllers;

import app.database.entities.Room;
import app.database.entities.User;
import app.hotel.dbcontroller.UserService;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Getter
@Controller
public class UserController {

    @FXML
    private TextField userName;
    @FXML
    private TextField userSurname;
    @FXML
    private TextField userType;

    @Autowired
    private UserService userService;

    public void addUser() {
        User user = new User();
        user.setName(getUserName().getText());
        user.setSurname(getUserSurname().getText());
        user.setUserType(getUserType().getText());

        userService.insertUser(user);
        switchMainWindow();
    }

    public void modifyUser() {
        System.out.println("modify user");
        printTextFields();
        switchMainWindow();
    }

    public void printTextFields() {
        System.out.println(userName.getText());
        System.out.println(userSurname.getText());
        System.out.println(userType.getText());
    }

    public void switchMainWindow() {
        AuxiliaryController.switchMainWindow();
    }
}
