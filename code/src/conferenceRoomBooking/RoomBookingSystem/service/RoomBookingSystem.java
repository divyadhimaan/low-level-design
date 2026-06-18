package RoomBookingSystem.service;

import RoomBookingSystem.model.Recurrence;
import RoomBookingSystem.orchestrator.RoomBookingOrchestrator;
import RoomBookingSystem.repository.EmployeeInventory;
import RoomBookingSystem.repository.RoomInventory;

import java.util.List;

public class RoomBookingSystem {

    private static RoomBookingSystem systemInstance;
    private final RoomBookingOrchestrator orchestrator;

    private RoomBookingSystem() {
        this.orchestrator = new RoomBookingOrchestrator(new RoomInventory(), new EmployeeInventory());
    }

    public static synchronized RoomBookingSystem getInstance()
    {
        if(systemInstance==null)
            systemInstance = new RoomBookingSystem();
        return systemInstance;
    }

    public RoomBookingOrchestrator getOrchestrator() {
        return orchestrator;
    }

    public void registerRoom(String roomName, String roomType, List<Integer> availableSlots) {
        orchestrator.registerRoom(roomName, roomType, availableSlots);
    }


    public void showAllRegisteredRooms(){
        orchestrator.showAllRegisteredRooms();
    }

    public void registerEmployee(String employeeName, String department){
        orchestrator.registerEmployee(employeeName, department);
    }
    public void showAllRegisteredEmployees(){
        orchestrator.showAllRegisteredEmployees();
    }

    public void bookRoom(String employeeName, int totalAttendees, int startSlot, int durationMinutes){
        System.out.println("------------------- Booking Initiated ----------------------- ");
        orchestrator.bookRoom(employeeName, totalAttendees, startSlot, durationMinutes);

        System.out.println("------------------- Booking Ended ----------------------- ");
    }

    public void bookRoomRecurring(String employeeName, int totalAttendees, int startSlot, int durationMinutes, int numberOfWeeks, int dayOfWeek, String frequencyType) {

        System.out.println("------------------- Booking Initiated ----------------------- ");
        orchestrator.bookRoom(employeeName, totalAttendees, new Recurrence(numberOfWeeks, startSlot, durationMinutes, dayOfWeek, frequencyType));
        System.out.println("------------------- Booking Ended ----------------------- ");

    }

    public void viewSchedule(){
        orchestrator.viewRoomSchedule();
        orchestrator.viewEmployeeBookings();
    }
}
