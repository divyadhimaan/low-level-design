import java.util.concurrent.*;

class myThread extends Thread{
    @Override
    public void run() {
        System.out.println("Thread is running");
    }
}

class MyRunnable implements Runnable{
    @Override
    public void run() {
        System.out.println("Runnable is running");
    }
}

class MyCallable implements Callable<String> {
    @Override
    public String call() throws Exception {
        return "Callable is running";
    }
}

class myClass {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // 1. Create a thread by extending the Thread class
//        myThread t1 = new myThread();
//        t1.start();


        // 2. Create a thread by implementing the Runnable interface
//        Runnable runnable = new MyRunnable();
//        new Thread(runnable).start();
//
//        ExecutorService pool = Executors.newFixedThreadPool(2);
//        pool.execute(runnable);
//        pool.shutdown();

        // 3. Create a thread by Callable interface
        ExecutorService pool = Executors.newFixedThreadPool(2);
        Callable<String> callable = new MyCallable();

        Future<String> future = pool.submit(callable);
        String result = future.get();
        System.out.println(result);
        pool.shutdown();

    }
}