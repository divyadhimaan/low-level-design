package PrintInOrder;

import java.util.concurrent.CountDownLatch;

public class PrintInOrder {
    private final CountDownLatch firstDone = new CountDownLatch(1);
    private final CountDownLatch secondDone = new CountDownLatch(1);

    public void first(){
        System.out.print("first");
        firstDone.countDown();
    }
    public void second() throws InterruptedException {
        firstDone.await();
        System.out.print("second");
        secondDone.countDown();
    }
    public void third() throws InterruptedException {
        secondDone.await();
        System.out.print("third");
    }
}
