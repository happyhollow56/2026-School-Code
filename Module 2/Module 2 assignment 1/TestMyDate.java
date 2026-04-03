/**
 * Test program for MyDate class.
 */
public class TestMyDate {
    public static void main(String[] args) {
        // Create two MyDate objects
        MyDate date1 = new MyDate();
        MyDate date2 = new MyDate(34355555133101L);

        // Display date1 (current date)
        System.out.println("Date 1 (current date):");
        System.out.println("Year: " + date1.getYear());
        System.out.println("Month: " + date1.getMonth() + " (0-based, 0=January)");
        System.out.println("Day: " + date1.getDay());

        // Display date2 (elapsed time)
        System.out.println("\nDate 2 (elapsed time 34355555133101L):");
        System.out.println("Year: " + date2.getYear());
        System.out.println("Month: " + date2.getMonth() + " (0-based, 0=January)");
        System.out.println("Day: " + date2.getDay());
    }
}
