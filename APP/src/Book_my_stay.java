import java.io.*;
import java.util.*;

// Reservation class (Serializable)
class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;

    private String reservationId;
    private String guestName;
    private String roomType;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    @Override
    public String toString() {
        return "Reservation ID: " + reservationId +
                ", Guest: " + guestName +
                ", Room: " + roomType;
    }
}

// Wrapper class for persistence (Serializable)
class SystemState implements Serializable {
    private static final long serialVersionUID = 1L;

    List<Reservation> reservations;
    Map<String, Integer> inventory;

    public SystemState(List<Reservation> reservations, Map<String, Integer> inventory) {
        this.reservations = reservations;
        this.inventory = inventory;
    }
}

// Persistence Service
class PersistenceService {

    private static final String FILE_NAME = "system_state.dat";

    // Save state
    public void save(SystemState state) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(state);
            System.out.println("System state saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving state: " + e.getMessage());
        }
    }

    // Load state
    public SystemState load() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            System.out.println("System state loaded successfully.");
            return (SystemState) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("No previous state found. Starting fresh.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading state. Starting with safe defaults.");
        }
        return null;
    }
}

// Inventory Manager
class InventoryManager {

    Map<String, Integer> inventory;

    public InventoryManager() {
        inventory = new HashMap<>();
        inventory.put("Single", 2);
        inventory.put("Double", 2);
        inventory.put("Suite", 1);
    }

    public void setInventory(Map<String, Integer> data) {
        this.inventory = data;
    }

    public boolean allocate(String roomType) {
        if (inventory.containsKey(roomType) && inventory.get(roomType) > 0) {
            inventory.put(roomType, inventory.get(roomType) - 1);
            return true;
        }
        return false;
    }

    public void display() {
        System.out.println("\nInventory:");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " → " + entry.getValue());
        }
    }
}

// Main Class
public class Book_my_stay {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        PersistenceService persistenceService = new PersistenceService();
        InventoryManager inventoryManager = new InventoryManager();

        List<Reservation> reservations = new ArrayList<>();

        // 🔹 Load previous state (Recovery)
        SystemState loadedState = persistenceService.load();
        if (loadedState != null) {
            reservations = loadedState.reservations;
            inventoryManager.setInventory(loadedState.inventory);
        }

        while (true) {
            System.out.println("\n--- Menu ---");
            System.out.println("1. Create Booking");
            System.out.println("2. View Bookings");
            System.out.println("3. View Inventory");
            System.out.println("4. Save & Exit");

            System.out.print("Enter choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1:
                    System.out.print("Enter Reservation ID: ");
                    String id = sc.nextLine();

                    System.out.print("Enter Guest Name: ");
                    String name = sc.nextLine();

                    System.out.print("Enter Room Type (Single/Double/Suite): ");
                    String room = sc.nextLine();

                    if (inventoryManager.allocate(room)) {
                        reservations.add(new Reservation(id, name, room));
                        System.out.println("Booking successful!");
                    } else {
                        System.out.println("Booking failed! Room not available.");
                    }
                    break;

                case 2:
                    System.out.println("\n--- Booking History ---");
                    for (Reservation r : reservations) {
                        System.out.println(r);
                    }
                    break;

                case 3:
                    inventoryManager.display();
                    break;

                case 4:
                    // 🔹 Save state before exit
                    SystemState state = new SystemState(reservations, inventoryManager.inventory);
                    persistenceService.save(state);

                    System.out.println("Exiting...");
                    sc.close();
                    return;

                default:
                    System.out.println("Invalid choice!");
            }
        }
    }
}