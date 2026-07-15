package BuildingH2O;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class BuildingH2O {
    private final Semaphore hydrogenSemaphore = new Semaphore(2);
    private final Semaphore oxygenSemaphore = new Semaphore(1);
    private final AtomicInteger moleculeCount = new AtomicInteger(0);

    private final CyclicBarrier cyclicBarrier = new CyclicBarrier(3, () ->{
        int count = moleculeCount.incrementAndGet();
        System.out.print(" [molecule #" + count + " formed] ");
    });

    public void hydrogen(Runnable releaseHydrogen) throws InterruptedException
    {
        hydrogenSemaphore.acquire();
        try{
            cyclicBarrier.await();
            releaseHydrogen.run();
        }catch(Exception e){
            Thread.currentThread().interrupt();
        }finally {
            hydrogenSemaphore.release();
        }
    }

    public void oxygen(Runnable releaseOxygen) throws InterruptedException
    {
        oxygenSemaphore.acquire();
        try{
            cyclicBarrier.await();
            releaseOxygen.run();
        }catch(Exception e){
            Thread.currentThread().interrupt();
        }finally {
            oxygenSemaphore.release();
        }
    }
}
