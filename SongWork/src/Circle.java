import java.io.Serializable;

class Circle implements Serializable {

    private static final long serialVersionUID = -7478396453816473169L;
    private double radius;
    private double area;

    public Circle() {
        super();
        this.radius = -1.0;
    }

    public Circle(double radius) {
        super();
        this.radius = radius;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }

    @Override
    public String toString() {
        return "Circle [radius=" + radius + ", area=" + area + "]";
    }

}
