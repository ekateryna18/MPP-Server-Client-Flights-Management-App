package agency.network.rpcprotocol;

import agency.model.Employee;
import agency.model.Flight;
import agency.model.Ticket;
import agency.network.dto.DTOUtils;
import agency.network.dto.EmployeeDTO;
import agency.network.dto.FlightDTO;
import agency.network.dto.TicketDTO;
import agency.services.IAgencyObserver;
import agency.services.IAgencyServices;
import agency.services.MyException;
import javafx.application.Platform;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ServicesRpcProxy implements IAgencyServices {
    private String host;
    private int port;
    private IAgencyObserver client;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Socket connection;

    private BlockingQueue<Response> qresponses;

    private volatile boolean finished;

    public ServicesRpcProxy(String host, int port) {
        this.host = host;
        this.port = port;
        qresponses = new LinkedBlockingQueue<Response>();
    }

    @Override
    public Employee login(Employee employee, IAgencyObserver client) throws MyException {
        System.out.println("Proxy - logging in...");
        initializeConnection();
        EmployeeDTO employeeDTO = DTOUtils.getDTO(employee);
        Request request = new Request.Builder().type(RequestType.LOGIN).data(employeeDTO).build();
        sendRequest(request);
        Response response = readResponse();
        if(response.type() == ResponseType.OK){
            this.client = client;
        }
        if(response.type() == ResponseType.ERROR){
            String err = response.data().toString();
            closeConnection();
            throw new MyException(err);
        }
        return employee;
    }

    @Override
    public void logout(Employee employee, IAgencyObserver client) throws MyException {
        EmployeeDTO employeeDTO = DTOUtils.getDTO(employee);
        Request req = new Request.Builder().type(RequestType.LOGOUT).data(employeeDTO).build();
        sendRequest(req);
        Response response = readResponse();
        closeConnection();
        if(response.type()==ResponseType.ERROR){
            String err=response.data().toString();
            closeConnection();
            throw new MyException(err);
        }
    }

    @Override
    public Flight[] getAvailableFlights() throws MyException {
        Request req = new Request.Builder().type(RequestType.GET_FLIGHTS).data(null).build();
        sendRequest(req);
        Response response = readResponse();
        if(response.type()==ResponseType.ERROR){
            String err=response.data().toString();
            closeConnection();
            throw new MyException(err);
        }
        FlightDTO[] flDTO = (FlightDTO[])response.data();
        Flight[] flights = DTOUtils.getFromDTO(flDTO);
        return flights;
    }

    @Override
    public Flight[] getSearchedFlights(String destination, LocalDateTime dateTime) throws MyException {
        String[] data_array = new String[2];
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String datetime_str = dateTime.format(formatter);
        data_array[0] = destination;
        data_array[1] = datetime_str;
        Request req = new Request.Builder().type(RequestType.FILTER_FLIGHTS).data(data_array).build();
        sendRequest(req);
        Response response = readResponse();
        if(response.type()==ResponseType.ERROR){
            String err=response.data().toString();
            closeConnection();
            throw new MyException(err);
        }
        FlightDTO[] flDTO = (FlightDTO[])response.data();
        Flight[] flights = DTOUtils.getFromDTO(flDTO);
        return flights;
    }

    @Override
    public void buyTicket(Ticket ticket) throws MyException {
        TicketDTO ticketDTO = DTOUtils.getDTO(ticket);
        Request req = new Request.Builder().type(RequestType.BUY_TICKET).data(ticketDTO).build();
        sendRequest(req);
        Response response = readResponse();
        if(response.type()==ResponseType.ERROR){
            String err=response.data().toString();
            closeConnection();
            throw new MyException(err);
        }
    }

    private void initializeConnection() throws MyException {
        try {
            connection=new Socket(host,port);
            output=new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input=new ObjectInputStream(connection.getInputStream());
            finished=false;
            startReader();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void closeConnection() {
        finished=true;
        try {
            input.close();
            output.close();
            connection.close();
            client=null;
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private void sendRequest(Request request)throws MyException {
        try {
            System.out.println("Sent request");
            output.writeObject(request);
            output.flush();
        } catch (IOException e) {
            throw new MyException("Error sending object "+e);
        }

    }
    private Response readResponse() throws MyException {
        System.out.println("Proxy - readResponse");
        Response response=null;
        try{

            response=qresponses.take();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }
    private void startReader(){
        System.out.println("Proxy=startReader");
        Thread tw=new Thread(new ReaderThread());
        tw.start();
    }

    private void handleUpdate(Response response){
        if(response.type() == ResponseType.UPDATE_FLIGHTS){
            System.out.println("Updating flights");
            Platform.runLater(()->{
                try{
                    System.out.println("trying updateee");
                    client.update();
                }catch (MyException e){
                    e.printStackTrace();
                }
            });
            System.out.println("Out of update proxi");
        }
    }
    private boolean isUpdate(Response response){
        return response.type()== ResponseType.UPDATE_FLIGHTS ;
    }
    private class ReaderThread implements Runnable{
        public void run() {
            while(!finished){
                try {
                    Object response=input.readObject();
                    System.out.println("response received "+response);
                    if (isUpdate((Response)response)){
                        Platform.runLater(() -> handleUpdate((Response)response));
                    }else{

                        try {
                            qresponses.put((Response)response);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                } catch (IOException e) {
                    System.out.println("Reading error "+e);
                } catch (ClassNotFoundException e) {
                    System.out.println("Reading error "+e);
                }
            }
        }
    }
}
