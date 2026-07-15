package PrintInOrder;

import java.util.concurrent.CountDownLatch;

public class PrintInOrder1 {
    private int state = 1;
    private final Object lock = new Object();

    public void first() throws InterruptedException{
        synchronized (lock){
            System.out.print("first");
            state = 2;
            lock.notifyAll();
        }
    }
    public void second() throws InterruptedException {
        synchronized (lock){
            while(state != 2){
                lock.wait();
            }
            System.out.print("second");
            state = 3;
            lock.notifyAll();
        }
    }
    public void third() throws InterruptedException {
        synchronized (lock){
            while(state != 3){
                lock.wait();
            }
            System.out.print("third");
            state = 4;
            lock.notifyAll();
        }
    }
}
