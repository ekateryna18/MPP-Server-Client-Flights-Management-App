package Controller;

import Model.Employee;
import Service.Service;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    @FXML
    private Label loginLabel;
    @FXML
    private TextField emailField;
    @FXML
    private TextField passwordField;
    private Service srv;

    public void setService(Service srv){
        this.srv = srv;
    }

    public void login(ActionEvent event) throws IOException {
        String email = emailField.getText();
        String password = passwordField.getText();
        Employee employee = srv.authentificate_employee(email, password);
        if(employee == null){
            loginLabel.setText("Email or password incorrect");
            loginLabel.setTextFill(Paint.valueOf("red"));
        }
        else try {
            loginLabel.setText("Succesfully logging in!");
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/view/MainWindowView.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            MainWindowController mainController = fxmlLoader.getController();
            mainController.setService(srv, employee.getFirstName(), employee.getLastName());
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setTitle("Social-network-app");
            stage.setScene(scene);
            stage.show();
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
