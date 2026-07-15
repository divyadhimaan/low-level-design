package ZeroOddEven;

public class Demo {
    public static void main(String[] args) throws InterruptedException {
        int n = 5;
        ZeroOddEven printer = new ZeroOddEven(n);

        Thread zeroThread = new Thread(() -> {
            try { printer.printZero(); }
            catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        });
        Thread oddThread = new Thread(() -> {
            try { printer.printOdd(); }
            catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        });
        Thread evenThread = new Thread(() -> {
            try { printer.printEven(); }
            catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        });


        // Start in scrambled order to prove ordering is enforced, not luck
        evenThread.start();
        oddThread.start();
        zeroThread.start();

        zeroThread.join();
        oddThread.join();
        evenThread.join();
        System.out.println();   // newline after the sequence

    }
}
