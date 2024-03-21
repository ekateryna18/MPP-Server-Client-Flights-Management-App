package agency.client.gui;

import agency.model.Employee;
import agency.services.IAgencyServices;
import agency.services.MyException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class LoginController {
    @FXML
    private Label loginLabel;
    @FXML
    private TextField emailField;
    @FXML
    private TextField passwordField;
    private IAgencyServices srv;
    Parent mainWParent;
    private MainWindowController mainCtrl;

    public void setServer(IAgencyServices srv){
        this.srv = srv;
    }
    public void setParent(Parent p){mainWParent = p;}

    public void login(ActionEvent event) throws IOException {
        String email = emailField.getText();
        String password = passwordField.getText();
        try {
            Employee employee = srv.login(new Employee(email, password), mainCtrl);
            loginLabel.setText("Succesfully logging in!");
            Stage stage = new Stage();
            stage.setTitle("Client: "+email);
            stage.setScene(new Scene(mainWParent));
//            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
//                @Override
//                public void handle(WindowEvent wevent) {
//                    try{
//                        //CHANGE LOGOUT
//                        mainCtrl.logout(event);
//                        System.exit(0);
//                    }catch (IOException e){
//                        System.out.println(e);
//                    }
//                }
//            });
            stage.show();
            mainCtrl.setServer(srv, employee);
            ((Node)(event.getSource())).getScene().getWindow().hide();
//            FXMLLoader fxmlLoader = new FXMLLoader();
//            fxmlLoader.setLocation(getClass().getResource("/view/MainWindowView.fxml"));
//            Scene scene = new Scene(fxmlLoader.load());
//            MainWindowController mainController = fxmlLoader.getController();
//            mainController.setServer(srv, employee.getFirstName(), employee.getLastName());
//            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//            stage.show();

        } catch (MyException e) {
            loginLabel.setText("Email or password incorrect");
            loginLabel.setTextFill(Paint.valueOf("red"));
        }

    }
    public void setMainWindowController(MainWindowController mainWindowController){
        this.mainCtrl = mainWindowController;
    }
}
