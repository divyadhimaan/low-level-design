package RoomBookingSystem.orchestrator;

import RoomBookingSystem.model.*;
import RoomBookingSystem.repository.EmployeeInventory;
import RoomBookingSystem.repository.RoomInventory;
import RoomBookingSystem.strategy.RoomStrategy;
import RoomBookingSystem.strategy.RecurringRoomStrategy;
import RoomBookingSystem.strategy.BestFitStrategy;
import RoomBookingSystem.strategy.BestFitRecurringStrategy;
import RoomBookingSystem.observer.BookingObserver;

import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class RoomBookingOrchestrator {

    private final RoomInventory roomInventory;
    private final EmployeeInventory employeeInventory;
    private RoomStrategy roomSelectionStrategy;
    private RecurringRoomStrategy recurringRoomSelectionStrategy;
    private final List<BookingObserver> bookingObservers;

    //Dependency Injection for better testability
    public RoomBookingOrchestrator(RoomInventory roomInventory, EmployeeInventory employeeInventory) {
        if(roomInventory == null || employeeInventory == null){
            throw new IllegalArgumentException("RoomInventory and EmployeeInventory cannot be null.");
        }
        this.roomInventory = roomInventory;
        this.employeeInventory = employeeInventory;
        this.roomSelectionStrategy = new BestFitStrategy(); // Default single booking strategy
        this.recurringRoomSelectionStrategy = new BestFitRecurringStrategy(); // Default recurring booking strategy
        this.bookingObservers = new CopyOnWriteArrayList<>(); // Thread-safe observer list
    }

    public void setRoomSelectionStrategy(RoomStrategy strategy) {
        if(strategy == null) {
            throw new IllegalArgumentException("RoomSelectionStrategy cannot be null.");
        }
        this.roomSelectionStrategy = strategy;
    }

    public void setRecurringRoomSelectionStrategy(RecurringRoomStrategy strategy) {
        if(strategy == null) {
            throw new IllegalArgumentException("RecurringRoomSelectionStrategy cannot be null.");
        }
        this.recurringRoomSelectionStrategy = strategy;
    }

    // Observer Pattern Methods
    public synchronized void subscribe(BookingObserver observer) {
        if(observer == null) {
            throw new IllegalArgumentException("Observer cannot be null.");
        }
        bookingObservers.add(observer);
    }

    public synchronized void unsubscribe(BookingObserver observer) {
        bookingObservers.remove(observer);
    }

    private void notifyBookingCreated(Booking booking) {
        for (BookingObserver observer : bookingObservers) {
            observer.onBookingCreated(booking);
        }
    }

    private void notifyRecurringBookingCreated(List<Booking> bookings) {
        for (BookingObserver observer : bookingObservers) {
            observer.onRecurringBookingCreated(bookings);
        }
    }

    private void notifyBookingCancelled(Booking booking) {
        for (BookingObserver observer : bookingObservers) {
            observer.onBookingCancelled(booking);
        }
    }

    private void notifyRecurringBookingCancelled(List<Booking> bookings) {
        for (BookingObserver observer : bookingObservers) {
            observer.onRecurringBookingCancelled(bookings);
        }
    }

    public void registerRoom(String roomName, String roomType, List<Integer> availableSlots){
        if(roomName == null || roomName.isEmpty() || availableSlots == null || availableSlots.isEmpty() ||
        roomType == null || roomType.isEmpty()){
            throw new IllegalArgumentException("Invalid room details provided.");
        }

        if(roomInventory.getRoom((roomName + "_" +  roomType))!= null){
            System.out.println("Room with name " + roomName + " and type " + roomType + " already exists.");
            return;
        }

        Room room = new Room(roomName, roomType);
        roomInventory.addRoom(room);
        System.out.println("Room: "+ room.getRoomName() + " | RoomType: " +room.getRoomType() +" | Status: Registered");
    }

    public void showAllRegisteredRooms(){
        Map<String, Room> allRooms = roomInventory.getAllRooms();

        System.out.println("------------------- Room Inventory ----------------------- ");
        for (Map.Entry<String, Room> entry : allRooms.entrySet()) {
            System.out.println("Room ID: " + entry.getKey() + ", Room Name: " + entry.getValue().getRoomName());
        }
        System.out.println("----------------------------------------------------------");
    }

    public void registerEmployee(String employeeName, String department){
        if(employeeName == null || employeeName.isEmpty() || department == null || department.isEmpty()){
            throw new IllegalArgumentException("Invalid employee details provided.");
        }

        if(employeeInventory.getEmployeeByName(employeeName) != null){
            System.out.println("Employee with name " + employeeName + " already exists.");
            return;
        }

        Employee employee = new Employee(employeeName, department);
        employeeInventory.addEmployee(employee);
        System.out.println("Employee: "+ employee.getEmployeeName() +" | Department name: "+department +" | Status: Registered");
    }
    public void showAllRegisteredEmployees(){
        Map<UUID, Employee> allEmployees = employeeInventory.getAllEmployees();
        System.out.println("------------------- Employee Inventory ----------------------- ");
        for (Map.Entry<UUID, Employee> entry : allEmployees.entrySet()) {
            System.out.println("Employee ID: " + entry.getKey() + ", Employee Name: " + entry.getValue().getEmployeeName());
        }
        System.out.println("---------------------------------------------------------------- ");
    }

    private static final LocalTime BUSINESS_START = LocalTime.of(9, 0);
    private static final LocalTime BUSINESS_END   = LocalTime.of(19, 0);

    private boolean validateMeetingDetails(int totalAttendees, LocalTime start, LocalTime end) {
        if (totalAttendees <= 0 || totalAttendees > 30) {
            System.out.println("Invalid number of attendees. Must be between 1 and 30.");
            return false;
        }
        if (start == null || end == null) {
            System.out.println("Start and end times must not be null.");
            return false;
        }
        if (!start.isBefore(end)) {
            System.out.println("Invalid time range: start (" + start + ") must be before end (" + end + ").");
            return false;
        }
        if (start.isBefore(BUSINESS_START)) {
            System.out.println("Invalid start time: " + start + " is before business hours (" + BUSINESS_START + ").");
            return false;
        }
        if (end.isAfter(BUSINESS_END)) {
            System.out.println("Invalid end time: " + end + " exceeds business hours (" + BUSINESS_END + ").");
            return false;
        }
        return true;
    }

    private boolean validateBookingInputs(String employeeName, int totalAttendees, LocalTime start, LocalTime end) {
        if (!validateMeetingDetails(totalAttendees, start, end)) return false;
        if (!validateEmployee(employeeName)) {
            System.out.println("Booking failed: Employee \"" + employeeName + "\" does not exist.");
            return false;
        }
        return true;
    }

    /**
     * Converts a slot integer to a LocalTime.
     * Slot 1 = 09:00, slot 2 = 10:00, ..., slot 10 = 18:00.
     */
    private LocalTime slotToTime(int slot) {
        return LocalTime.of(8 + slot, 0);
    }

    public void bookRoom(String employeeName, int totalAttendees, LocalTime start, LocalTime end){

        if (!validateBookingInputs(employeeName, totalAttendees, start, end)){
            return;
        }

        Booking booking = null;

        // Find best room without holding inventory lock - getAllRooms() returns a safe snapshot
        Room bestFitRoom = findBestRoom(totalAttendees, start, end);

        // Only lock the specific room while booking to avoid nested synchronization deadlock
        if (bestFitRoom != null) {
            synchronized (bestFitRoom) {
                if (bestFitRoom.canBook(start, end)) { // double-check availability
                    booking = createBooking(bestFitRoom, 0, start, end, employeeName);
                }
            }
        }

        if (booking != null) {
            printBookingConfirmation(booking);
            notifyBookingCreated(booking);
        } else {
            System.out.println("Booking failed: No suitable rooms available or slots already booked.");
        }
    }

    /** Creates a booking and returns the Booking object if successful. Caller holds the room lock. */
    private Booking createBooking(Room room, int day, LocalTime start, LocalTime end, String employeeName) {
        Booking booking = new Booking(room, start, end, employeeName, day);
        boolean success = room.bookSlots(day, start, end, booking);
        if (success) {
            Employee emp = employeeInventory.getEmployeeByName(employeeName);
            if (emp != null) emp.addBooking(booking);
            return booking;
        }
        return null;
    }

    private void printBookingConfirmation(Booking booking) {
        System.out.println("Booking Successful!");
        System.out.println("Room: " + booking.getRoom().getRoomName() +
                "\nBooked Room Type: " + booking.getRoom().getRoomType() +
                "\nBooked by: " + booking.getEmployeeName() +
                "\nTime: " + booking.getStartTime() + " – " + booking.getEndTime() +
                "\nBooking ID: " + booking.getBookingId());
    }

    public void printRecurringBookingConfirmation(List<Booking> bookings, Recurrence recurrence) {
        if (bookings == null || bookings.isEmpty()) {
            System.out.println("No recurring bookings found.");
            return;
        }

        System.out.println("=== Recurring Booking Details ===");
        System.out.println("Frequency: " + recurrence.getFrequencyType());
        System.out.println("Start (time): " + recurrence.getStart());
        System.out.println("End (time): " + recurrence.getEnd());
        System.out.println("Number of Weeks: " + recurrence.getNumberOfWeeks());
        System.out.println("Total Occurrences: " + bookings.size());

        System.out.println("\nOccurrences:");
        System.out.printf("%-10s %-15s %-10s %-10s %-10s%n",
                "Occurrence", "Room Name", "Day Index", "Start", "End");
        System.out.println("----------------------------------------------------------");

        int occurrence = 1;
        for (Booking booking : bookings) {
            System.out.printf("%-10d %-15s %-10d %-10s %-10s%n",
                    occurrence,
                    booking.getRoom().getRoomName(),
                    booking.getDay(),
                    booking.getStartTime(),
                    booking.getEndTime());
            occurrence++;
        }
        System.out.println("----------------------------------------------------------\n");
    }


    private boolean validateRecurrence(Recurrence recurrence){
        // Validate number of weeks
        if (recurrence.getNumberOfWeeks() <= 0) {
            System.out.println("Invalid number of weeks. Please provide a positive number.");
            return false;
        }

        if(recurrence.getDayOfWeek() <=0 || recurrence.getDayOfWeek() >6){
            System.out.println("Invalid day of week. Please provide a value between 1 (Monday) and 6 (Saturday).");
            return false;
        }

        // Validate frequency type
        try {
            FrequencyType.valueOf(recurrence.getFrequencyType().toString());
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid frequency type. Valid types are: DAILY, WEEKLY, MONTHLY.");
            return false;
        }
        return true;
    }

    public List<Booking> bookRoom(String employeeName, int totalAttendees, Recurrence recurrence){
        List<Booking> bookings = new ArrayList<>();

//        LocalTime start = slotToTime(recurrence.getStartSlot());
//        LocalTime end   = start.plusMinutes(recurrence.getDurationInMinutes());

        if (!validateBookingInputs(employeeName, totalAttendees, recurrence.getStart(), recurrence.getEnd()) ||
                !validateRecurrence(recurrence)){
            return bookings;
        }

        Room bestFitRoom = findBestRoom(totalAttendees, recurrence);
        if (bestFitRoom == null) {
            System.out.println("Booking failed: No suitable rooms available for the recurring schedule.");
            return bookings;
        }

        int currentDay = recurrence.getDayOfWeek();
        int incrementDays = switch (recurrence.getFrequencyType()) {
            case DAILY -> 1;
            case WEEKLY -> 7;
            case BIWEEKLY -> 14;
            case MONTHLY -> 30;
            default -> throw new IllegalArgumentException("Unsupported frequency type");
        };
        int totalOccurrences = recurrence.getFrequencyType() == FrequencyType.DAILY
                ? recurrence.getNumberOfWeeks() * 7
                : recurrence.getNumberOfWeeks();

        // Atomic loop: all occurrences under a single room lock
        synchronized (bestFitRoom) {
            for (int i = 0; i < totalOccurrences; i++) {
                if (!bestFitRoom.canBookForDay(currentDay, recurrence.getStart(), recurrence.getEnd())) {
                    System.out.println("Booking failed: Room not available on day " + currentDay);
                    // Rollback all previously booked occurrences
                    for (Booking b : bookings) {
                        bestFitRoom.cancelBooking(b.getDay(), b.getStartTime());
                        Employee emp = employeeInventory.getEmployeeByName(employeeName);
                        if (emp != null) emp.removeBooking(b);
                    }
                    bookings.clear();
                    return bookings;
                }

                Booking booking = createBooking(bestFitRoom, currentDay, recurrence.getStart(), recurrence.getEnd(), employeeName);
                bookings.add(booking);
                currentDay += incrementDays;
            }
        }

        System.out.println("Recurring booking successful for " + bookings.size() + " occurrences (" + recurrence.getFrequencyType() + ")");
        printRecurringBookingConfirmation(bookings, recurrence);
        notifyRecurringBookingCreated(bookings);
        return bookings;
    }



    public synchronized void viewRoomSchedule(){
        Map<String, Room> allRooms = roomInventory.getAllRooms();
        System.out.println("------------------- Room Schedules ----------------------- ");
        for (Map.Entry<String, Room> entry : allRooms.entrySet()) {
            Room room = entry.getValue();
            System.out.println("Room ID: " + entry.getKey() + ", Room Name: " + room.getRoomName());
            room.displayBookings();
            System.out.println("----------------------------------------------------------");
        }
    }

    public synchronized void viewEmployeeBookings(){
        Map<UUID, Employee> allEmployees = employeeInventory.getAllEmployees();
        System.out.println("------------------- Employee Bookings ----------------------- ");
        for (Map.Entry<UUID, Employee> entry : allEmployees.entrySet()) {
            Employee employee = entry.getValue();
            System.out.println("Employee ID: " + entry.getKey() + ", Employee Name: " + employee.getEmployeeName());
            employee.displayBookings();
            System.out.println("----------------------------------------------------------");
        }
    }

    // helper methods
    private Room findBestRoom(int totalAttendees, LocalTime start, LocalTime end) {
        List<Room> availableRooms = new ArrayList<>(roomInventory.getAllRooms().values());
        return roomSelectionStrategy.selectRoom(availableRooms, start, end);
    }

    private Room findBestRoom(int totalAttendees, Recurrence recurrence) {
        List<Room> availableRooms = new ArrayList<>(roomInventory.getAllRooms().values());
        return recurringRoomSelectionStrategy.selectRoom(availableRooms, recurrence);
    }

    private boolean validateEmployee(String employeeName){
        return employeeInventory.checkEmployeeExists(employeeName);
    }


}

