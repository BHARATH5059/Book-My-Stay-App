/**
 * Hotel Booking Management System - Room Initialization
 *
 * @author
 * @version 2.1
 */
abstract class Room {
    private String type;
    private int beds;
    private double price;

    public Room(String type, int beds, double price) {
        this.type = type;
        this.beds = beds;
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public int getBeds() {
        return beds;
    }

    public double getPrice() {
        return price;
    }

    public abstract void display();
}

class SingleRoom extends Room {
    public SingleRoom() {
        super("Single Room", 1, 1000);
    }

    public void display() {
        System.out.println(getType() + " | Beds: " + getBeds() + " | Price: " + getPrice());
    }
}

class DoubleRoom extends Room {
    public DoubleRoom() {
        super("Double Room", 2, 2000);
    }

    public void display() {
        System.out.println(getType() + " | Beds: " + getBeds() + " | Price: " + getPrice());
    }
}

class SuiteRoom extends Room {
    public SuiteRoom() {
        super("Suite Room", 3, 5000);
    }

    public void display() {
        System.out.println(getType() + " | Beds: " + getBeds() + " | Price: " + getPrice());
    }
}

public class Book_my_stay {
    public static void main(String[] args) {

        Room single = new SingleRoom();
        Room dbl = new DoubleRoom();
        Room suite = new SuiteRoom();

        int singleAvailable = 10;
        int doubleAvailable = 5;
        int suiteAvailable = 2;

        System.out.println("Welcome to Book My Stay - Hotel Booking System v2.1\n");

        single.display();
        System.out.println("Available: " + singleAvailable + "\n");

        dbl.display();
        System.out.println("Available: " + doubleAvailable + "\n");

        suite.display();
        System.out.println("Available: " + suiteAvailable);
    }
}