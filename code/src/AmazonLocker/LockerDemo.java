import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class LockerDemo {

    public static void main(String[] args) {
        List<Compartment> compartments = List.of(
                new Compartment("SMALL"),
                new Compartment("MEDIUM"),
                new Compartment("LARGE")
        );

        Locker locker = Locker.getInstance(compartments);

        printState("Initial", locker);

        // Scenario 1: Deposit a package
        System.out.println("=== Scenario 1: Deposit a MEDIUM package ===");
        String tokenCode = locker.depositPackage("MEDIUM");
        System.out.println("Token issued: " + tokenCode);
        printState("After deposit", locker);

        // Scenario 2: Retrieve with valid token
        System.out.println("\n=== Scenario 2: Retrieve with valid token ===");
        try {
            locker.retrievePackage(tokenCode);
            System.out.println("Package retrieved successfully.");
        } catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
        }
        printState("After retrieval", locker);

        // Scenario 3: Retrieve with expired token
        System.out.println("\n=== Scenario 3: Retrieve with expired token ===");
        String expiredToken = depositWithExpiredToken(locker);
        printState("After deposit (expired token)", locker);
        try {
            locker.retrievePackage(expiredToken);
            System.out.println("Package retrieved successfully.");
        } catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
        }
        printState("After attempted retrieval with expired token", locker);
    }

    private static String depositWithExpiredToken(Locker locker) {
        Compartment compartment = locker.compartments.stream()
                .filter(c -> !c.isOccupied())
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No free compartment"));

        compartment.open();
        compartment.markOccupied();

        // Manually create an already-expired token (expiration in the past)
        Instant expired = Instant.now().minus(1, ChronoUnit.DAYS);
        AccessToken expiredAccessToken = new AccessToken("EXPIRED", compartment, expired);
        locker.accessTokenMap.put("EXPIRED", expiredAccessToken);

        System.out.println("Token issued (pre-expired): EXPIRED");
        return "EXPIRED";
    }

    private static void printState(String label, Locker locker) {
        System.out.println("\n[State: " + label + "]");
        System.out.println("Compartments:");
        for (Compartment c : locker.compartments) {
            System.out.println("  " + c.getCompartmentSize() + " → occupied=" + c.isOccupied());
        }
        System.out.println("AccessTokenMap: " + locker.accessTokenMap.keySet());
        System.out.println();
    }
}
