package DiningPhilosophers;

import java.util.concurrent.locks.ReentrantLock;

public class DiningPhilosophers {
    private final ReentrantLock[] forks = new ReentrantLock[5];

    public DiningPhilosophers(){
        for(int i=0;i<5;i++){
            forks[i] = new ReentrantLock();
        }
    }

    public void wantsToEat(int philosopher,
                           Runnable pickLeftFork,
                           Runnable pickRightFork,
                            Runnable eat,
                            Runnable putLeftFork,
                            Runnable putRightFork) throws InterruptedException {

        int left = philosopher;
        int right = (philosopher+1)%5;

        int first = Math.min(left, right);
        int second = Math.max(left, right);

        forks[first].lock();
        forks[second].lock();

        try{
            pickLeftFork.run();
            pickRightFork.run();
            eat.run();
            putLeftFork.run();
            putRightFork.run();
        } finally {
            forks[second].unlock();
            forks[first].unlock();
        }
    }
}
