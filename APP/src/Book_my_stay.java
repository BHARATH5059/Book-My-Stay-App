import java.util.*;

// Class representing an Add-On Service
class Service {
    private String serviceName;
    private double cost;

    public Service(String serviceName, double cost) {
        this.serviceName = serviceName;
        this.cost = cost;
    }

    public String getServiceName() {
        return serviceName;
    }

    public double getCost() {
        return cost;
    }

    @Override
    public String toString() {
        return serviceName + " (₹" + cost + ")";
    }
}

// Manager class to handle Add-On Services
class AddOnServiceManager {

    // Map: Reservation ID → List of Services
    private Map<String, List<Service>> reservationServicesMap;

    public AddOnServiceManager() {
        reservationServicesMap = new HashMap<>();
    }

    // Add service to a reservation
    public void addService(String reservationId, Service service) {
        reservationServicesMap
                .computeIfAbsent(reservationId, k -> new ArrayList<>())
                .add(service);
    }

    // Get all services for a reservation
    public List<Service> getServices(String reservationId) {
        return reservationServicesMap.getOrDefault(reservationId, new ArrayList<>());
    }

    // Calculate total add-on cost
    public double calculateTotalCost(String reservationId) {
        double total = 0.0;
        List<Service> services = getServices(reservationId);

        for (Service s : services) {
            total += s.getCost();
        }
        return total;
    }

    // Display services for a reservation
    public void displayServices(String reservationId) {
        List<Service> services = getServices(reservationId);

        if (services.isEmpty()) {
            System.out.println("No add-on services selected.");
            return;
        }

        System.out.println("Add-On Services for Reservation ID: " + reservationId);
        for (Service s : services) {
            System.out.println("- " + s);
        }

        System.out.println("Total Add-On Cost: ₹" + calculateTotalCost(reservationId));
    }
}

// Main Class
public class Book_my_stay {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        AddOnServiceManager manager = new AddOnServiceManager();

        System.out.print("Enter Reservation ID: ");
        String reservationId = sc.nextLine();

        while (true) {
            System.out.println("\nSelect Add-On Service:");
            System.out.println("1. Breakfast (₹500)");
            System.out.println("2. Airport Pickup (₹1000)");
            System.out.println("3. Extra Bed (₹800)");
            System.out.println("4. Done");

            System.out.print("Enter choice: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    manager.addService(reservationId, new Service("Breakfast", 500));
                    break;
                case 2:
                    manager.addService(reservationId, new Service("Airport Pickup", 1000));
                    break;
                case 3:
                    manager.addService(reservationId, new Service("Extra Bed", 800));
                    break;
                case 4:
                    System.out.println("\nFinal Service Summary:");
                    manager.displayServices(reservationId);
                    sc.close();
                    return;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }
}