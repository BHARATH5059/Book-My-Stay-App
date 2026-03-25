import java.util.*;

// Reservation class
class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;
    private boolean isCancelled;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.isCancelled = false;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getRoomType() {
        return roomType;
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    public void cancel() {
        this.isCancelled = true;
    }

    @Override
    public String toString() {
        return "Reservation ID: " + reservationId +
                ", Guest: " + guestName +
                ", Room: " + roomType +
                ", Status: " + (isCancelled ? "Cancelled" : "Confirmed");
    }
}

// Inventory Manager
class InventoryManager {

    private Map<String, Integer> inventory;

    public InventoryManager() {
        inventory = new HashMap<>();
        inventory.put("Single", 2);
        inventory.put("Double", 2);
        inventory.put("Suite", 1);
    }

    public boolean isAvailable(String roomType) {
        return inventory.containsKey(roomType) && inventory.get(roomType) > 0;
    }

    public void allocate(String roomType) {
        inventory.put(roomType, inventory.get(roomType) - 1);
    }

    public void release(String roomType) {
        inventory.put(roomType, inventory.get(roomType) + 1);
    }

    public void displayInventory() {
        System.out.println("\nCurrent Inventory:");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " → " + entry.getValue());
        }
    }
}

// Booking Manager
class BookingManager {

    private Map<String, Reservation> bookings;

    public BookingManager() {
        bookings = new HashMap<>();
    }

    public void addBooking(Reservation res) {
        bookings.put(res.getReservationId(), res);
    }

    public Reservation getBooking(String id) {
        return bookings.get(id);
    }

    public void displayBookings() {
        System.out.println("\n--- All Bookings ---");
        for (Reservation r : bookings.values()) {
            System.out.println(r);
        }
    }
}

// Cancellation Service (Core Logic)
class CancellationService {

    // Stack for rollback tracking (LIFO)
    private Stack<String> rollbackStack;

    public CancellationService() {
        rollbackStack = new Stack<>();
    }

    public void cancelBooking(String reservationId,
                              BookingManager bookingManager,
                              InventoryManager inventoryManager) {

        Reservation res = bookingManager.getBooking(reservationId);

        // Validation
        if (res == null) {
            System.out.println("Cancellation Failed: Reservation not found.");
            return;
        }

        if (res.isCancelled()) {
            System.out.println("Cancellation Failed: Already cancelled.");
            return;
        }

        // Step 1: Push to rollback stack
        rollbackStack.push(res.getRoomType());

        // Step 2: Restore inventory
        inventoryManager.release(res.getRoomType());

        // Step 3: Mark booking cancelled
        res.cancel();

        System.out.println("Cancellation successful for Reservation ID: " + reservationId);
    }

    public void showRollbackStack() {
        System.out.println("\nRollback Stack (Recent Releases): " + rollbackStack);
    }
}

// Main Class
public class Book_my_stay {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        InventoryManager inventory = new InventoryManager();
        BookingManager bookingManager = new BookingManager();
        CancellationService cancellationService = new CancellationService();

        while (true) {
            System.out.println("\n--- Menu ---");
            System.out.println("1. Create Booking");
            System.out.println("2. Cancel Booking");
            System.out.println("3. View Bookings");
            System.out.println("4. View Inventory");
            System.out.println("5. View Rollback Stack");
            System.out.println("6. Exit");

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
                    String roomType = sc.nextLine();

                    if (!inventory.isAvailable(roomType)) {
                        System.out.println("Booking Failed: Room not available.");
                        break;
                    }

                    inventory.allocate(roomType);
                    Reservation res = new Reservation(id, name, roomType);
                    bookingManager.addBooking(res);

                    System.out.println("Booking successful!");
                    break;

                case 2:
                    System.out.print("Enter Reservation ID to cancel: ");
                    String cancelId = sc.nextLine();

                    cancellationService.cancelBooking(cancelId, bookingManager, inventory);
                    break;

                case 3:
                    bookingManager.displayBookings();
                    break;

                case 4:
                    inventory.displayInventory();
                    break;

                case 5:
                    cancellationService.showRollbackStack();
                    break;

                case 6:
                    System.out.println("Exiting...");
                    sc.close();
                    return;

                default:
                    System.out.println("Invalid choice!");
            }
        }
    }
}