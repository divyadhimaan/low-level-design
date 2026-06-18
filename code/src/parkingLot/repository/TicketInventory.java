package repository;

import model.ParkingLotTicket;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class TicketInventory {

    private Map<UUID, ParkingLotTicket> tickets = new ConcurrentHashMap<>();

    public synchronized void addTicket(ParkingLotTicket ticket) {
        tickets.put(ticket.getTicketId(), ticket);
    }

    public synchronized ParkingLotTicket getTicketById(UUID ticketId) {
        return tickets.get(ticketId);
    }

}
