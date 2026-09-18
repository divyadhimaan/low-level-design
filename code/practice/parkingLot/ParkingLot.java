package parkingLot;

import lombok.Getter;
import parkingLot.inventory.TicketInventory;
import parkingLot.model.ParkingLevel;
import parkingLot.service.ParkingService;
import parkingLot.service.PaymentService;
import parkingLot.strategy.PaymentStrategy;
import parkingLot.strategy.SpotAllocationStrategy;

import java.util.List;

@Getter
public class ParkingLot {
    private final String parkingLotId;
    private final List<ParkingLevel> levels;
    private final PaymentStrategy paymentStrategy;
    private final SpotAllocationStrategy spotAllocationStrategy;
    private final TicketInventory ticketInventory;
    private final PaymentService paymentService;
    private final ParkingService parkingService;

    public ParkingLot(String parkingLotId,
                      List<ParkingLevel> levels,
                      SpotAllocationStrategy spotAllocationStrategy,
                      PaymentStrategy paymentStrategy) {
        this.parkingLotId = parkingLotId;
        this.levels = levels;
        this.spotAllocationStrategy = spotAllocationStrategy;
        this.paymentStrategy = paymentStrategy;
        this.ticketInventory = new TicketInventory();
        this.paymentService = new PaymentService();
        this.parkingService = new ParkingService();
    }
}
