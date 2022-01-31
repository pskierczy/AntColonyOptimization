package ps.code.aco;
//based on
//https://gist.github.com/gunvirranu/6816d65c0231981787ebefd3bdb61f98
//

import com.sun.javafx.scene.DirtyBits;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.scene.shape.Circle;


public class Vector2DProp {

    private DoubleProperty x = null;
    private DoubleProperty y = null;

    public Vector2DProp() {
    }

    public final DoubleProperty XProperty() {
        if (x == null) {
            x = new DoublePropertyBase(0.0) {
                @Override
                public Object getBean() {
                    return this;
                }

                @Override
                public String getName() {
                    return "X";
                }
            };
        }
        return x;
    }

    public final DoubleProperty YProperty() {
        if (y == null) {
            y = new DoublePropertyBase(0.0) {
                @Override
                public Object getBean() {
                    return this;
                }

                @Override
                public String getName() {
                    return "Y";
                }
            };
        }
        return y;
    }

    private void setXval(double xval) {
        if (x != null)
            XProperty().set(xval);

    }

    private void setYval(double yval) {
        if (y != null)
            YProperty().set(yval);
    }

    private double getXval() {
        return XProperty().doubleValue();
    }

    private double getYval() {
        return YProperty().doubleValue();
    }

    public Vector2DProp(double x, double y) {
        setXval(x);
        setYval(y);
    }

    public static Vector2DProp FromAngleAndMagnitude(double magnitude, double angleInRadians) {
        return new Vector2DProp(magnitude * Math.cos(angleInRadians), magnitude * Math.sin(angleInRadians));
    }

    public Vector2DProp(Vector2DProp v) {
        set(v);
    }

    public double getX() {
        return getXval();
    }

    public double getY() {
        return getYval();
    }


    public void set(double x, double y) {
        setXval(x);
        setYval(y);
    }

    public void set(Vector2DProp v) {
        this.x = v.x;
        this.y = v.y;
    }

    public void setZero() {
        setXval(0);
        setYval(0);
    }

    public double[] getComponents() {
        return new double[]{getX(), getY()};
    }

    public double getLength() {
        return Math.sqrt(getX() * getX() + getY() * getY());
    }

    public double getLengthSq() {
        return (getX() * getX() + getY() * getY());
    }

    public double distanceSq(double vx, double vy) {
        vx = getX();
        vy -= getY();
        return (vx * vx + vy * vy);
    }

    public double distanceSq(Vector2DProp v) {
        double vx = v.getX() - getX();
        double vy = v.getY() - getY();
        return (vx * vx + vy * vy);
    }

    public double distance(double vx, double vy) {
        vx -= getX();
        vy -= getY();
        return Math.sqrt(vx * vx + vy * vy);
    }

    public double distance(Vector2DProp v) {
        double vx = v.getX() - getX();
        double vy = v.getY() - getY();
        return Math.sqrt(vx * vx + vy * vy);
    }

    public double getAngle() {
        return Math.atan2(getY(), getX());
    }

    public void normalize() {
        double magnitude = getLength();
        setXval(getX() / magnitude);
        setYval(getY() / magnitude);
    }

    public Vector2DProp getNormalized() {
        double magnitude = getLength();
        return new Vector2DProp(getX() / magnitude, getY() / magnitude);
    }

    public static Vector2DProp toCartesian(double magnitude, double angle) {
        return new Vector2DProp(magnitude * Math.cos(angle), magnitude * Math.sin(angle));
    }

    public void add(Vector2DProp v) {
        setXval(getX() + v.getX());
        setYval(getX() + v.getX());
    }

    public void add(double vx, double vy) {
        setXval(getX() + vx);
        setYval(getX() + vy);
    }

    public static Vector2DProp add(Vector2DProp v1, Vector2DProp v2) {
        return new Vector2DProp(v1.getX() + v2.getX(), v1.getY() + v2.getY());
    }

    public Vector2DProp getAdded(Vector2DProp v) {
        return new Vector2DProp(getX() + v.getX(), getY() + v.getY());
    }

