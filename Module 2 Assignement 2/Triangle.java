/*UML Diagram for Triangle class:
+------------------+
| GeometricObject  | (abstract)
+------------------+
| - color: String  |
| - filled: boolean|
+------------------+
| + GeometricObject()|
| + GeometricObject(color: String, filled: boolean) |
| + getColor(): String |
| + setColor(color: String): void |
| + isFilled(): boolean |
| + setFilled(filled: boolean): void |
| + getArea(): double {abstract} |
| + getPerimeter(): double {abstract} |
+------------------+
          △
          | extends
+------------------+
|    Triangle      |
+------------------+
| - side1: double  |
| - side2: double  |
| - side3: double  |
+------------------+
| + Triangle()     |
| + Triangle(side1: double, side2: double, side3: double) |
| + getSide1(): double |
| + getSide2(): double |
| + getSide3(): double |
| + getArea(): double |
| + getPerimeter(): double |
| + toString(): String |
+------------------+ */

public class Triangle extends GeometricObject {
    private double side1 = 1.0;
    private double side2 = 1.0;
    private double side3 = 1.0;

    /** No-arg constructor creates default triangle */
    public Triangle() {
    }

    /** Constructor with specified sides */
    public Triangle(double side1, double side2, double side3) {
        this.side1 = side1;
        this.side2 = side2;
        this.side3 = side3;
    }

    /** Accessor methods */
    public double getSide1() {
        return side1;
    }

    public double getSide2() {
        return side2;
    }

    public double getSide3() {
        return side3;
    }

    /** Calculate area using Heron's formula */
    @Override
    public double getArea() {
        double s = getPerimeter() / 2;
        return Math.sqrt(s * (s - side1) * (s - side2) * (s - side3));
    }

    /** Calculate perimeter */
    @Override
    public double getPerimeter() {
        return side1 + side2 + side3;
    }

    /** Return string description */
    @Override
    public String toString() {
        return "Triangle: side1 = " + side1 + " side2 = " + side2 + " side3 = " + side3;
    }
}