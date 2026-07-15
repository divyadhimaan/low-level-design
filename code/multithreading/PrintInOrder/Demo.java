package PrintInOrder;

public class Demo {

    public static void main(String[] args) throws InterruptedException{

//        Using countDownLatch
//        PrintInOrder printInOrder = new PrintInOrder();
//
//        Thread thread1 = new Thread(() -> printInOrder.first());
//        Thread thread2 = new Thread(() -> {
//            try{
//                printInOrder.second();
//            } catch (InterruptedException e) {
//                Thread.currentThread().interrupt();
//            }
//        });
//        Thread thread3 = new Thread(() -> {
//            try{
//                printInOrder.third();
//            } catch (InterruptedException e) {
//                Thread.currentThread().interrupt();
//            }
//        });
//
//        thread1.start();
//        thread3.start();
//        thread2.start();


        PrintInOrder1 printInOrder1 = new PrintInOrder1();
        Thread thread1 = new Thread(() -> {
            try{
                printInOrder1.first();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        Thread thread2 = new Thread(() -> {
            try{
                printInOrder1.second();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        Thread thread3 = new Thread(() -> {
            try{
                printInOrder1.third();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        thread1.start();
        thread3.start();
        thread2.start();

        thread2.join();
        thread1.join();
        thread3.join();
        System.out.println("\nAll threads have finished execution.");

    }
}
