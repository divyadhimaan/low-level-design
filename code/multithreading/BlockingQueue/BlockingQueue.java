package BlockingQueue;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class BlockingQueue {
    private final Queue<Integer> q = new LinkedList<>();
    private final int capacity;
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition notFull = lock.newCondition();
    private final Condition notEmpty = lock.newCondition();

    public BlockingQueue(int cap){
        this.capacity = cap;
    }

    public void enqueue(int item) throws InterruptedException{
        lock.lock();
        try{
            while(q.size() == capacity)
                notFull.await();
            q.offer(item);
            notEmpty.signal();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally{
            lock.unlock();
        }

    }

    public int dequeue() throws InterruptedException{
        lock.lock();
        try{
            if (q.isEmpty())
                notEmpty.await();
            int val = q.poll();
            notFull.signal();
            return val;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally{
            lock.unlock();
        }

    }

    public int size() {
        lock.lock();
        try{
            return q.size();
        } finally{
            lock.unlock();
        }
    }

}

