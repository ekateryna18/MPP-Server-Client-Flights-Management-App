package Model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Flight extends Entity<Integer>{
    private String destination;
    private LocalDateTime datetime;
    private String airport;
    private Integer noPlaces;

    public Flight(String destination, LocalDateTime date, String airport, Integer noPlaces) {
        this.destination = destination;
        this.datetime = date;
        this.airport = airport;
        this.noPlaces = noPlaces;
    }
    public Flight(Integer id,String destination, LocalDateTime date, String airport, Integer noPlaces) {
        this.setId(id);
        this.destination = destination;
        this.datetime = date;
        this.airport = airport;
        this.noPlaces = noPlaces;
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

    @Override
    public String toString() {
        return "Id: " + this.getId().toString() +", Destination= " + this.getDestination()+ ", Datetime= " + this.getDatetime() + ", Airport= " + this.getAirport() + ", places= "+ this.getNoPlaces().toString();
    }
}
