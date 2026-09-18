import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class Locker {
    private static Locker instance;
    public List<Compartment> compartments;
    public Map<String, AccessToken> accessTokenMap;

    private Locker(List<Compartment> compartmentList)
    {
        this.compartments = compartmentList;
        accessTokenMap = new HashMap<>();
    }

    public static synchronized Locker getInstance(List<Compartment> compartmentList) {
        if(instance == null){
            instance = new Locker(compartmentList);
        }
        return instance;
    }

    public String depositPackage(String size){
        Compartment compartment = getAvailableCompartment(size);
        if(compartment == null) {
            throw new IllegalStateException("No available compartment of size: " + size);
        }

        compartment.open();
        compartment.markOccupied();
        AccessToken accessToken = generateAccessToken(compartment);
        accessTokenMap.put(accessToken.getCode().toString(), accessToken);

        return accessToken.getCode();
    }

    public void retrievePackage(String tokenCode){
        if(tokenCode == null || tokenCode.isEmpty()){
            throw new RuntimeException("Token code cannot be null or empty");
        }
        AccessToken accessToken = accessTokenMap.get(tokenCode);
        if(accessToken == null) {
            throw new RuntimeException("Invalid access token");
        }

        if(accessToken.isExpired()){
            throw new RuntimeException("Access token has expired");
        }

        Compartment compartment = accessToken.getCompartment();
        compartment.open();
        clearDeposit(accessToken);

    }

    public void openExpiredCompartment(){
        for(AccessToken accessToken: accessTokenMap.values()){
            if(accessToken.isExpired()){
                Compartment compartment = accessToken.getCompartment();
                compartment.open();
                clearDeposit(accessToken);
            }
        }
    }


    private Compartment getAvailableCompartment(String size){
        List<String> sizesInOrder = List.of("SMALL", "MEDIUM", "LARGE");
        int requestedSizeIndex = sizesInOrder.indexOf(size);

        for(int  i = requestedSizeIndex; i < sizesInOrder.size(); i++){
            String currentSize = sizesInOrder.get(i);
            for(Compartment compartment : compartments){
                if(compartment.getCompartmentSize().equals(currentSize) && !compartment.isAvailable()){
                    return compartment;
                }
            }
        }

        //without fallback, just return null if no compartment is found
//        for(Compartment compartment : compartments){
//            if(compartment.getCompartmentSize().equals(size) && !compartment.isAvailable()){
//                return compartment;
//            }
//        }
        return null;
    }

    private AccessToken generateAccessToken(Compartment compartment){
        String code = String.format("%06d", new Random().nextInt(1_000_000));
        Instant expirationTime = Instant.now().plus(7, ChronoUnit.DAYS); // 7 Days
        return new AccessToken(code, compartment, expirationTime);
    }

    private void clearDeposit(AccessToken accessToken){
        Compartment compartment = accessToken.getCompartment();
        compartment.markFree();
        accessTokenMap.remove(accessToken.getCode());
    }



}
