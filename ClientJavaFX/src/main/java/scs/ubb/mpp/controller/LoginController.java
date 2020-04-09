package scs.ubb.mpp.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import scs.ubb.mpp.entity.User;
import scs.ubb.mpp.services.Services;

import java.io.IOException;


public class LoginController {
    @FXML
    private TextField usernameTextField;

    @FXML
    private PasswordField passwordTextField;
    private Services service;

    @FXML
    public void initialize() {
    }

    public void setService(Services service) {
        this.service = service;
    }

    public void loginButtonOnAction(ActionEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/tripView.fxml"));

            AnchorPane root = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Trips");
            dialogStage.initModality(Modality.WINDOW_MODAL);

            Scene scene = new Scene(root);
            dialogStage.setScene(scene);
            dialogStage.setWidth(1300);

            TripController tripController = loader.getController();
            User user = new User(
                    usernameTextField.getText(),
                    passwordTextField.getText());
            service.login(user, tripController);

            tripController.setItems(service, user);
            dialogStage.show();

            ((Node)(event.getSource())).getScene().getWindow().hide();
        } catch (Exception e) {
            Alert message = new Alert(Alert.AlertType.ERROR);
            message.initOwner(null);
            message.setTitle("Mesaj eroare");
            message.setContentText(e.getMessage());
            message.showAndWait();
            return;
        }
    }
}
