package agency.repository.jdbc;

import agency.model.Ticket;
import agency.repository.ITicketRepo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

public class TicketDBRepo implements ITicketRepo {
    private final JdbcUtils dbUtils;
    private static final Logger logger = LogManager.getLogger();

    public TicketDBRepo(Properties props){
        logger.info("Initializing TicketDBRepo with properties: {} ", props);
        dbUtils = new JdbcUtils(props);
    }

    @Override
    public Ticket findOne(Integer integer) {
        return null;
    }

    @Override
    public Iterable<Ticket> findAll() {
        return null;
    }

    @Override
    public void save(Ticket entity) {
        logger.traceEntry("saving ticket {} ", entity);
        Connection con = dbUtils.getConnection();
        try(PreparedStatement ps = con.prepareStatement("INSERT INTO ticket(tourist_names, client_address, no_places, id_flight, client_name) values(?,?,?,?,?)")){
            String tourists = String.join(",", entity.getTouristNames());
            ps.setString(1, tourists);
            ps.setString(2, entity.getClientAddress());
            ps.setInt(3, entity.getNoPlaces());
            ps.setInt(4, entity.getFlight().getID());
            ps.setString(5, entity.getClientName());
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
    public void update(Ticket entity) {

    }
}