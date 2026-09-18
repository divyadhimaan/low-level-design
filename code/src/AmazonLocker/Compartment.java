import java.util.UUID;

public class Compartment {
    public final CompartmentSize compartmentSize;
    public CompartmentStatus status;
//    public boolean occupied;

    public Compartment(String size){
        this.compartmentSize = addSize(size);
        this.status = CompartmentStatus.AVAILABLE;
//        this.occupied = false;
    }

    private CompartmentSize addSize(String size){
        if(size.equals("SMALL")){
            return CompartmentSize.SMALL;
        } else if(size.equals("MEDIUM")){
            return CompartmentSize.MEDIUM;
        } else if(size.equals("LARGE")){
            return CompartmentSize.LARGE;
        } else {
            throw new IllegalArgumentException("Invalid compartment size: " + size);
        }
    }

    public String getCompartmentSize() {
        return compartmentSize.name();
    }

//    public Boolean isOccupied() {
//        return occupied;
//    }

    public Boolean isAvailable() {
        return status == CompartmentStatus.AVAILABLE;
    }

    public void markAvailable(){
        this.status = CompartmentStatus.AVAILABLE;
    }
    public void markOutOfService(){
        this.status = CompartmentStatus.OUT_OF_SERVICE;
    }

    public void markOccupied(){
        this.status = CompartmentStatus.OCCUPIED;
    }

    public void markFree(){
        this.status = CompartmentStatus.AVAILABLE;
    }

    public void open(){
        System.out.println("Compartment is open");
    }



}
