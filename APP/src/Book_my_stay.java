import java.util.HashMap;
import java.util.Map;

class RoomInventory {
    private HashMap<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single Room", 10);
        inventory.put("Double Room", 5);
        inventory.put("Suite Room", 2);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public void updateAvailability(String roomType, int count) {
        inventory.put(roomType, count);
    }

    public void displayInventory() {
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " Available: " + entry.getValue());
        }
    }
}

/**
 * Hotel Booking Management System - Inventory Setup
 * 
 * @author 
 * @version 3.1
 */
public class Book_my_stay {
    public static void main(String[] args) {
        System.out.println("Welcome to Book My Stay - Hotel Booking System v3.1\n");

        RoomInventory inventory = new RoomInventory();

        inventory.displayInventory();

        System.out.println("\nUpdating Double Room availability...\n");
        inventory.updateAvailability("Double Room", 3);

        inventory.displayInventory();
    }
}