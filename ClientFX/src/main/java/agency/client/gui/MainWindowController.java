package agency.client.gui;

import agency.model.Employee;
import agency.model.Flight;
import agency.model.Ticket;
import agency.services.IAgencyObserver;
import agency.services.IAgencyServices;
import agency.services.MyException;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class MainWindowController implements Initializable, IAgencyObserver {
    ObservableList<Flight> modelFlight = FXCollections.observableArrayList();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private final DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @FXML
    private Label emailLabel;
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
    private IAgencyServices srv;
    private Employee employee;
    public void setServer(IAgencyServices srv, Employee employee){
        this.srv = srv;
        this.employee = employee;
        emailLabel.setText(employee.getEmail());
        try{
            modelFlight.setAll(srv.getAvailableFlights());
        }catch (MyException ex){
            ex.printStackTrace();
        }
    }
    public MainWindowController(){
        System.out.println("Constructor MainWindowController");
    }
    public MainWindowController(IAgencyServices s){
        this.srv = s;
        System.out.println("Constructor MainWindowController cu server");
    }
    @FXML
    public void initialize(URL location, ResourceBundle resources){
        destinationCol.setCellValueFactory(new PropertyValueFactory<Flight,String>("destination"));
        datetimeCol.setCellValueFactory(new PropertyValueFactory<Flight, String>("datetime"));
        airportCol.setCellValueFactory(new PropertyValueFactory<Flight,String>("airport"));
        placesCol.setCellValueFactory(new PropertyValueFactory<Flight,String>("noPlaces"));
        tableV.setItems(modelFlight);
        //setAvailableFlights();
        destinationCol1.setCellValueFactory(new PropertyValueFactory<Flight,String>("destination"));
        datetimeCol1.setCellValueFactory(new PropertyValueFactory<Flight, String>("datetime"));
        airportCol1.setCellValueFactory(new PropertyValueFactory<Flight,String>("airport"));
        placesCol1.setCellValueFactory(new PropertyValueFactory<Flight,String>("noPlaces"));
    }
//    private void setAvailableFlights(){
//        try{
//            Flight[] flights = srv.getAvailableFlights();
//            tableV.getItems().clear();
//            modelFlight.setAll(flights);
//            tableV.setItems(modelFlight);
//            for(Flight f: flights){
//                tableV.getItems().add(f);
//            }
//        }catch (MyException ex){
//            ex.printStackTrace();
//        }
//    }
    public void handleSearchFlights(){
        String destination = searchField.getText();
        LocalDate dateTime = datePicker.getValue();
        LocalDateTime finaldate = dateTime.atTime(0,0);
//        System.out.println(finaldate);
        try{
            modelFlight.setAll(srv.getSearchedFlights(destination, finaldate));
            tableV1.setItems(modelFlight);
        }catch (MyException ex){
            System.out.println("Filter error" + ex);
        }
    }

    public void handleBuyButton(){
        Flight selected = (Flight) tableV1.getSelectionModel().getSelectedItem();
        if(selected != null) {
            String client_name = clientField.getText();
            String tourist_names_str = touristField.getText();
            String address = addressField.getText();
            Integer places = Integer.parseInt(placesField.getText());
            List<String> tourist_names = new ArrayList<String>(Arrays.asList(tourist_names_str.split(",")));
            Ticket ticket = new Ticket(client_name,tourist_names,address,places,selected);
            try{
                srv.buyTicket(ticket);
//                modelFlight.setAll(srv.getAvailableFlights());
//                tableV.setItems(modelFlight);
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Buy ticket", "Ticket succesfully bought!");
            }catch (MyException ex){
                System.err.println("Erorr buying ticket "+ex);
            }

        }else MessageAlert.showErrorMessage(null, "Nu ati selectat nici un student!");

    }

    @Override
    public void update() throws MyException {
        Platform.runLater(()->{
            System.out.println("Update gui list");
            try {
                modelFlight.setAll(srv.getAvailableFlights());
                tableV.setItems(modelFlight);
            }catch (Exception ex){
                System.err.println(ex.getMessage());
            }
        });

    }

    @FXML public void logout(ActionEvent event) throws IOException {
        priv_logout();
        ((Node)(event.getSource())).getScene().getWindow().hide();
//        FXMLLoader fxmlLoader = new FXMLLoader();
//        fxmlLoader.setLocation(getClass().getResource("/view/LoginView.fxml"));
//        Scene scene = new Scene(fxmlLoader.load());
//        LoginController loginController = fxmlLoader.getController();
//        loginController.setServer(srv);
//        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
//        stage.setTitle("Social-network-app");
//        stage.setScene(scene);
//        stage.show();
    }
    private void priv_logout(){
        try{
            srv.logout(employee, this);
        }catch (MyException e){
            System.out.println("Logout error" + e);
        }
    }

}
