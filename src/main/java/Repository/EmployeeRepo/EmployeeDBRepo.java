package Repository.EmployeeRepo;

import Model.Employee;
import Repository.JdbcUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EmployeeDBRepo implements IEmployeeRepo {
    private final JdbcUtils dbUtils;
    private static final Logger logger = LogManager.getLogger();

    public EmployeeDBRepo(Properties props){
        logger.info("Initializing EmployeeDBRepo with properties: {} ", props);
        dbUtils = new JdbcUtils(props);
    }

    @Override
    public Employee authentificate(String email, String password) {
        logger.traceEntry("authentificating employee email {} ", email);
        Connection con = dbUtils.getConnection();
        try(PreparedStatement ps = con.prepareStatement("SELECT * FROM Employee WHERE email=?")){
            ps.setString(1, email);
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()) {
                    Integer id = rs.getInt("id_employee");
                    String first_name = rs.getString("first_name");
                    String last_name = rs.getString("last_name");
                    String passw = rs.getString("password");
                    Employee employee = new Employee(first_name, last_name,email, passw);
                    employee.setId(id);
                    if(passw.equals(password)){
                        logger.info("Authentification successfull!");
                        return employee;
                    }
                    else{
                        logger.info("Authentification failed!");
                        return null;
                    }
                }
            }
        }catch (SQLException ex){
            logger.error(ex);
            System.err.println("Error DB " + ex);
        }
        return null;
    }

    @Override
    public Employee findOne(Integer integer) {
        return null;
    }

    @Override
    public Iterable<Employee> findAll() {
        return null;
    }

    @Override
    public void save(Employee entity) {
        logger.traceEntry("saving employee {} ", entity);
        Connection con = dbUtils.getConnection();
        try(PreparedStatement ps = con.prepareStatement("INSERT INTO Employee(first_name, last_name, email, password) values(?,?,?,?)")){
            ps.setString(1, entity.getFirstName());
            ps.setString(2, entity.getLastName());
            ps.setString(3, entity.getEmail());
            ps.setString(4, entity.getPassword());
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
    public void update(Employee entity) {

    }
}