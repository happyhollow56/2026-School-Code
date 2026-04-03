/**
 * Test program for MyRectangle2D class.
 */
public class TestMyRectangle2D {
    public static void main(String[] args) {
        // Create a MyRectangle2D object
        MyRectangle2D r1 = new MyRectangle2D(2, 2, 5.5, 4.9);

        // Display area and perimeter
        System.out.println("Rectangle r1:");
        System.out.println("Area: " + r1.getArea());
        System.out.println("Perimeter: " + r1.getPerimeter());

        // Test contains(3, 3)
        System.out.println("\nr1.contains(3, 3): " + r1.contains(3, 3));

        // Test contains with another rectangle
        MyRectangle2D r2 = new MyRectangle2D(4, 5, 10.5, 3.2);
        System.out.println("r1.contains(new MyRectangle2D(4, 5, 10.5, 3.2)): " + 
                           r1.contains(r2));

        // Test overlaps with another rectangle
        MyRectangle2D r3 = new MyRectangle2D(3, 5, 2.3, 5.4);
        System.out.println("r1.overlaps(new MyRectangle2D(3, 5, 2.3, 5.4)): " + 
                           r1.overlaps(r3));
    }
}
