package ZeroOddEven;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ZeroOddEven {
    private int curr = 1;
    private final int max;
    private boolean zeroTurn = true;

    ReentrantLock lock = new ReentrantLock();
    Condition zeroCondition = lock.newCondition();
    Condition oddCondition = lock.newCondition();
    Condition evenCondition = lock.newCondition();


    public ZeroOddEven(int n){
        this.max = n;
    }

    public void printZero() throws InterruptedException
    {
        while(true){
            lock.lock();
            try {
                while(curr <= max && !zeroTurn)
                {
                    zeroCondition.await();
                }
                if(curr > max){
                    oddCondition.signal();
                    evenCondition.signal();
                    return;
                }

                System.out.print(0);
                zeroTurn = false;
                if(curr%2==0)
                    evenCondition.signal();
                else
                    oddCondition.signal();
            }finally {
                lock.unlock();
            }
        }

    }

    public void printEven() throws InterruptedException
    {
        while(true){
            lock.lock();
            try {
                while(curr <= max && (zeroTurn || curr%2 != 0)){
                    evenCondition.await();
                }
                if(curr >  max){
                    zeroCondition.signal();
                    oddCondition.signal();
                    return;
                }

                System.out.print(curr);
                curr++;
                zeroTurn = true;
                zeroCondition.signal();
            }
            finally {
                lock.unlock();
            }
        }
    }

    public void printOdd() throws InterruptedException
    {
        while(true){
            lock.lock();
            try {
                while(curr <= max && (zeroTurn || curr%2 == 0)){
                    oddCondition.await();
                }
                if(curr >  max){
                    zeroCondition.signal();
                    evenCondition.signal();
                    return;
                }

                System.out.print(curr);
                curr++;
                zeroTurn = true;
                zeroCondition.signal();
            }
            finally {
                lock.unlock();
            }
        }
    }
}
