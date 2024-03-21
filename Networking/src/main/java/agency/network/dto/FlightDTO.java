package agency.network.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

public class FlightDTO implements Serializable {
    private int FlightID;
    private String destination;
    private LocalDateTime datetime;
    private String airport;
    private Integer noPlaces;

    public FlightDTO(int flightID, String destination, LocalDateTime datetime, String airport, Integer noPlaces) {
        FlightID = flightID;
        this.destination = destination;
        this.datetime = datetime;
        this.airport = airport;
        this.noPlaces = noPlaces;
    }

    public int getFlightID() {
        return FlightID;
    }

    public void setFlightID(int flightID) {
        FlightID = flightID;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public LocalDateTime getDatetime() {
        return datetime;
    }

    public void setDatetime(LocalDateTime datetime) {
        this.datetime = datetime;
    }

    public String getAirport() {
        return airport;
    }

    public void setAirport(String airport) {
        this.airport = airport;
    }

    public Integer getNoPlaces() {
        return noPlaces;
    }

    public void setNoPlaces(Integer noPlaces) {
        this.noPlaces = noPlaces;
    }
}
