package DiningPhilosophers;

public class Demo {

    public static void main(String[] args) throws InterruptedException {
        DiningPhilosophers table = new DiningPhilosophers();
        int rounds = 3;   // how many times each philosopher eats

        Thread[] philosophers = new Thread[5];

        for (int i = 0; i < 5; i++) {
            final int id = i;
            philosophers[i] = new Thread(() -> {
                try {
                    for (int r = 0; r < rounds; r++) {
                        table.wantsToEat(
                                id,
                                () -> System.out.println("Philosopher " + id + " picks up LEFT fork"),
                                () -> System.out.println("Philosopher " + id + " picks up RIGHT fork"),
                                () -> System.out.println("Philosopher " + id + " is EATING"),
                                () -> System.out.println("Philosopher " + id + " puts down LEFT fork"),
                                () -> System.out.println("Philosopher " + id + " puts down RIGHT fork")
                        );
                        // think for a bit between meals — increases interleaving
                        Thread.sleep((long) (Math.random() * 20));
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }, "philosopher-" + id);
        }

        for (Thread p : philosophers) p.start();
        for (Thread p : philosophers) p.join();

        System.out.println("All philosophers finished eating — no deadlock.");
    }
}