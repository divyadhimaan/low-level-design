package RoomBookingSystem.orchestrator;

import RoomBookingSystem.model.*;
import RoomBookingSystem.repository.EmployeeInventory;
import RoomBookingSystem.repository.RoomInventory;

import java.util.*;

public class RoomBookingOrchestrator {

    private final RoomInventory roomInventory;
    private final EmployeeInventory employeeInventory;

    //Dependency Injection for better testability
    public RoomBookingOrchestrator(RoomInventory roomInventory, EmployeeInventory employeeInventory) {
        if(roomInventory == null || employeeInventory == null){
            throw new IllegalArgumentException("RoomInventory and EmployeeInventory cannot be null.");
        }
        this.roomInventory = roomInventory;
        this.employeeInventory = employeeInventory;
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

        Room room = new Room(roomName, roomType, new HashSet<>(availableSlots));
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

    private boolean validateMeetingDetails(int totalAttendees, int startSlot, int durationMinutes){
        if(durationMinutes <= 0 || durationMinutes>600){
            System.out.println("Invalid duration. Please provide a duration between 1 and 600 minutes.");
            return false;
        }
        if(startSlot < 0 || startSlot > 24){
            System.out.println("Invalid start slot. Please provide a start slot between 0 and 24");
            return false;
        }
        if(totalAttendees > 30 || totalAttendees <= 0){
            System.out.println("Invalid number of attendees. Please provide a number between 1 and 30.");
            return false;
        }
        return true;
    }

    private boolean validateBookingInputs(String employeeName, int totalAttendees, int startSlot, int durationMinutes) {
        if (!validateMeetingDetails(totalAttendees, startSlot, durationMinutes)) return false;
        if (!validateEmployee(employeeName)) {
            System.out.println("Booking failed: Employee \"" + employeeName + "\" does not exist.");
            return false;
        }

        int slotsRequired = (int) Math.ceil(durationMinutes / 60.0);
        if (startSlot + slotsRequired - 1 > 10) {
            System.out.println("Booking failed: Requested duration exceeds available slots in the day.");
            return false;
        }

        return true;
    }

    public void bookRoom(String employeeName, int totalAttendees, int startSlot, int durationMinutes){

        if(!validateBookingInputs(employeeName, totalAttendees, startSlot, durationMinutes)){
            return;
        }

        List<Integer> requiredSlots = calculateRequiredSlots(startSlot, durationMinutes);

        Booking booking = null;

        // Find best room without holding inventory lock - getAllRooms() returns a safe snapshot
        Room bestFitRoom = findBestRoom(totalAttendees, requiredSlots);

        // Only lock the specific room while booking to avoid nested synchronization deadlock
        if (bestFitRoom != null) {
            synchronized (bestFitRoom) {
                if (bestFitRoom.canBook(requiredSlots)) { // double-check availability
                    booking = createBooking(bestFitRoom, 0, requiredSlots, employeeName);
                }
            }
        }

        if (booking != null) {
            printBookingConfirmation(booking);
        } else {
            System.out.println("Booking failed: No suitable rooms available or slots already booked.");
        }
    }


    /** Creates a booking and returns the Booking object if successful */
    private Booking createBooking(Room room, int day, List<Integer> requiredSlots, String employeeName) {
        Booking booking = new Booking(room, requiredSlots, employeeName, day);
        boolean success = room.bookSlots(day, requiredSlots, booking);
        if(success){
            Employee emp = employeeInventory.getEmployeeByName(employeeName);
            if(emp != null) emp.addBooking(booking);
            return booking;
        }
        return null;
    }

    private void printBookingConfirmation(Booking booking) {
        System.out.println("Booking Successful!");
        System.out.println("Room: " + booking.getRoom().getRoomName() +
                "\nBooked Room Type: " + booking.getRoom().getRoomType() +
                "\nBooked by: " + booking.getEmployeeName() +
                "\nBooked Slots: " + booking.getBookedSlots() +
                "\nBooking ID: " + booking.getBookingId());
    }

    public void printRecurringBookingConfirmation(List<Booking> bookings, Recurrence recurrence) {
        if (bookings == null || bookings.isEmpty()) {
            System.out.println("No recurring bookings found.");
            return;
        }

        System.out.println("=== Recurring Booking Details ===");
        System.out.println("Frequency: " + recurrence.getFrequencyType());
        System.out.println("Start Slot: " + recurrence.getStartSlot());
        System.out.println("Duration (minutes): " + recurrence.getDurationInMinutes());
        System.out.println("Number of Weeks: " + recurrence.getNumberOfWeeks());
        System.out.println("Day of Week: " + recurrence.getDayOfWeek());
        System.out.println("Total Occurrences: " + bookings.size());

        System.out.println("\nOccurrences:");
        System.out.printf("%-10s %-15s %-10s %-20s\n", "Occurrence", "Room Name", "Day Index", "Booked Slots");
        System.out.println("----------------------------------------------------------");

        int occurrence = 1;
        for (Booking booking : bookings) {
            System.out.printf("%-10d %-15s %-10d %-20s\n",
                    occurrence,
                    booking.getRoom().getRoomName(),
                    booking.getDay(),
                    booking.getBookedSlots());
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

        if(!validateBookingInputs(employeeName, totalAttendees, recurrence.getStartSlot(), recurrence.getDurationInMinutes()) ||
                !validateRecurrence(recurrence)){
            return bookings;
        }

        List<Integer> requiredSlots = calculateRequiredSlots(recurrence.getStartSlot(), recurrence.getDurationInMinutes());


        Room bestFitRoom = findBestRoom(totalAttendees, recurrence);
        if (bestFitRoom == null) {
            System.out.println("Booking failed: Booking failed: No suitable rooms available for the recurring schedule.");
            return bookings;
        }

        int currentDay = recurrence.getDayOfWeek();

        int incrementDays = switch (recurrence.getFrequencyType()){
            case DAILY -> 1;
            case WEEKLY -> 7;
            case BIWEEKLY -> 14;
            case MONTHLY -> 30;
            default -> throw new IllegalArgumentException("Unsupported frequency type");
        };

        int totalOccurrences = recurrence.getNumberOfWeeks();
        if (recurrence.getFrequencyType() == FrequencyType.DAILY) {
            totalOccurrences *= 7; // book every day in each week
        }

        // Proceed with booking - synchronized to ensure atomicity of all occurrences
        synchronized (bestFitRoom) {
            for(int i=0; i<totalOccurrences; i++){
                if(!bestFitRoom.canBookForDay(currentDay, requiredSlots)){
                    System.out.println("Booking failed: Room not available on day " + currentDay);
                    // rollback previously booked slots
                    for(Booking b : bookings){
                        bestFitRoom.cancelBooking(b.getDay(), b.getBookedSlots());
                        Employee emp = employeeInventory.getEmployeeByName(employeeName);
                        if(emp != null) emp.removeBooking(b);
                    }
                    bookings.clear();
                    return bookings;
                }

                Booking booking = createBooking(bestFitRoom, currentDay, requiredSlots, employeeName);
                bookings.add(booking);

                currentDay += incrementDays;
            }
        }

        System.out.println("Recurring booking successful for " + bookings.size() + " occurrences (" + recurrence.getFrequencyType() + ")");

        printRecurringBookingConfirmation(bookings, recurrence);
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

    //helper methods
    private Room findBestRoom(int totalAttendees, List<Integer> requiredSlots) {
        Collection<Room> rooms = roomInventory.getAllRooms().values();
        Room bestRoom = null;

        RoomType primaryType = (totalAttendees <= 10) ? RoomType.SMALL : RoomType.LARGE;
        RoomType fallbackType = (primaryType == RoomType.SMALL) ? RoomType.LARGE : RoomType.SMALL;

        // only consider small rooms
        for (Room room : roomInventory.getAllRoomsByType(primaryType).values()) {
            if (room.canBook(requiredSlots)) {
                bestRoom = room;
                break; // found a suitable small room
            }
        }

        //if not found in small rooms, consider large rooms
        if(bestRoom == null){
            for (Room room : roomInventory.getAllRoomsByType(fallbackType).values()) {
                if (room.canBook(requiredSlots)) {
                    bestRoom = room;
                    break; // found a suitable large room
                }
            }
        }

        return bestRoom;
    }


    private Room findBestRoom(int totalAttendees, Recurrence recurrence) {
        Room bestRoom = null;

        List<Integer> requiredSlots = calculateRequiredSlots(recurrence.getStartSlot(), recurrence.getDurationInMinutes());


        RoomType primaryType = (totalAttendees <= 10) ? RoomType.SMALL : RoomType.LARGE;
        RoomType fallbackType = (primaryType == RoomType.SMALL) ? RoomType.LARGE : RoomType.SMALL;

        for (Room room : roomInventory.getAllRoomsByType(primaryType).values()) {
            if (room.canBookRecurring(recurrence, requiredSlots)) {
                bestRoom = room;
                break;
            }
        }

        if (bestRoom == null) {
            for (Room room : roomInventory.getAllRoomsByType(fallbackType).values()) {
                if (room.canBookRecurring(recurrence, requiredSlots)) {
                    bestRoom = room;
                    break;
                }
            }
        }

        return bestRoom;
    }


    // helper to calculate slots
    private List<Integer> calculateRequiredSlots(int startSlot, int durationMinutes){
        int slotsRequired = (int) Math.ceil(durationMinutes / 60.0);
        List<Integer> slots = new ArrayList<>();
        for(int i=0; i<slotsRequired; i++){
            slots.add(startSlot + i);
        }
        return slots;
    }

    private boolean validateEmployee(String employeeName){
        return employeeInventory.checkEmployeeExists(employeeName);
    }


}

