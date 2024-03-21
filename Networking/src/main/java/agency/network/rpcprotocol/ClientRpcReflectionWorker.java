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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ClientRpcReflectionWorker implements Runnable, IAgencyObserver {
    private IAgencyServices server;
    private Socket connection;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private volatile boolean connected;

    public ClientRpcReflectionWorker(IAgencyServices server, Socket connection) {
        System.out.println("Constructor ClientRpcReflectionWorker");
        this.server = server;
        this.connection = connection;
        try{
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
            connected = true;
        }catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }

    @Override
    public void run() {
        while(connected){
            try {
                System.out.println("Handling request...");
                Object request=input.readObject();
                Response response=handleRequest((Request)request);
                if (response!=null){
                    sendResponse(response);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            input.close();
            output.close();
            connection.close();
        } catch (IOException e) {
            System.out.println("Error "+e);
        }
    }
    private static Response okResponse=new Response.Builder().type(ResponseType.OK).build();
    //  private static Response errorResponse=new Response.Builder().type(ResponseType.ERROR).build();
    private Response handleRequest(Request request){
        Response response=null;
        String handlerName="handle"+(request).type();
        System.out.println("HandlerName "+handlerName);
        try {
            Method method=this.getClass().getDeclaredMethod(handlerName, Request.class);
            response=(Response)method.invoke(this,request);
            System.out.println("Method "+handlerName+ " invoked");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return response;
    }
    private Response handleLOGIN(Request request){
        System.out.println("Login request ..."+request.type());
        EmployeeDTO employeeDTO = (EmployeeDTO)request.data();
        Employee employee = DTOUtils.getFromDTO(employeeDTO);
        try{
            server.login(employee, this);
            return okResponse;
        }catch (MyException ex){
            connected = false;
            return new Response.Builder().type(ResponseType.ERROR).data(ex.getMessage()).build();
        }
    }
    private Response handleGET_FLIGHTS(Request request){
        System.out.println("Getting available flights...");
        try{
            Flight[] flights=server.getAvailableFlights();
            FlightDTO[] DTOflights=DTOUtils.getDTO(flights);
            return new Response.Builder().type(ResponseType.GET_FLIGHTS).data(DTOflights).build();
        }catch (MyException ex){
            return new Response.Builder().type(ResponseType.ERROR).data(ex.getMessage()).build();
        }
    }

    private Response handleLOGOUT(Request request){
        System.out.println("Logout request...");
        EmployeeDTO employeeDTO = (EmployeeDTO)request.data();
        Employee employee = DTOUtils.getFromDTO(employeeDTO);
        try{
            server.logout(employee, this);
            connected=false;
            return okResponse;

        }catch (MyException ex){
            return new Response.Builder().type(ResponseType.ERROR).data(ex.getMessage()).build();
        }
    }

    private Response handleFILTER_FLIGHTS(Request request){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String[] result_string = new String[2];
        result_string = (String[])request.data();
        String destination = result_string[0];
        LocalDateTime dateTime = LocalDateTime.parse(result_string[1], formatter);
        try{
            Flight[] flights = server.getSearchedFlights(destination, dateTime);
            FlightDTO[] DTOflights=DTOUtils.getDTO(flights);
            return new Response.Builder().type(ResponseType.GET_FLIGHTS).data(DTOflights).build();
        }catch (MyException ex){
            return new Response.Builder().type(ResponseType.ERROR).data(ex.getMessage()).build();
        }
    }
    private Response handleBUY_TICKET(Request request){
        System.out.println("Buying ticket...");
        TicketDTO ticketDTO = (TicketDTO)request.data();
        Ticket ticket = DTOUtils.getFromDTO(ticketDTO);
        try{
            server.buyTicket(ticket);
            return new Response.Builder().type(ResponseType.OK).data(null).build();
        }catch (MyException ex){
            return new Response.Builder().type(ResponseType.ERROR).data(ex.getMessage()).build();
        }
    }
    @Override
    public void update() throws MyException {
        Response resp = new Response.Builder().type(ResponseType.UPDATE_FLIGHTS).data(null).build();
        System.out.println("Update on flights list");
        try{
            sendResponse(resp);
            System.out.println("answer");
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void sendResponse(Response response) throws IOException{
        System.out.println("sending response "+response);
        output.writeObject(response);
        output.flush();
    }

}
