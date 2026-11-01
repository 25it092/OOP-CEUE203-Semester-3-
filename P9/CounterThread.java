public class CounterThread extends Thread{
    static int count = 0;
    static int i=0;
    int c=0;
    public void run(){
        for(; i<=100; i++){
            count+=i;
            c++;
            System.out.println("thread: " + getName() + " at count:" + count);
        }
    }
    static int getCount(){
        return count;
    }
    public static void main(String[] args) {
        CounterThread t1 = new CounterThread();
        CounterThread t2 = new CounterThread();
        CounterThread t3 = new CounterThread();
        CounterThread t4 = new CounterThread();
        t1.start();
        t2.start();
        t3.start();
        t4.start();
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {

        }
        System.out.println(CounterThread.getCount());
        System.out.println(t1.c);
        System.out.println(t2.c);
        System.out.println(t3.c);
        System.out.println(t4.c);
    }
}

    
