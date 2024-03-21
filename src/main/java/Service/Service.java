package Service;

import Model.Employee;
import Model.Flight;
import Model.Ticket;
import Repository.EmployeeRepo.EmployeeDBRepo;
import Repository.EmployeeRepo.IEmployeeRepo;
import Repository.FlightRepo.FlightDBRepo;
import Repository.FlightRepo.IFlightRepo;
import Repository.Repository;
import Repository.TicketRepo.ITicketRepo;
import Repository.TicketRepo.TicketDBRepo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Service {
    private IEmployeeRepo employeeRepo;
    private IFlightRepo flightRepo;
    private ITicketRepo ticketRepo;

    public Service(IEmployeeRepo employeeRepo, IFlightRepo flightRepo, ITicketRepo ticketRepo) {
        this.employeeRepo = employeeRepo;
        this.flightRepo = flightRepo;
        this.ticketRepo = ticketRepo;
    }

    public Employee authentificate_employee(String email, String password)
    {
        return employeeRepo.authentificate(email, password);
    }

    public List<Flight> findFlightsByDestinationDate(String destination, LocalDateTime datetime){
        List<Flight> flights = new ArrayList<>();
        for(Flight f: flightRepo.findFlightsByDestinationDate(destination, datetime))
        {
            flights.add(f);
        }
        return flights;
    }

    public List<Flight> getAllAvailableFlights(){
        List<Flight> flights = new ArrayList<>();
        for(Flight f: flightRepo.findAvailableFlights())
        {
            flights.add(f);
        }
        return flights;
    }

    public void buyTicket(String client_name, String tourist_names_str, String client_address, Integer no_places, Flight flight){
        List<String> tourist_names = new ArrayList<String>(Arrays.asList(tourist_names_str.split(",")));
        Ticket ticket = new Ticket(client_name, tourist_names, client_address, no_places, flight);
        ticketRepo.save(ticket);
        Integer places = flight.getNoPlaces() -1;
        flight.setNoPlaces(places);
        flightRepo.update(flight);
    }
}
