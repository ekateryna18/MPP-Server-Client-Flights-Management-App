package Repository.FlightRepo;

import Model.Flight;
import Repository.JdbcUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class FlightDBRepo implements IFlightRepo{
    private final JdbcUtils dbUtils;
    private static final Logger logger = LogManager.getLogger();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public FlightDBRepo(Properties props){
        logger.info("Initializing FlightDBRepo with properties: {} ", props);
        dbUtils = new JdbcUtils(props);
    }

    @Override
    public Iterable<Flight> findAvailableFlights() {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<Flight> flights = new ArrayList<>();
        try(PreparedStatement preStmt = con.prepareStatement("SELECT * FROM flight WHERE no_places > 0"))
        {
            try(ResultSet result = preStmt.executeQuery())
            {
                while(result.next())
                {
                    int id = result.getInt("id_flight");
                    String destination = result.getString("destination");
                    LocalDateTime dateTime = LocalDateTime.parse(result.getString("datetime"), formatter);
                    String airport = result.getString("airport");
                    int places = result.getInt("no_places");
                    Flight flight = new Flight(destination,dateTime,airport,places);
                    flight.setId(id);
                    flights.add(flight);
                }
            }
        }catch (SQLException ex)
        {
            logger.error(ex);
            System.err.println("Error DB " + ex);
        }
        logger.traceExit(flights);
        return flights;
    }

    @Override
    public Iterable<Flight> findFlightsByDestinationDate(String destination, LocalDateTime datetime) {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<Flight> flights = new ArrayList<>();
        try(PreparedStatement preStmt = con.prepareStatement("SELECT * FROM flight WHERE destination = ? AND datetime>= ?"))
        {
            preStmt.setString(1,destination);
            preStmt.setString(2,datetime.format(formatter));

            try(ResultSet result = preStmt.executeQuery())
            {
                while(result.next())
                {
                    int id = result.getInt("id_flight");
                    String dest= result.getString("destination");
                    LocalDateTime dateTime = LocalDateTime.parse(result.getString("datetime"), formatter);
                    String airport = result.getString("airport");
                    int places = result.getInt("no_places");
                    Flight flight = new Flight(dest,dateTime,airport,places);
                    flight.setId(id);
                    flights.add(flight);
                }
            }
        }catch (SQLException ex)
        {
            logger.error(ex);
            System.err.println("Error DB " + ex);
        }
        logger.traceExit(flights);
        return flights;
    }

    @Override
    public Flight findOne(Integer integer) {
        logger.traceEntry("finding flight with id {} ",integer);
        Connection con=dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("select * from flight where id=?")){
            preStmt.setInt(1,integer);
            try(ResultSet result=preStmt.executeQuery()) {
                if (result.next()) {
                    int id = result.getInt("id_flight");
                    String destination = result.getString("destination");
                    LocalDateTime dateTime = LocalDateTime.parse(result.getString("datetime"), formatter);
                    String airport = result.getString("airport");
                    Integer no_places = result.getInt("no_places");
                    Flight flight = new Flight(id,destination,dateTime,airport,no_places);
                    logger.traceExit(flight);
                    return flight;
                }
            }
        }catch (SQLException ex){
            logger.error(ex);
            System.out.println("Error DB "+ex);
        }
        logger.traceExit("No task found with id {}", integer);

        return null;
    }

    @Override
    public Iterable<Flight> findAll() {
        return null;
    }

    @Override
    public void save(Flight entity) {
        logger.traceEntry("saving flight {} ", entity);
        Connection con = dbUtils.getConnection();
        try(PreparedStatement ps = con.prepareStatement("INSERT INTO flight(destination, datetime, airport, no_places) values(?,?,?,?)")){
            ps.setString(1, entity.getDestination());
            ps.setString(2, entity.getDatetime().format(formatter));
            ps.setString(3, entity.getAirport());
            ps.setInt(4, entity.getNoPlaces());
            int result = ps.executeUpdate();
            logger.trace("Saved {} instances", result);
        }catch (SQLException ex){
            logger.error(ex);
            System.err.println("Error DB " + ex);
        }
        logger.traceExit();
    }

    @Override
    public void delete(Integer integer) {

    }

    @Override
    public void update(Flight entity) {
        logger.traceEntry("updating flight {} ", entity);
        Connection con = dbUtils.getConnection();
        try(PreparedStatement ps = con.prepareStatement("UPDATE flight SET no_places = ? WHERE id_flight=?")) {
            ps.setInt(1, entity.getNoPlaces());
            ps.setInt(2, entity.getId());
            int result = ps.executeUpdate();
            logger.trace("Updated {} instances", result);
        }catch(SQLException ex){
            logger.error(ex);
            System.err.println("Error DB "+ex);
        }
        logger.traceExit();
    }
}