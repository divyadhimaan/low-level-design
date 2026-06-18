package RoomBookingSystem.service;

import RoomBookingSystem.model.Recurrence;
import RoomBookingSystem.orchestrator.RoomBookingOrchestrator;
import RoomBookingSystem.repository.EmployeeInventory;
import RoomBookingSystem.repository.RoomInventory;
import RoomBookingSystem.observer.EmailObserver;
import RoomBookingSystem.observer.CalendarObserver;
import RoomBookingSystem.observer.SlackObserver;

import java.time.LocalTime;
import java.util.List;

public class RoomBookingSystem {

    private static RoomBookingSystem systemInstance;
    private final RoomBookingOrchestrator orchestrator;

    private RoomBookingSystem() {
        this.orchestrator = new RoomBookingOrchestrator(new RoomInventory(), new EmployeeInventory());
        initializeDefaultObservers();
    }

    /**
     * Initialize default observers for the system
     * Can be customized or replaced by calling getOrchestrator().subscribe() methods
     */
    private void initializeDefaultObservers() {
        orchestrator.subscribe(new EmailObserver());
        orchestrator.subscribe(new CalendarObserver());
        orchestrator.subscribe(new SlackObserver());
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

    public void bookRoom(String employeeName, int totalAttendees, LocalTime start, LocalTime end){
        System.out.println("------------------- Booking Initiated ----------------------- ");
        orchestrator.bookRoom(employeeName, totalAttendees, start, end);

        System.out.println("------------------- Booking Ended ----------------------- ");
    }

    public void bookRoomRecurring(String employeeName, int totalAttendees, LocalTime start, LocalTime end, int numberOfWeeks, int dayOfWeek, String frequencyType) {

        System.out.println("------------------- Booking Initiated ----------------------- ");
        orchestrator.bookRoom(employeeName, totalAttendees, new Recurrence(numberOfWeeks, start, end, dayOfWeek, frequencyType));
        System.out.println("------------------- Booking Ended ----------------------- ");

    }

    public void bookRoomRecurring(String employeeName, int totalAttendees, Recurrence recurrence) {
        System.out.println("------------------- Booking Initiated ----------------------- ");
        orchestrator.bookRoom(employeeName, totalAttendees, recurrence);
        System.out.println("------------------- Booking Ended ----------------------- ");
    }

    public void viewSchedule(){
        orchestrator.viewRoomSchedule();
        orchestrator.viewEmployeeBookings();
    }
}
