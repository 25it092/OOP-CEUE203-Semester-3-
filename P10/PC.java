class SharedBuffer {
    private final int[] buffer;
    private final int capacity;
    private int head = 0;
    private int tail = 0;
    private int size = 0;

    public SharedBuffer(int capacity) {
        this.capacity = capacity;
        this.buffer = new int[capacity];
    }

    public synchronized void produce(int value) throws InterruptedException {
        while (size == capacity) {
            wait();
        }
        buffer[tail] = value;
        tail = (tail + 1) % capacity;
        size++;
        System.out.println("Produced: " + value);
        notify();
    }

    public synchronized int consume() throws InterruptedException {
        while (size == 0) {
            wait();
        }
        int value = buffer[head];
        head = (head + 1) % capacity;
        size--;
        System.out.println("Consumed: " + value);
        notify();
        return value;
    }
}

class Producer extends Thread {
    private final SharedBuffer sharedBuffer;
    private final int totalItems;

    public Producer(SharedBuffer sharedBuffer, int totalItems) {
        this.sharedBuffer = sharedBuffer;
        this.totalItems = totalItems;
    }

    @Override
    public void run() {
        try {
            for (int i = 1; i <= totalItems; i++) {
                sharedBuffer.produce(i);
                Thread.sleep(300);
            }
        } catch (InterruptedException e) {
        }
    }
}

class Consumer extends Thread {
    private final SharedBuffer sharedBuffer;
    private final int totalItems;

    public Consumer(SharedBuffer sharedBuffer, int totalItems) {
        this.sharedBuffer = sharedBuffer;
        this.totalItems = totalItems;
    }

    @Override
    public void run() {
        try {
            for (int i = 1; i <= totalItems; i++) {
                sharedBuffer.consume();
                Thread.sleep(500);
            }
        } catch (InterruptedException e) {
        }
    }
}

public class PC {
    public static void main(String[] args) {
        SharedBuffer sharedBuffer = new SharedBuffer(3);
        int itemsToProcess = 8;

        Producer producer = new Producer(sharedBuffer, itemsToProcess);
        Consumer consumer = new Consumer(sharedBuffer, itemsToProcess);

        producer.start();
        consumer.start();
    }
}
