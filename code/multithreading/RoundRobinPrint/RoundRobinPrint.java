package RoundRobinPrint;

public class RoundRobinPrint {
    private int current = 1;
    private final int totalThreads;
    private final int maxCount;
    private final Object lock = new Object();

    RoundRobinPrint(int totalThreads, int maxCount) {
        this.totalThreads = totalThreads;
        this.maxCount = maxCount;
    }

    public void print(int threadId) throws InterruptedException{
        while(true){
            synchronized (lock){
                while(current <= maxCount && (current-1)%totalThreads != threadId){
                    lock.wait();
                }

                if(current > maxCount){
                    lock.notifyAll();
                    return;
                }

                System.out.println("T" + (threadId+1) + ": " + current);
                current++;
                lock.notifyAll();
            }

        }
    }
}
