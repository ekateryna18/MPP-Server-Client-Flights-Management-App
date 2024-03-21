package agency.network.protobuffprotocol;

import agency.model.Employee;
import agency.model.Flight;
import agency.model.Ticket;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ProtoUtils {
    public static AgencyProtobufs.AgencyRequest createLoginRequest(Employee employee) {
        AgencyProtobufs.Employee employeeDTO = AgencyProtobufs.Employee.newBuilder().setEmail(employee.getEmail()).setPassword(employee.getPassword()).build();
        AgencyProtobufs.AgencyRequest request = AgencyProtobufs.AgencyRequest.newBuilder().setType(AgencyProtobufs.AgencyRequest.Type.Login).setEmployee(employeeDTO).build();
        return request;
    }

    public static AgencyProtobufs.AgencyRequest createLogoutRequest(Employee employee) {
        AgencyProtobufs.Employee employeeDTO = AgencyProtobufs.Employee.newBuilder().setEmail(employee.getEmail()).build();
        AgencyProtobufs.AgencyRequest request = AgencyProtobufs.AgencyRequest.newBuilder().setType(AgencyProtobufs.AgencyRequest.Type.Logout).setEmployee(employeeDTO).build();
        return request;
    }

    public static AgencyProtobufs.AgencyRequest createGetFlightsRequest() {
        AgencyProtobufs.AgencyRequest request = AgencyProtobufs.AgencyRequest.newBuilder().setType(AgencyProtobufs.AgencyRequest.Type.Get_flights).build();
        return request;
    }

    public static AgencyProtobufs.AgencyRequest createFilterFlightsRequest(String destination, LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String datetime_str = dateTime.format(formatter);
        AgencyProtobufs.FilterData filterDataDTO = AgencyProtobufs.FilterData.newBuilder().setDestination(destination).setDatetime(datetime_str).build();
        AgencyProtobufs.AgencyRequest request = AgencyProtobufs.AgencyRequest.newBuilder().setType(AgencyProtobufs.AgencyRequest.Type.Filter_flights).setFilterDataArray(filterDataDTO).build();
        return request;
    }

    public static AgencyProtobufs.AgencyRequest createBuyTicketRequest(Ticket ticket) {
        String tourists = String.join(",", ticket.getTouristNames());
        Flight flight = ticket.getFlight();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String datetime_str = flight.getDatetime().format(formatter);
        AgencyProtobufs.Flight flightDTO = AgencyProtobufs.Flight.newBuilder().setFlightID(flight.getID()).setDestination(flight.getDestination()).setAirport(flight.getAirport()).setDatetime(datetime_str).setNoPlaces(flight.getNoPlaces()).build();
        AgencyProtobufs.Ticket ticketDTO = AgencyProtobufs.Ticket.newBuilder().setClientName(ticket.getClientName()).setTouristNames(tourists).setClientAddress(ticket.getClientAddress()).setNoPlaces(ticket.getNoPlaces()).setFlight(flightDTO).build();
        AgencyProtobufs.AgencyRequest request = AgencyProtobufs.AgencyRequest.newBuilder().setType(AgencyProtobufs.AgencyRequest.Type.Buy_ticket).setTicket(ticketDTO).build();
        return request;
    }
    public static String getError(AgencyProtobufs.AgencyResponse response)
    {
        String errorMsg= response.getError();
        return errorMsg;
    }

    public static Flight[] getFlights(AgencyProtobufs.AgencyResponse response)
    {
        Flight[] flights = new Flight[response.getFlightsCount()];
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        for(int i=0;i< response.getFlightsCount();i++)
        {
            AgencyProtobufs.Flight flightDTO=response.getFlights(i);
            String date_cut = flightDTO.getDatetime().substring(0,16);
            LocalDateTime dateTime = LocalDateTime.parse(flightDTO.getDatetime(), formatter);
            Flight flight = new Flight("","",0);
            flight.setID(flightDTO.getFlightID());
            flight.setAirport(flightDTO.getAirport());
            flight.setDatetime(dateTime);
            flight.setNoPlaces(flightDTO.getNoPlaces());
            flight.setDestination(flightDTO.getDestination());
            flights[i] =flight;
        }
        return flights;
    }
}

