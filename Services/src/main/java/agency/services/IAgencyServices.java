package agency.services;

import agency.model.Employee;
import agency.model.Flight;
import agency.model.Ticket;

import java.time.LocalDateTime;

public interface IAgencyServices {
    Employee login(Employee employee, IAgencyObserver client) throws MyException;
    void logout(Employee employee,IAgencyObserver client) throws MyException;
    Flight[] getAvailableFlights() throws MyException;
    Flight[] getSearchedFlights(String destination, LocalDateTime dateTime) throws MyException;
    void buyTicket(Ticket ticket) throws MyException;
}
