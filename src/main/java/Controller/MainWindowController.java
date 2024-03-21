package Controller;

import Model.Flight;
import Model.Ticket;
import Repository.EmployeeRepo.EmployeeDBRepo;
import Repository.FlightRepo.FlightDBRepo;
import Repository.TicketRepo.TicketDBRepo;
import Service.Service;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class MainWindowController {
    ObservableList<Flight> modelFlight = FXCollections.observableArrayList();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private final DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private String lastnameMain;
    private String firstnameMain;
    @FXML
    private Label firstnameLabel;
    @FXML
    private Label lastnameLabel;
    @FXML
    private DatePicker datePicker;
    @FXML
    private TextField searchField;
    @FXML
    private TextField placesField;
    @FXML
    private TextField clientField;
    @FXML
    private TextField touristField;
    @FXML
    private TextField addressField;
    @FXML
    private TableView<Flight> tableV;
    @FXML
    private TableView<Flight> tableV1;
    @FXML
    private TableColumn<Flight,String> destinationCol;
    @FXML
    private TableColumn<Flight, String> datetimeCol;
    @FXML
    private TableColumn<Flight,String> airportCol;
    @FXML
    private TableColumn<Flight, String> placesCol;
    @FXML
    private TableColumn<Flight,String> destinationCol1;
    @FXML
    private TableColumn<Flight, String> datetimeCol1;
    @FXML
    private TableColumn<Flight,String> airportCol1;
    @FXML
    private TableColumn<Flight, String> placesCol1;
    private Service srv;
    public void setService(Service srv, String first, String last){
        this.srv = srv;
        this.lastnameMain = last;
        this.firstnameMain = first;
        firstnameLabel.setText(this.firstnameMain);
        lastnameLabel.setText(this.lastnameMain);
        modelFlight.setAll(srv.getAllAvailableFlights());
    }

    @FXML
    public void initialize(){
        destinationCol.setCellValueFactory(new PropertyValueFactory<Flight,String>("destination"));
        datetimeCol.setCellValueFactory(new PropertyValueFactory<Flight, String>("datetime"));
        airportCol.setCellValueFactory(new PropertyValueFactory<Flight,String>("airport"));
        placesCol.setCellValueFactory(new PropertyValueFactory<Flight,String>("noPlaces"));
        tableV.setItems(modelFlight);
        destinationCol1.setCellValueFactory(new PropertyValueFactory<Flight,String>("destination"));
        datetimeCol1.setCellValueFactory(new PropertyValueFactory<Flight, String>("datetime"));
        airportCol1.setCellValueFactory(new PropertyValueFactory<Flight,String>("airport"));
        placesCol1.setCellValueFactory(new PropertyValueFactory<Flight,String>("noPlaces"));
    }

    public void handleSearchFlights(){
        String destination = searchField.getText();
        LocalDate dateTime = datePicker.getValue();
        LocalDateTime finaldate = dateTime.atTime(0,0);
        System.out.println(finaldate);

        modelFlight.setAll(srv.findFlightsByDestinationDate(destination, finaldate));
        tableV1.setItems(modelFlight);
    }

    public void handleBuyButton(){
        Flight selected = (Flight) tableV1.getSelectionModel().getSelectedItem();
        if(selected != null) {
            String client_name = clientField.getText();
            String tourist_names_str = touristField.getText();
            String address = addressField.getText();
            Integer places = Integer.parseInt(placesField.getText());
            srv.buyTicket(client_name, tourist_names_str, address, places, selected);
            modelFlight.setAll(srv.getAllAvailableFlights());
            tableV.setItems(modelFlight);
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Buy ticket", "Ticket succesfully bought!");
        }else MessageAlert.showErrorMessage(null, "Nu ati selectat nici un student!");

    }

    @FXML public void logout(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/view/LoginView.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        LoginController loginController = fxmlLoader.getController();
        loginController.setService(srv);
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setTitle("Social-network-app");
        stage.setScene(scene);
        stage.show();
    }
}
