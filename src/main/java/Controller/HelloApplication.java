package Controller;

import Repository.EmployeeRepo.EmployeeDBRepo;
import Repository.FlightRepo.FlightDBRepo;
import Repository.TicketRepo.TicketDBRepo;
import Service.Service;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/view/LoginView.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        LoginController loginController = fxmlLoader.getController();
        Properties props=new Properties();
        try {
            props.load(new FileReader("bd.config.properties"));
        } catch (IOException e) {
            System.out.println("Cannot find bd.config "+e);
        }
        EmployeeDBRepo employeeDBRepo = new EmployeeDBRepo(props);
        FlightDBRepo flightDBRepo = new FlightDBRepo(props);
        TicketDBRepo ticketDBRepo = new TicketDBRepo(props);
        Service service = new Service(employeeDBRepo, flightDBRepo, ticketDBRepo);
        loginController.setService(service);
        stage.setTitle("Social-network-app");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}