    public void subtract(Vector2DProp v) {
        setXval(getX() - v.getX());
        setYval(getX() - v.getX());
    }

    public void subtract(double vx, double vy) {
        setXval(getX() - vx);
        setYval(getX() - vy);
    }

    public static Vector2DProp subtract(Vector2DProp v1, Vector2DProp v2) {
        return new Vector2DProp(v1.getX() - v2.getX(), v1.getY() - v2.getY());
    }

    public Vector2DProp getSubtracted(Vector2DProp v) {
        return new Vector2DProp(getX() - v.getX(), getY() - v.getY());
    }

    public void multiply(double scalar) {
        setXval(getX() * scalar);
        setYval(getY() * scalar);
    }

    public Vector2DProp getMultiplied(double scalar) {
        return new Vector2DProp(getX() * scalar, getY() * scalar);
    }

    public void divide(double scalar) {
        setXval(getX() / scalar);
        setYval(getY() / scalar);
    }

    public Vector2DProp getDivided(double scalar) {
        return new Vector2DProp(getX() / scalar, getY() / scalar);
    }

    public Vector2DProp getPerp() {
        return new Vector2DProp(-getY(), getX());
    }

    public double dot(Vector2DProp v) {
        return (getX() * v.getX() + getY() * v.getY());
    }

    public double dot(double vx, double vy) {
        return (getX() * vx + getY() * vy);
    }

    public static double dot(Vector2DProp v1, Vector2DProp v2) {
        return v1.getX() * v2.getX() + v1.getY() * v2.getY();
    }

    public double cross(Vector2DProp v) {
        return (getX() * v.getY() - getY() * v.getX());
    }

    public double cross(double vx, double vy) {
        return (getX() * vy - getY() * vx);
    }

    public static double cross(Vector2DProp v1, Vector2DProp v2) {
        return (v1.getX() * v2.getY() - v1.getY() * v2.getX());
    }

    public double project(Vector2DProp v) {
        return (this.dot(v) / this.getLength());
    }

    public double project(double vx, double vy) {
        return (this.dot(vx, vy) / this.getLength());
    }

    public static double project(Vector2DProp v1, Vector2DProp v2) {
        return (dot(v1, v2) / v1.getLength());
    }

    public Vector2DProp getProjectedVector(Vector2DProp v) {
        return this.getNormalized().getMultiplied(this.dot(v) / this.getLength());
    }

    public Vector2DProp getProjectedVector(double vx, double vy) {
        return this.getNormalized().getMultiplied(this.dot(vx, vy) / this.getLength());
    }

    public static Vector2DProp getProjectedVector(Vector2DProp v1, Vector2DProp v2) {
        return v1.getNormalized().getMultiplied(Vector2DProp.dot(v1, v2) / v1.getLength());
    }

    public void rotateBy(double angle) {
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        double rx = getX() * cos - getY() * sin;
        setYval(getX() * sin + getY() * cos);
        setXval(rx);
    }

    public Vector2DProp getRotatedBy(double angle) {
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        return new Vector2DProp(getX() * cos - getY() * sin, getX() * sin + getY() * cos);
    }

    public void rotateTo(double angle) {
        set(toCartesian(getLength(), angle));
    }

    public Vector2DProp getRotatedTo(double angle) {
        return toCartesian(getLength(), angle);
    }

    public void reverse() {
        setXval(-getX());
        setYval(-getY());
    }

    public void ReverseX() {
        setXval(-getX());
    }

    public void ReverseY() {
        setYval(-getY());
    }

    public Vector2DProp getReversed() {
        return new Vector2DProp(-getX(), -getY());
    }

    @Override
    public Vector2DProp clone() {
        return new Vector2DProp(getX(), getY());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof Vector2DProp) {
            Vector2DProp v = (Vector2DProp) obj;
            return (x == v.x) && (y == v.y);
        }
        return false;
    }

    @Override
    public String toString() {
        return "Vector2d[" + x + ", " + y + "]";
    }
}