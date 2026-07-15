package BuildingH2O;

public class Demo {
    public static void main(String[] args) throws InterruptedException {
        BuildingH2O h2o = new BuildingH2O();
        String input = "OOHHHHHHOO"; // Example input
        Thread[] threads = new Thread[input.length()];

        for(int i=0;i<input.length();i++){
            char atom = input.charAt(i);
            if(atom == 'H'){
                threads[i] = new Thread(() -> {
                    try {
                        h2o.hydrogen(() -> System.out.print("H"));
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                });
            }else{
                threads[i] = new Thread(() -> {
                    try {
                        h2o.oxygen(() -> System.out.print("O"));
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                });
            }
        }

        for (Thread t : threads) t.start();
        for (Thread t : threads) t.join();

        System.out.println("\nDone.");
    }
}
