package agency.network.dto;
import agency.model.Employee;
import agency.model.Flight;
import agency.model.Ticket;

import java.time.LocalDateTime;
import java.util.List;

public class DTOUtils {
    public static Employee getFromDTO(EmployeeDTO empldto){
        String email = empldto.getEmail();
        String passw = empldto.getPassword();
        return new Employee(email, passw);
    }
    public static EmployeeDTO getDTO(Employee employee){
        String email = employee.getEmail();
        String passw = employee.getPassword();
        return new EmployeeDTO(email, passw);
    }
    public static Ticket getFromDTO(TicketDTO ticketDTO){
        List<String> tourist_names = ticketDTO.getTouristNames();
        String client_address = ticketDTO.getClientAddress();
        int no_places = ticketDTO.getNoPlaces();
        Flight flight = ticketDTO.getFlight();
        String client_name = ticketDTO.getClientName();
        return new Ticket(client_name, tourist_names, client_address,no_places,flight);
    }
    public static TicketDTO getDTO(Ticket ticket){
        List<String> tourist_names = ticket.getTouristNames();
        String client_address = ticket.getClientAddress();
        int no_places = ticket.getNoPlaces();
        Flight flight = ticket.getFlight();
        String client_name = ticket.getClientName();
        return new TicketDTO(client_name, tourist_names, client_address,no_places,flight);
    }
    public static Flight getFromDTO(FlightDTO flightDTO){
        int flight_id = flightDTO.getFlightID();
        String destination = flightDTO.getDestination();
        LocalDateTime dateTime = flightDTO.getDatetime();
        String airport = flightDTO.getAirport();
        int no_places = flightDTO.getNoPlaces();
        return new Flight(flight_id, destination,dateTime,airport,no_places);
    }
    public static FlightDTO getDTO(Flight flight){
        int flight_id = flight.getID();
        String destination = flight.getDestination();
        LocalDateTime dateTime = flight.getDatetime();
        String airport = flight.getAirport();
        int no_places = flight.getNoPlaces();
        return new FlightDTO(flight_id, destination,dateTime,airport,no_places);
    }
    public static Flight[] getFromDTO(FlightDTO[] DTOflights){
        Flight[] flights = new Flight[DTOflights.length];
        for(int i=0;i<DTOflights.length;i++){
            flights[i] = getFromDTO(DTOflights[i]);
        }
        return  flights;
    }
    public static FlightDTO[] getDTO(Flight[] flights){
        FlightDTO[] DTOflights = new FlightDTO[flights.length];
        for(int i=0;i<flights.length;i++){
            DTOflights[i] = getDTO(flights[i]);
        }
        return  DTOflights;
    }
}
