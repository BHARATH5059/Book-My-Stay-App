import java.util.*;

class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}

class BookingQueue {
    private Queue<Reservation> queue = new LinkedList<>();

    public void addRequest(Reservation r) {
        queue.offer(r);
    }

    public Reservation getNextRequest() {
        return queue.poll();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}

class RoomInventory {
    private Map<String, Integer> inventory = new HashMap<>();

    public RoomInventory() {
        inventory.put("Single Room", 2);
        inventory.put("Double Room", 1);
        inventory.put("Suite Room", 1);
    }

    public int getAvailability(String type) {
        return inventory.getOrDefault(type, 0);
    }

    public void decrement(String type) {
        inventory.put(type, inventory.get(type) - 1);
    }
}

class BookingService {
    private RoomInventory inventory;
    private Map<String, Set<String>> allocatedRooms = new HashMap<>();
    private int idCounter = 1;

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
    }

    public void process(BookingQueue queue) {
        while (!queue.isEmpty()) {
            Reservation r = queue.getNextRequest();
            String type = r.getRoomType();

            if (inventory.getAvailability(type) > 0) {
                String roomId = type.replaceAll("\\s+", "").toUpperCase() + "_" + idCounter++;
                allocatedRooms.putIfAbsent(type, new HashSet<>());

                if (!allocatedRooms.get(type).contains(roomId)) {
                    allocatedRooms.get(type).add(roomId);
                    inventory.decrement(type);
                    System.out.println("Booking Confirmed: " + r.getGuestName() + " -> " + roomId);
                }
            } else {
                System.out.println("Booking Failed (No Availability): " + r.getGuestName());
            }
        }
    }
}

public class Book_my_stay {
    public static void main(String[] args) {

        System.out.println("Welcome to Book My Stay - Hotel Booking System v6.0\n");

        BookingQueue queue = new BookingQueue();
        queue.addRequest(new Reservation("Alice", "Single Room"));
        queue.addRequest(new Reservation("Bob", "Single Room"));
        queue.addRequest(new Reservation("Charlie", "Single Room"));
        queue.addRequest(new Reservation("David", "Suite Room"));

        RoomInventory inventory = new RoomInventory();
        BookingService service = new BookingService(inventory);

        service.process(queue);
    }
}