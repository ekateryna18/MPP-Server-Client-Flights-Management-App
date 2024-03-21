package agency.network.protobuffprotocol;

import agency.model.Employee;
import agency.model.Flight;
import agency.model.Ticket;
import agency.network.dto.DTOUtils;
import agency.network.dto.EmployeeDTO;
import agency.network.dto.FlightDTO;
import agency.network.dto.TicketDTO;
import agency.network.rpcprotocol.*;
import agency.services.IAgencyObserver;
import agency.services.IAgencyServices;
import agency.services.MyException;
import javafx.application.Platform;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ProtoProxy implements IAgencyServices{
    private String host;
    private int port;
    private IAgencyObserver client;
    private InputStream input;
    private OutputStream output;
    private Socket connection;

    private BlockingQueue<AgencyProtobufs.AgencyResponse> qresponses;

    private volatile boolean finished;

    public ProtoProxy(String host, int port) {
        this.host = host;
        this.port = port;
        this.qresponses = new LinkedBlockingQueue<AgencyProtobufs.AgencyResponse>();
    }

    @Override
    public Employee login(Employee employee, IAgencyObserver client) throws MyException {
        System.out.println("Proxy - logging in...");
        initializeConnection();
        sendRequest(ProtoUtils.createLoginRequest(employee));
        AgencyProtobufs.AgencyResponse response = readResponse();
        if(response.getType() == AgencyProtobufs.AgencyResponse.Type.Ok){
            this.client = client;
        }
        if(response.getType() == AgencyProtobufs.AgencyResponse.Type.Error){
            String err =ProtoUtils.getError(response);
            closeConnection();
            throw new MyException(err);
        }
        return employee;
    }

    @Override
    public void logout(Employee employee, IAgencyObserver client) throws MyException {
        sendRequest(ProtoUtils.createLogoutRequest(employee));
        AgencyProtobufs.AgencyResponse response = readResponse();
        closeConnection();
        if(response.getType()== AgencyProtobufs.AgencyResponse.Type.Error){
            String err=ProtoUtils.getError(response);
            closeConnection();
            throw new MyException(err);
        }
    }

    @Override
    public Flight[] getAvailableFlights() throws MyException {
        sendRequest(ProtoUtils.createGetFlightsRequest());
        AgencyProtobufs.AgencyResponse response = readResponse();
        if(response.getType()== AgencyProtobufs.AgencyResponse.Type.Error){
            String err=ProtoUtils.getError(response);
            closeConnection();
            throw new MyException(err);
        }
        Flight[] flights = ProtoUtils.getFlights(response);
        return flights;
    }

    @Override
    public Flight[] getSearchedFlights(String destination, LocalDateTime dateTime) throws MyException {
        sendRequest(ProtoUtils.createFilterFlightsRequest(destination, dateTime));
        AgencyProtobufs.AgencyResponse response = readResponse();
        if(response.getType()== AgencyProtobufs.AgencyResponse.Type.Error){
            String err=ProtoUtils.getError(response);
            closeConnection();
            throw new MyException(err);
        }
        Flight[] flights = ProtoUtils.getFlights(response);
        return flights;
    }

    @Override
    public void buyTicket(Ticket ticket) throws MyException {
        sendRequest(ProtoUtils.createBuyTicketRequest(ticket));
        AgencyProtobufs.AgencyResponse response = readResponse();
        if(response.getType()== AgencyProtobufs.AgencyResponse.Type.Error){
            String err=ProtoUtils.getError(response);
            closeConnection();
            throw new MyException(err);
        }
    }
    private AgencyProtobufs.AgencyResponse readResponse() throws MyException{
        AgencyProtobufs.AgencyResponse response = null;
        try{
            response = qresponses.take();
        }catch(InterruptedException ex)
        {
            ex.printStackTrace();
        }
        return response;
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
    private void sendRequest(AgencyProtobufs.AgencyRequest request)throws MyException{
        try {
            System.out.println("Sending request ..."+request);
            //request.writeTo(output);
            request.writeDelimitedTo(output);
            output.flush();
            System.out.println("Request sent.");
        } catch (IOException e) {
            throw new MyException("Error sending object "+e);
        }

    }
    private void initializeConnection() throws MyException{
        try {
            connection=new Socket(host,port);
            output=connection.getOutputStream();
            //output.flush();
            input=connection.getInputStream();     //new ObjectInputStream(connection.getInputStream());
            finished=false;
            startReader();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void startReader(){
        Thread tw=new Thread(new ReaderThread());
        tw.start();
    }
    private class ReaderThread implements Runnable{
        public void run() {
            while(!finished){
                try {
                    AgencyProtobufs.AgencyResponse response=AgencyProtobufs.AgencyResponse.parseDelimitedFrom(input);
                    System.out.println("response received "+response);

                    if (isUpdateResponse(response.getType())){
                        handleUpdate(response);
                    }else{
                        try {
                            qresponses.put(response);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Reading error "+e);
                }
            }
        }
    }
    private void handleUpdate(AgencyProtobufs.AgencyResponse updateResponse){
        if(updateResponse.getType() == AgencyProtobufs.AgencyResponse.Type.Update_flights){
            System.out.println("Updating flights");
            try{
                System.out.println("trying updateee");
                client.update();
            }catch (MyException e){
                e.printStackTrace();
            }
            System.out.println("Out of update proxi");
        }
    }
    private boolean isUpdateResponse(AgencyProtobufs.AgencyResponse.Type type){
        return type== AgencyProtobufs.AgencyResponse.Type.Update_flights;
    }

}
