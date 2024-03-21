package agency.services.rest;

import agency.model.Flight;
import agency.repository.IFlightRepo;
import agency.repository.RepositoryException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/agency/flights")
public class FlightController {
    private static final String template = "Hello, %s!";
    @Autowired
    private IFlightRepo flightRepository;

    @RequestMapping("/greeting")
    public  String greeting(@RequestParam(value="name", defaultValue="World") String name) {
        return String.format(template, name);
    }

    @RequestMapping(method = RequestMethod.GET)
    public Flight[] getAll(){
        System.out.println("Get all flights...");
        List<Flight> flights = new ArrayList<>();
        for(Flight f: flightRepository.findAvailableFlights())
        {
            flights.add(f);
        }
        return flights.toArray(new Flight[flights.size()]);
    }

    @RequestMapping(value= "/{id}", method=RequestMethod.GET)
    public ResponseEntity<?> getById(@PathVariable Integer id){
        System.out.println("Get by id "+id.toString());
        Flight flight = flightRepository.findOne(id);
        if(flight == null)
            return new ResponseEntity<String>("Flight not found", HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<Flight>(flight, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public Flight create(@RequestBody Flight flight)
    {
        flight = flightRepository.saveAndReturn(flight);
        return flight;
    }

    @RequestMapping(value="/{id}", method= RequestMethod.PUT)
    public Flight update(@PathVariable Integer id,@RequestBody Flight flight){
        System.out.println("Updating flight...");
        if(id.equals(flight.getID())){
            flightRepository.update(flight);
        }
        return null;
    }

    @RequestMapping(value="/{id}", method=RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable Integer id)
    {
        System.out.println("Deleting flight..."+id.toString());
        try{
            flightRepository.delete(id);
            return new ResponseEntity<Flight>(HttpStatus.OK);
        }catch (RepositoryException ex){
            System.out.println("Ctrl delete flight exception");
            return new ResponseEntity<String>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @ExceptionHandler(RepositoryException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String userError(RepositoryException e) {
        return e.getMessage();
    }

}
