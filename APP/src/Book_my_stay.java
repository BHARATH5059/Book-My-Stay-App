import java.util.*;

// Custom Exception for Invalid Booking
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

// Class representing a Reservation
class Reservation {
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
                ", Room Type: " + roomType;
    }
}

// Inventory Manager
class InventoryManager {

    private Map<String, Integer> roomInventory;

    public InventoryManager() {
        roomInventory = new HashMap<>();
        roomInventory.put("Single", 2);
        roomInventory.put("Double", 2);
        roomInventory.put("Suite", 1);
    }

    public void validateRoomType(String roomType) throws InvalidBookingException {
        if (!roomInventory.containsKey(roomType)) {
            throw new InvalidBookingException("Invalid room type: " + roomType);
        }
    }

    public void checkAvailability(String roomType) throws InvalidBookingException {
        if (roomInventory.get(roomType) <= 0) {
            throw new InvalidBookingException("No rooms available for: " + roomType);
        }
    }

    public void allocateRoom(String roomType) {
        roomInventory.put(roomType, roomInventory.get(roomType) - 1);
    }

    public void displayInventory() {
        System.out.println("\nCurrent Room Inventory:");
        for (Map.Entry<String, Integer> entry : roomInventory.entrySet()) {
            System.out.println(entry.getKey() + " → " + entry.getValue());
        }
    }
}

// Validator Class (Fail-Fast)
class BookingValidator {

    public static void validateInput(String reservationId, String guestName, String roomType)
            throws InvalidBookingException {

        if (reservationId == null || reservationId.trim().isEmpty()) {
            throw new InvalidBookingException("Reservation ID cannot be empty.");
        }

        if (guestName == null || guestName.trim().isEmpty()) {
            throw new InvalidBookingException("Guest name cannot be empty.");
        }

        if (roomType == null || roomType.trim().isEmpty()) {
            throw new InvalidBookingException("Room type cannot be empty.");
        }
    }
}

// Main Class
public class Book_my_stay {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        InventoryManager inventory = new InventoryManager();

        while (true) {
            try {
                System.out.println("\n--- Booking Menu ---");
                System.out.println("1. Create Booking");
                System.out.println("2. View Inventory");
                System.out.println("3. Exit");

                System.out.print("Enter choice: ");
                int choice = sc.nextInt();
                sc.nextLine(); // consume newline

                switch (choice) {

                    case 1:
                        System.out.print("Enter Reservation ID: ");
                        String id = sc.nextLine();

                        System.out.print("Enter Guest Name: ");
                        String name = sc.nextLine();

                        System.out.print("Enter Room Type (Single/Double/Suite): ");
                        String roomType = sc.nextLine();

                        // Step 1: Input Validation (Fail Fast)
                        BookingValidator.validateInput(id, name, roomType);

                        // Step 2: Business Validation
                        inventory.validateRoomType(roomType);
                        inventory.checkAvailability(roomType);

                        // Step 3: Allocation (only after validation passes)
                        inventory.allocateRoom(roomType);

                        Reservation res = new Reservation(id, name, roomType);
                        System.out.println("Booking Successful!");
                        System.out.println(res);

                        break;

                    case 2:
                        inventory.displayInventory();
                        break;

                    case 3:
                        System.out.println("Exiting...");
                        sc.close();
                        return;

                    default:
                        System.out.println("Invalid choice!");
                }

            } catch (InvalidBookingException e) {
                // Graceful handling of validation errors
                System.out.println("Booking Failed: " + e.getMessage());

            } catch (InputMismatchException e) {
                // Handle wrong input types (e.g., string instead of int)
                System.out.println("Invalid input type! Please enter correct values.");
                sc.nextLine(); // clear buffer

            } catch (Exception e) {
                // Catch-all for unexpected errors
                System.out.println("Unexpected error occurred: " + e.getMessage());
            }
        }
    }
}