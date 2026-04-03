/**
 * MyRectangle2D class represents a rectangle in 2D space.
 * 
 * UML Diagram:
 * +----------------------------------+
 * |         MyRectangle2D            |
 * +----------------------------------+
 * | - x: double                      |
 * | - y: double                      |
 * | - width: double                  |
 * | - height: double                 |
 * +----------------------------------+
 * | + MyRectangle2D()                |
 * | + MyRectangle2D(x, y, w, h)      |
 * | + getX(): double                 |
 * | + getY(): double                 |
 * | + getWidth(): double             |
 * | + getHeight(): double            |
 * | + setX(x: double): void          |
 * | + setY(y: double): void          |
 * | + setWidth(w: double): void      |
 * | + setHeight(h: double): void     |
 * | + getArea(): double              |
 * | + getPerimeter(): double         |
 * | + contains(x, y: double): boolean|
 * | + contains(r: MyRectangle2D):    |
 * |     boolean                      |
 * | + overlaps(r: MyRectangle2D):    |
 * |     boolean                      |
 * +----------------------------------+
 */
public class MyRectangle2D {
    private double x;
    private double y;
    private double width;
    private double height;

    // No-arg constructor
    public MyRectangle2D() {
        this(0, 0, 1, 1);
    }

    // Constructor with parameters
    public MyRectangle2D(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    // Getters
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    // Setters
    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    // Get area
    public double getArea() {
        return width * height;
    }

    // Get perimeter
    public double getPerimeter() {
        return 2 * (width + height);
    }

    // Check if point is inside rectangle
    public boolean contains(double x, double y) {
        return x >= this.x - width / 2 && x <= this.x + width / 2 &&
               y >= this.y - height / 2 && y <= this.y + height / 2;
    }

    // Check if another rectangle is inside this rectangle
    public boolean contains(MyRectangle2D r) {
        return contains(r.x - r.width / 2, r.y - r.height / 2) &&
               contains(r.x + r.width / 2, r.y + r.height / 2);
    }

    // Check if another rectangle overlaps with this rectangle
    public boolean overlaps(MyRectangle2D r) {
        return !(this.x + width / 2 < r.x - r.width / 2 ||
                 this.x - width / 2 > r.x + r.width / 2 ||
                 this.y + height / 2 < r.y - r.height / 2 ||
                 this.y - height / 2 > r.y + r.height / 2);
    }
}