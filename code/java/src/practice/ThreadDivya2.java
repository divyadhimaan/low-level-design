package practice;

public class ThreadDivya2 implements Runnable{
    public void run(){
        System.out.println("Thread running...");
    }
    public static void main(String[] args){
        Thread dd = new Thread(new ThreadDivya2());
        dd.start();
    }
}
