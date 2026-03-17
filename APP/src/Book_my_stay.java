import java.util.*;

abstract class Room {
    private String type;
    private double price;

    public Room(String type, double price) {
        this.type = type;
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public double getPrice() {
        return price;
    }

    public abstract void display();
}

class SingleRoom extends Room {
    public SingleRoom() {
        super("Single Room", 1000);
    }

    public void display() {
        System.out.println(getType() + " | Price: " + getPrice());
    }
}

class DoubleRoom extends Room {
    public DoubleRoom() {
        super("Double Room", 2000);
    }

    public void display() {
        System.out.println(getType() + " | Price: " + getPrice());
    }
}

class SuiteRoom extends Room {
    public SuiteRoom() {
        super("Suite Room", 5000);
    }

    public void display() {
        System.out.println(getType() + " | Price: " + getPrice());
    }
}

class RoomInventory {
    private HashMap<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single Room", 10);
        inventory.put("Double Room", 0);
        inventory.put("Suite Room", 2);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }
}

class RoomSearchService {
    private RoomInventory inventory;

    public RoomSearchService(RoomInventory inventory) {
        this.inventory = inventory;
    }

    public void searchRooms(List<Room> rooms) {
        for (Room room : rooms) {
            int available = inventory.getAvailability(room.getType());
            if (available > 0) {
                room.display();
                System.out.println("Available: " + available + "\n");
            }
        }
    }
}

public class Book_my_stay {
    public static void main(String[] args) {

        System.out.println("Welcome to Book My Stay - Hotel Booking System v4.0\n");

        RoomInventory inventory = new RoomInventory();

        List<Room> rooms = new ArrayList<>();
        rooms.add(new SingleRoom());
        rooms.add(new DoubleRoom());
        rooms.add(new SuiteRoom());

        RoomSearchService searchService = new RoomSearchService(inventory);
        searchService.searchRooms(rooms);
    }
}