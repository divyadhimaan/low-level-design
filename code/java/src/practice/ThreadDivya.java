package practice;

public class ThreadDivya extends Thread{
    public void run(){
        System.out.println("Thread running...");
    }
    public static void main(String[] args){
        ThreadDivya dd = new ThreadDivya();
        dd.start();
    }
}
