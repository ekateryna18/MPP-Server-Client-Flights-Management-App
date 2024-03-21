package agency.services;

import agency.model.Ticket;

public interface IAgencyObserver {
    void update() throws MyException;
}
