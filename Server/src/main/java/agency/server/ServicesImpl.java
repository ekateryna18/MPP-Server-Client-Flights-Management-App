package agency.server;

import agency.model.Employee;
import agency.model.Flight;
import agency.model.Ticket;
import agency.network.dto.EmployeeDTO;
import agency.repository.jdbc.EmployeeDBRepo;
import agency.repository.jdbc.FlightDBRepo;
import agency.repository.jdbc.TicketDBRepo;
import agency.services.IAgencyObserver;
import agency.services.IAgencyServices;
import agency.services.MyException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServicesImpl implements IAgencyServices {
    private EmployeeDBRepo employeeDBRepo;
    private TicketDBRepo ticketDBRepo;
    private FlightDBRepo flightDBRepo;
    private Map<String, IAgencyObserver> loggedClients;


    public ServicesImpl(EmployeeDBRepo employeeDBRepo, TicketDBRepo ticketDBRepo, FlightDBRepo flightDBRepo) {
        this.employeeDBRepo = employeeDBRepo;
        this.ticketDBRepo = ticketDBRepo;
        this.flightDBRepo = flightDBRepo;
        loggedClients=new ConcurrentHashMap<>();
    }

    public synchronized Employee login(Employee employee, IAgencyObserver client) throws MyException{
        Employee empl = employeeDBRepo.authentificate(employee.getEmail(), employee.getPassword());
        if(empl!=null){
            if(loggedClients.get(empl.getEmail())!=null)
                throw new MyException("Employee already logged in");
            loggedClients.put(empl.getEmail(), client);
        }else{
            throw new MyException("Authentification failed!");
        }
        return empl;
    }
    public synchronized void logout(Employee employee, IAgencyObserver client) throws MyException {
        IAgencyObserver localClient=loggedClients.remove(employee.getEmail());
        if (localClient==null)
            throw new MyException("Employee "+employee.getEmail()+" is not logged in.");
        else{
            System.out.println("Employee "+employee.getEmail()+" logged out!");
        }
    }

    public synchronized Flight[] getAvailableFlights() throws MyException {
        List<Flight> flights = new ArrayList<>();
        for(Flight f: flightDBRepo.findAvailableFlights())
        {
            flights.add(f);
        }
        System.out.println("Getting available flights from repo DB...");
        System.out.println("Size: " + flights.size());
        return flights.toArray(new Flight[flights.size()]);
    }

    public synchronized Flight[] getSearchedFlights(String destination, LocalDateTime dateTime) throws MyException {
        List<Flight> flights = new ArrayList<>();
        for(Flight f: flightDBRepo.findFlightsByDestinationDate(destination, dateTime))
        {
            flights.add(f);
        }
        System.out.println("Getting available flights from repo DB...");
        System.out.println("Size: " + flights.size());
        return flights.toArray(new Flight[flights.size()]);
    }
    private final int defaultThreadsNo=5;
    private void notifyClients() throws MyException{
        System.out.println("notify");
        ExecutorService executor = Executors.newFixedThreadPool(defaultThreadsNo);
        for(String email : loggedClients.keySet()){
            IAgencyObserver client=loggedClients.get(email);
            if(client!=null)
                executor.execute(()->{
                    try{
                        System.out.println("Notifying : " + email + "...");
                        client.update();
                    }catch (MyException e){
                        System.err.println("Error notifying " + e);
                    }
                });
        }
        executor.shutdown();
    }
    public synchronized void buyTicket(Ticket ticket) throws MyException {
        ticketDBRepo.save(ticket);
        Flight flight =  ticket.getFlight();
        Integer places = flight.getNoPlaces() -ticket.getNoPlaces();
        flight.setNoPlaces(places);
        flightDBRepo.update(flight);
        notifyClients();
    }

}
