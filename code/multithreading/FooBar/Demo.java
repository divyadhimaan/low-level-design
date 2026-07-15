package FooBar;

public class Demo {
    public static void main(String[] args) throws InterruptedException {
        int n = 5;
        FooBar fooBar = new FooBar(n);

        Thread threadFoo = new Thread(() -> {
            try{
                fooBar.foo(() -> System.out.print("foo"));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        Thread threadBar = new Thread(() -> {
            try{
                fooBar.bar(() -> System.out.print("bar"));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        threadBar.start();
        threadFoo.start();

        threadFoo.join();
        threadBar.join();
        System.out.println();   // newline after "foobarfoobarfoobar"
    }
}
