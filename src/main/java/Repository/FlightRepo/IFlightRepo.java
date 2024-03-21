package Repository.FlightRepo;

import Model.Employee;
import Model.Flight;
import Repository.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface IFlightRepo extends Repository<Integer, Flight> {
    public Iterable<Flight> findAvailableFlights();
    public Iterable<Flight> findFlightsByDestinationDate(String destination, LocalDateTime datetime);
}
