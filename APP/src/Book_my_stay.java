import java.util.*;

// Booking Request
class BookingRequest {
    String guestName;
    String roomType;

    public BookingRequest(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }
}

// Thread-safe Inventory Manager
class InventoryManager {

    private Map<String, Integer> inventory;

    public InventoryManager() {
        inventory = new HashMap<>();
        inventory.put("Single", 2);
        inventory.put("Double", 2);
        inventory.put("Suite", 1);
    }

    // Critical Section: synchronized method
    public synchronized boolean allocateRoom(String roomType, String guestName) {

        if (!inventory.containsKey(roomType)) {
            System.out.println("Invalid room type for " + guestName);
            return false;
        }

        int available = inventory.get(roomType);

        if (available > 0) {
            // Simulate delay to expose race condition if unsynchronized
            try { Thread.sleep(100); } catch (InterruptedException e) {}

            inventory.put(roomType, available - 1);

            System.out.println("Booking SUCCESS for " + guestName +
                               " → " + roomType +
                               " | Remaining: " + (available - 1));
            return true;
        } else {
            System.out.println("Booking FAILED for " + guestName +
                               " → No " + roomType + " rooms available");
            return false;
        }
    }

    public void displayInventory() {
        System.out.println("\nFinal Inventory:");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " → " + entry.getValue());
        }
    }
}

// Shared Booking Queue
class BookingQueue {

    private Queue<BookingRequest> queue = new LinkedList<>();

    public synchronized void addRequest(BookingRequest request) {
        queue.add(request);
        notify(); // notify waiting threads
    }

    public synchronized BookingRequest getRequest() {
        while (queue.isEmpty()) {
            try {
                wait(); // wait for new requests
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return queue.poll();
    }
}

// Worker Thread
class BookingProcessor extends Thread {

    private BookingQueue queue;
    private InventoryManager inventory;

    public BookingProcessor(BookingQueue queue, InventoryManager inventory, String name) {
        super(name);
        this.queue = queue;
        this.inventory = inventory;
    }

    @Override
    public void run() {
        while (true) {
            BookingRequest request = queue.getRequest();

            if (request == null) continue;

            inventory.allocateRoom(request.roomType, request.guestName);

            // Stop condition (for demo purpose)
            if (Thread.currentThread().isInterrupted()) break;
        }
    }
}

// Main Class
public class Book_my_stay {

    public static void main(String[] args) throws InterruptedException {

        BookingQueue queue = new BookingQueue();
        InventoryManager inventory = new InventoryManager();

        // Create worker threads (simulating concurrent processing)
        BookingProcessor t1 = new BookingProcessor(queue, inventory, "Processor-1");
        BookingProcessor t2 = new BookingProcessor(queue, inventory, "Processor-2");

        t1.start();
        t2.start();

        // Simulate multiple guest requests (concurrent input)
        queue.addRequest(new BookingRequest("Alice", "Single"));
        queue.addRequest(new BookingRequest("Bob", "Single"));
        queue.addRequest(new BookingRequest("Charlie", "Single")); // may fail

        queue.addRequest(new BookingRequest("David", "Double"));
        queue.addRequest(new BookingRequest("Eve", "Double"));
        queue.addRequest(new BookingRequest("Frank", "Double")); // may fail

        queue.addRequest(new BookingRequest("Grace", "Suite"));
        queue.addRequest(new BookingRequest("Heidi", "Suite")); // will fail

        // Allow time for processing
        Thread.sleep(2000);

        // Stop threads (demo purpose)
        t1.interrupt();
        t2.interrupt();

        // Display final inventory
        inventory.displayInventory();
    }
}