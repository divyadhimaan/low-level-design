//import java.util.concurrent.Callable;
//import java.util.concurrent.ExecutionException;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.Future;
//
//class MyCallable implements Callable<String> {
//    private final String name;
//
//    public MyCallable(String name) {
//        this.name = name;
//    }
//
//    @Override
//    public String call() throws Exception {
//        StringBuilder result = new StringBuilder();
//        for(int i=0;i<5;i++){
//            result.append("Callable ").append(name)
//                    .append(" is running: ").append(i).append("\n");
//            Thread.sleep(1000);
//        }
//        return result.toString();
//    }
//}
//
//public class CallableExample {
//    public static void main(String[] args){
//        // create executor service with a fixed thread pool of size 2
//        ExecutorService executor = Executors.newFixedThreadPool(2);
//
//        // create callable instances
//        Callable<String> callable1 = new MyCallable("A");
//        Callable<String> callable2 = new MyCallable("B");
//
//        try{
//            //submit callable tasks to the executor service and get Future objects
//            Future<String> future1 = executor.submit(callable1);
//            Future<String> future2 = executor.submit(callable2);
//
//            //get results from Future objects
//            System.out.println("Result from Callable A: \n" + future1.get()); //Blocks until the callable A is done
//            System.out.println("Result from Callable B: \n" + future2.get()); //Blocks until the callable B is done
//        }catch(InterruptedException | ExecutionException e) {
//            System.out.println("Error occurred: " + e.getMessage());
//        } finally {
//            executor.shutdown(); // Shutdown the executor service
//        }
//    }
//}
