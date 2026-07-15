package BlockingQueue;

public class Demo {
    public static void main(String[] args) throws InterruptedException {
        // --- Part 1: basic single-threaded sanity check ---
        BlockingQueue queue = new BlockingQueue(5);
        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);
        System.out.println("Queue size: " + queue.size());
        queue.dequeue();
        System.out.println("Queue size after dequeue: " + queue.size());

        System.out.println("---- concurrent demo ----");

        // --- Part 2: real producer/consumer with blocking ---
        BlockingQueue bq = new BlockingQueue(3);  // small capacity forces blocking
        // Producer: tries to add 6 items into a capacity-3 queue → will block when full
        Thread producer = new Thread(() -> {
            try {
                for (int i = 1; i <= 6; i++) {
                    bq.enqueue(i);
                    System.out.println("Produced: " + i + " (size=" + bq.size() + ")");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "producer");

        // Consumer: drains slowly, so the producer is forced to wait for space
        Thread consumer = new Thread(()->{
            try{
                for(int i=1;i<=6;i++){
                    Thread.sleep(200);
                    System.out.println("          Consumed: " + bq.dequeue());
                }
            }catch (InterruptedException e){
                Thread.currentThread().interrupt();
            }
        }, "consumer");

        producer.start();
        consumer.start();

        producer.join();
        consumer.join();

        System.out.println("Done. Final size: " + bq.size());  // 0
    }
}
