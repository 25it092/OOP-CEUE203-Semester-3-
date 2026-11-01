class SeatBooking {
    static int seatsLeft = 5;
    static int successfulBookings = 0;

    public static void bookUnsync() {
        if (seatsLeft > 0) {
            try { 
                Thread.sleep(10); 
            } catch (InterruptedException e) {}
            seatsLeft--;
            successfulBookings++;
            System.out.println(Thread.currentThread().getName() + " booked a seat!");
        }
    }

    public static synchronized void bookSync() {
        if (seatsLeft > 0) {
            try { 
                Thread.sleep(10); 
            } catch (InterruptedException e) {}
            seatsLeft--;
            successfulBookings++;
            System.out.println(Thread.currentThread().getName() + " booked a seat!");
        }
    }

    public static void runTest(boolean useSync) throws InterruptedException {
        seatsLeft = 5;
        successfulBookings = 0;
        Thread[] threads = new Thread[10];

        for (int i = 0; i < 10; i++) {
            Runnable task = new Runnable() {
                public void run() {
                    if (useSync) {
                        bookSync();
                    } else {
                        bookUnsync();
                    }
                }
            };
            threads[i] = new Thread(task, "Thread-" + (i + 1));
            threads[i].start();
        }

        for (Thread t : threads) {
            t.join();
        }

        System.out.println("Total Seats Booked: " + successfulBookings + "\n");
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("--- WITHOUT SYNCHRONIZATION ---");
        runTest(false);

        System.out.println("--- WITH SYNCHRONIZATION ---");
        runTest(true);
    }
}
