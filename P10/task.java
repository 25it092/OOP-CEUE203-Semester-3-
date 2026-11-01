import java.util.concurrent.*;
public class task implements Runnable{
    int id;
    public task(int id) { this.id = id;}
    public void run(){
        System.out.println("Task: " + id + " started by: " + Thread.currentThread().getName());
        try {
            Thread.sleep(2000);
        } 
        catch (InterruptedException e) {

        }
        System.out.println("Task: " + id + " completed by: " + Thread.currentThread().getName());
    }

    public static void main(String[] args){
        ExecutorService executor = Executors.newFixedThreadPool(3);
        for (int i = 1; i <= 10; i++) {
            executor.submit(new task(i));
        }

        executor.shutdown();
    }
}
