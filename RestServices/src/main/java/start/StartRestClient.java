package start;

import agency.model.Flight;
import agency.services.rest.ServiceException;
import org.springframework.web.client.RestClientException;
import rest.client.FlightsClient;

import java.time.LocalDateTime;

public class StartRestClient {
    private final static FlightsClient flightsClient=new FlightsClient();
    public static void main(String[] args) {
        Flight flightT = new Flight(20,"test89", LocalDateTime.now(), "test45",45);
        try{
            //save
            show(()-> System.out.println(flightsClient.create(flightT)));
            //get all
            show(()->{
                Flight[] res=flightsClient.getAll();
                for(Flight f:res){
                    System.out.println(f.getID()+": "+f.getDestination()+ " "+f.getDatetime().toString()+ " "+f.getAirport()+ " "+f.getNoPlaces());
                }
            });
        }catch(RestClientException ex){
            System.out.println("Exception ... "+ex.getMessage());
        }
        Flight flightUpdt = new Flight(12,"testUpdtJava", LocalDateTime.now(), "javaupdate",45);
        //update
        show(() ->flightsClient.update(flightUpdt));
        //findone
        show(()-> System.out.println(flightsClient.findOne(9)));
        //delete
        show(() -> flightsClient.delete(17));
    }
    private static void show(Runnable task) {
        try {
            task.run();
        } catch (ServiceException e) {
            //  LOG.error("Service exception", e);
            System.out.println("Service exception"+ e);
        }
    }

}
