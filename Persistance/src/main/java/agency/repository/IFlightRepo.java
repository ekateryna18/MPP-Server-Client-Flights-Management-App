package agency.repository;

import agency.model.Flight;

import java.time.LocalDateTime;

public interface IFlightRepo extends Repository<Integer, Flight> {
    public Iterable<Flight> findAvailableFlights();
    public Iterable<Flight> findFlightsByDestinationDate(String destination, LocalDateTime datetime);
    public Flight saveAndReturn(Flight entity);
}
