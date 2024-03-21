import agency.network.utils.AbstractServer;
import agency.network.utils.RpcConcurrentServer;
import agency.network.utils.ServerException;
import agency.repository.jdbc.EmployeeDBRepo;
import agency.repository.jdbc.FlightDBRepo;
import agency.repository.jdbc.TicketDBRepo;
import agency.server.ServicesImpl;
import agency.services.IAgencyServices;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.io.IOException;
import java.util.Properties;

public class StartRpcServer {
    private static int defaultPort=55555;
    static SessionFactory sessionFactory;
    public static void main(String[] args){
        Properties serverProps=new Properties();
        try{
            serverProps.load(StartRpcServer.class.getResourceAsStream("/server.properties"));
            System.out.println("Server properties set.");
            serverProps.list(System.out);
        }catch(IOException e){
            System.err.println("Cannot find server.properties" + e);
            return;
        }
        initialize();
        EmployeeDBRepo employeeDBRepo = new EmployeeDBRepo(sessionFactory);
        TicketDBRepo ticketDBRepo = new TicketDBRepo(serverProps);
        FlightDBRepo flightDBRepo = new FlightDBRepo(serverProps);
        IAgencyServices serverImpl = new ServicesImpl(employeeDBRepo, ticketDBRepo, flightDBRepo);
        int serverPort=defaultPort;
        try{
            serverPort = Integer.parseInt(serverProps.getProperty("agency.server.port"));
        }catch(NumberFormatException nfe){
            System.err.println("Wrong port nr "+ nfe.getMessage());
            System.err.println("Using default port "+defaultPort);
        }
        System.out.println("Starting server on port " +serverPort);
        AbstractServer server =new RpcConcurrentServer(serverPort, serverImpl);
        try{
            server.start();
        }catch (ServerException e){
            System.err.println("Error starting the server" + e.getMessage());
        }finally {
            try{
                server.stop();
                close();
            }catch (ServerException e){
                System.err.println("Error stopping server" + e.getMessage());
            }
        }
    }

    static void initialize() {
        // A SessionFactory is set up once for an application!
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        try {
            sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();
        }
        catch (Exception e) {
            System.err.println("Exception "+e);
            StandardServiceRegistryBuilder.destroy( registry );
        }
    }

    static void close(){
        if ( sessionFactory != null ) {
            sessionFactory.close();
        }

    }
}
