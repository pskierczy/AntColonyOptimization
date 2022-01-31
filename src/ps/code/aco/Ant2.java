package ps.code.aco;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Polyline;

import java.util.Random;

public class Ant2
        extends Group {
    private static final double MAX_VELOCITY = 3;
    private Vector2DProp _velocity;

    private double _direction;
    private Random random;
    private Circle ant;
    Line collisionRadar;

    private Polygon radar[];


    private final Color[] colors = new Color[]{Color.RED, Color.GREEN, Color.BLUE, Color.VIOLET, Color.BROWN, Color.ORANGE};


    public Ant2(int size, double x, double y, double direction) {
        super();

        ant = new Circle(0, 0, size);
        ant.setFill(Color.BROWN);
        this._direction = direction;

        this.random = new Random();
        this._direction = random.nextDouble() * Math.PI * 2;
        this._velocity = Vector2DProp.FromAngleAndMagnitude(MAX_VELOCITY, _direction);

        //this.setFill(new Color(random.nextDouble(), random.nextDouble(), random.nextDouble(), 1));
        //ant.setFill(colors[random.nextInt(6)]);
        this.setLayoutX(x);
        this.setLayoutY(y);

        //initializeRadars();

    }

    public Ant2() {
        this(2, 200, 200, 0);
    }

    public Ant2(double x, double y) {
        this(2, x, y, 0);
    }

    public void Move(Image map) {
        //checkForCollision(top, left, bottom, right);

        double option = random.nextDouble();

        if (option < 0.33)
            moveLeft(option);
        else if (option < 0.66)
            moveForward(option - 0.33);
        else
            moveRight(option - 0.66);

        _velocity.normalize();
        _velocity.multiply(MAX_VELOCITY);

        updatePosition();

    }

    public void Move(double top, double left, double bottom, double right) {
        checkForCollision(top, left, bottom, right);

        double option = random.nextDouble();

        if (option < 0.33)
            moveLeft(option);
        else if (option < 0.66)
            moveForward(option - 0.33);
        else
            moveRight(option - 0.66);

        _velocity.normalize();
        _velocity.multiply(MAX_VELOCITY);

        updatePosition();

    }

    private void moveLeft(double magnitude) {
        Vector2DProp v1 = _velocity.getPerp();
        v1.multiply(-magnitude);
        _velocity.add(v1);

        return;
    }

    private void moveForward(double magnitude) {
        _velocity.multiply(magnitude + 1);

        return;
    }

    private void moveRight(double magnitude) {
        Vector2DProp v1 = _velocity.getPerp();
        v1.multiply(magnitude);
        _velocity.add(v1);

        return;
    }

    private void checkForCollision(double top, double left, double bottom, double right) {
        if (ant.getCenterX() < left || ant.getCenterX() > right)
            this._velocity.ReverseX();
        if (ant.getCenterY() < top || ant.getCenterY() > bottom)
            this._velocity.ReverseY();

    }

    private void checkForCollision(Image map) {


//        if (this.getCenterX() < left || this.getCenterX() > right)
//            this._velocity.ReverseX();
//        if (this.getCenterY() < top || this.getCenterY() > bottom)
//            this._velocity.ReverseY();

    }

    private void updatePosition() {
//        ant.setCenterX(ant.getCenterX() + _velocity.getX());
//        ant.setCenterY(ant.getCenterY() + _velocity.getY());
        this.setLayoutX(this.getLayoutX() + _velocity.getX());
        this.setLayoutY(this.getLayoutY() + _velocity.getY());
    }

    public void leaveTrace(WritableImage writableImage) {
        int x = (int) ant.getCenterX();
        int y = (int) ant.getCenterY();
        Color fill = (Color) ant.getFill();

        //colorBackgroundPixel(x-1, y-1, fill, writableImage);
        colorBackgroundPixel(x - 1, y, fill, writableImage);
        //colorBackgroundPixel(x-1, y+1, fill, writableImage);

        colorBackgroundPixel(x, y - 1, fill, writableImage);
        colorBackgroundPixel(x, y, fill, writableImage);
        colorBackgroundPixel(x, y + 1, fill, writableImage);

        // colorBackgroundPixel(x+1, y-1, fill, writableImage);
        colorBackgroundPixel(x + 1, y, fill, writableImage);
        //colorBackgroundPixel(x+1, y+1, fill, writableImage);
    }

    private boolean colorBackgroundPixel(int x, int y, Color color, WritableImage writableImage) {
        if (x < 0 || x > writableImage.getWidth() - 1 || y < 0 || y > writableImage.getHeight() - 1)
            return false;
        try {
            writableImage.getPixelWriter().setColor(x, y, color);
            return true;
        } catch (IndexOutOfBoundsException ex) {
            System.out.println(String.format("Cx=%f, Cy=%f, x=%d   y=%d", ant.getCenterX(), ant.getCenterY(), x, y));
            return false;
        } finally {
            return true;
        }
    }

    private void initializeRadars() {
        collisionRadar = new Line();
        int radarLength = 10;
        collisionRadar.startXProperty().bind(ant.centerXProperty());
        collisionRadar.startYProperty().bind(ant.centerYProperty());
        collisionRadar.setEndX(collisionRadar.getStartX() + radarLength * Math.cos(_velocity.getAngle()));
        collisionRadar.setEndY(collisionRadar.getStartY() + radarLength * Math.sin(_velocity.getAngle()));
        collisionRadar.setStroke(Color.GREEN);

        this.getChildren().add(collisionRadar);

    }
}
