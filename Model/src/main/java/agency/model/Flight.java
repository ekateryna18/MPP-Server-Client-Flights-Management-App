package agency.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class Flight extends Entity<Integer> {
    private String destination;
    //@JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime datetime;
    private String airport;
    private Integer noPlaces;
    public Flight(){
    }

    public Flight(String destination, LocalDateTime date, String airport, Integer noPlaces) {
        this.destination = destination;
        this.datetime = date;
        this.airport = airport;
        this.noPlaces = noPlaces;
    }
    public Flight(String destination, String airport, Integer noPlaces) {
        this.destination = destination;
        this.airport = airport;
        this.noPlaces = noPlaces;
    }
    public Flight(Integer id,String destination, LocalDateTime date, String airport, Integer noPlaces) {
        this.setID(id);
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
        return "Id: " + this.getID().toString() +", Destination= " + this.getDestination()+ ", Datetime= " + this.getDatetime() + ", Airport= " + this.getAirport() + ", places= "+ this.getNoPlaces().toString();
    }
}
