package rest.client;

import agency.model.Flight;
import agency.services.rest.ServiceException;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.Callable;

public class FlightsClient {
    public static final String URL = "http://localhost:8080/agency/flights";

    private RestTemplate restTemplate = new RestTemplate();
    private <T> T execute(Callable<T> callable) {
        try {
            return callable.call();
        } catch (ResourceAccessException | HttpClientErrorException e) { // server down, resource exception
            throw new ServiceException(e);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }
    public Flight[] getAll(){
        return execute(() -> restTemplate.getForObject(URL, Flight[].class));
    }
    public Flight findOne(Integer id){
        return execute(() -> restTemplate.getForObject(String.format("%s/%s", URL, id.toString()),Flight.class));
    }
    public Flight create(Flight flight){
        return execute(() -> restTemplate.postForObject(URL, flight, Flight.class));
    }
    public void update(Flight flight){
        execute(() -> {
            restTemplate.put(String.format("%s/%s", URL, flight.getID().toString()), flight);
            return null;
        });
    }
    public void delete(Integer id){
        execute(() -> {
            restTemplate.delete(String.format("%s/%s", URL, id.toString()));
            return null;
        });
    }

}
