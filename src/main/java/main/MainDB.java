package main;

import Model.Employee;
import Model.Flight;
import Model.Ticket;
import Repository.EmployeeRepo.EmployeeDBRepo;
import Repository.FlightRepo.FlightDBRepo;
import Repository.TicketRepo.TicketDBRepo;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

public class MainDB {
    public static void main(String[] args){
        Properties props=new Properties();
        try {
            props.load(new FileReader("bd.config.properties"));
        } catch (IOException e) {
            System.out.println("Cannot find bd.config "+e);
        }
        System.out.println("Adding an employee");
        EmployeeDBRepo employeeDBRepo = new EmployeeDBRepo(props);

        employeeDBRepo.save(new Employee("Anisia", "Plea", "anisiaplea@yahoo.com", "aniani1234"));

        System.out.println("Authentificate employee  ... anisiaplea@yahoo.com");
        System.out.println(employeeDBRepo.authentificate("anisiaplea@yahoo.com", "aniani1234"));

        System.out.println("Adding a flight");
        FlightDBRepo flightDBRepo = new FlightDBRepo(props);
        LocalDateTime loc= LocalDateTime.of(2023, 9, 11,11,30);

        flightDBRepo.save(new Flight("Nice",loc, "Bucuresti",190));

        System.out.println("Updating a flight");
        Flight flight =new Flight(6,"Nice",loc, "Bucuresti",100);

        flightDBRepo.update(flight);

        System.out.println("All available flights");
        for(Flight fl : flightDBRepo.findAvailableFlights())
        {
            System.out.println(fl);
        }
        System.out.println("All available flights from a destination after a specific date");
        LocalDateTime loc2= LocalDateTime.of(2023, 6, 13,11,30);
        for(Flight fl : flightDBRepo.findFlightsByDestinationDate("Nice",loc2))
        {
            System.out.println(fl);
        }

        System.out.println("Adding a ticket");
        TicketDBRepo ticketDBRepo = new TicketDBRepo(props);
        List<String> tourists = new ArrayList<>();
        tourists.add("Maria Ioana");
        tourists.add("Petre Marian");
        tourists.add("Ana Maria");

        ticketDBRepo.save(new Ticket("Maria Ioana", tourists, "Dorobantilor 93",2,flight));

    }
}