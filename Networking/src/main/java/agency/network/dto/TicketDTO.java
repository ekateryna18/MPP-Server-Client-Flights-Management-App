package agency.network.dto;

import agency.model.Flight;

import java.io.Serializable;
import java.util.List;

public class TicketDTO implements Serializable {
    private String clientName;
    private List<String> touristNames;
    private String clientAddress;
    private Integer noPlaces;
    private Flight flight;

    public TicketDTO(String clientName, List<String> touristNames, String clientAddress, Integer noPlaces, Flight flight) {
        this.clientName = clientName;
        this.touristNames = touristNames;
        this.clientAddress = clientAddress;
        this.noPlaces = noPlaces;
        this.flight = flight;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public List<String> getTouristNames() {
        return touristNames;
    }

    public void setTouristNames(List<String> touristNames) {
        this.touristNames = touristNames;
    }

    public String getClientAddress() {
        return clientAddress;
    }

    public void setClientAddress(String clientAddress) {
        this.clientAddress = clientAddress;
    }

    public Integer getNoPlaces() {
        return noPlaces;
    }

    public void setNoPlaces(Integer noPlaces) {
        this.noPlaces = noPlaces;
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }
}
