package RoundRobinPrint;

public class Demo {
    public static void main(String[] args) throws InterruptedException{
        int totalThreads = 3;
        int maxCount = 10;

        RoundRobinPrint roundRobinPrint = new RoundRobinPrint(totalThreads, maxCount);

        Thread[] threads = new Thread[totalThreads];

        for(int i=0;i<totalThreads;i++){
            final int threadId = i;
            threads[i] = new Thread(() -> {
                try {
                    roundRobinPrint.print(threadId);
                }catch(InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        for(int i=totalThreads-1;i>=0;i--){
            threads[i].start();
        }

        for(Thread t: threads){
            t.join();
        }

    }
}
