package app.hotel.controllers;

import app.database.entities.User;
import app.hotel.dbservices.UserService;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Getter
@Controller
public class UserController implements ModifyController{

    @FXML
    private TextField userId;
    @FXML
    private TextField userName;
    @FXML
    private TextField userSurname;
    @FXML
    private TextField userType;

    private User selectedUser;

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

        selectedUser.setId(userId.getText());
        selectedUser.setName(userName.getText());
        selectedUser.setSurname(userSurname.getText());
        selectedUser.setUserType(userType.getText());

        userService.updateUser(selectedUser);

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

    @Override
    public void initData(Object object) {
        User user = (User) object;
        selectedUser = user;
        userId.setText(selectedUser.getId());
        userName.setText(selectedUser.getName());
        userSurname.setText(selectedUser.getSurname());
        userType.setText(selectedUser.getUserType());
    }
}
