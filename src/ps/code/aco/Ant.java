package ps.code.aco;

import javafx.geometry.Point2D;
import javafx.scene.CacheHint;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Ant
        extends Circle {
    private final boolean DEBUG = false;
    private static final double MAX_VELOCITY = 3;
    private final double SEARCH_RADAR_SPAN_ANGLE = 60;
    private final double SEARCH_RADARS_COUNT = 7;
    private final double initRadarAngle = (-SEARCH_RADAR_SPAN_ANGLE / 2.0) / 180.0 * Math.PI;
    private final double deltaRadarAngle = -initRadarAngle / (SEARCH_RADARS_COUNT / 2.0);
    private final double RADAR_LENGTH = 10;


    public enum STATE {
        WANDER,
        TRACE_FOUND,
        FOOD_FOUND,
        FOOD_TO_HIVE,
        FOOD_RETURNED
    }


    private Vector2D _velocity;

    private double _direction;
    private Random random;

    private final Color[] colors = new Color[]{Color.RED, Color.GREEN, Color.BLUE, Color.VIOLET, Color.BROWN, Color.ORANGE};

    private Line collisionRadar;
    private List<Line> searchRadars;
    private List<Line> searchRadarLeft;
    private List<Line> searchRadarMid;
    private List<Line> searchRadarRight;
    private List<Point2D> radarPoints;

    public Ant(int size, double x, double y, double direction) {
        super();
        this.setCache(true);
        this.setCacheHint(CacheHint.SPEED);
        this.setRadius(size);
        this.setCenterX(x);
        this.setCenterY(y);
        this._direction = direction;
        this.setFill(Color.BROWN);
        this.random = new Random();
        this._direction = random.nextDouble() * Math.PI * 2;
        this._velocity = Vector2D.FromAngleAndMagnitude(MAX_VELOCITY, _direction);

        initializeCollisionRadar();
        initializeSearchRadar();
    }

    public Line getCollisionRadar() {
        return collisionRadar;
    }

    public List<Line> getSearchRadars() {
        return searchRadars;
    }


    public Ant() {
        this(2, 200, 200, 0);
    }

    public Ant(double x, double y) {
        this(2, x, y, 0);
    }

    public void Move(Image terrain, Image toNest, Image scent) {
        if (checkForCollision(terrain))
            _velocity.rotateBy(Math.PI / 4);


        double option = getMoveDirection(toNest);
        if (option < 0)
            option = random.nextDouble();

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

    private double getMoveDirection(Image toNest) {
        int redLeftRadar = 0, redMidRadar = 0, redRightRadar = 0;
        //return values = 0.16 or 0.5 or 0.83 + 0.2*random(1)
        //2% chance to return -1 and free roam
        double option = 0.16;

        if (random.nextDouble() < 0.02)
            return -1.0;

        for (Line l : searchRadarLeft
        ) {
            redLeftRadar += checkRadarLineValue(toNest, l, false);
            l.setStroke(Color.WHITE);
        }
        for (Line l : searchRadarMid
        ) {
            redMidRadar += checkRadarLineValue(toNest, l, false);
            l.setStroke(Color.WHITE);
        }
        for (Line l : searchRadarRight
        ) {
            redRightRadar += checkRadarLineValue(toNest, l, false);
            l.setStroke(Color.WHITE);
        }


        if (redLeftRadar == 0 && redMidRadar == 0 && redRightRadar == 0)
            return -1.0;

        int totalRed = redMidRadar + redRightRadar + redLeftRadar;
        for (Line l : searchRadarLeft
        ) {
            l.setStroke(Color.rgb((int) (255.0 * redLeftRadar / totalRed), 0, 0));
        }
        for (Line l : searchRadarMid
        ) {
            l.setStroke(Color.rgb((int) (255.0 * redMidRadar / totalRed), 0, 0));
        }
        for (Line l : searchRadarRight
        ) {
            l.setStroke(Color.rgb((int) (255.0 * redLeftRadar / totalRed), 0, 0));
        }


        if (redLeftRadar >= redMidRadar && redLeftRadar >= redRightRadar)
            return 0.16 * (1.0 + (0.2 * random.nextDouble() - 0.1));
        if (redMidRadar >= redRightRadar)
            return 0.5;

        return 0.75 * (1.0 + (0.2 * random.nextDouble() - 0.1));
    }


    private void moveLeft(double magnitude) {
        if (!DEBUG) {
            Vector2D v1 = _velocity.getPerp();
            v1.multiply(-magnitude);
            _velocity.add(v1);
        }
        return;
    }

    private void moveForward(double magnitude) {
        if (!DEBUG) {
            _velocity.multiply(magnitude + 1);
        }
        return;
    }

    private void moveRight(double magnitude) {
        if (!DEBUG) {
            Vector2D v1 = _velocity.getPerp();
            v1.multiply(magnitude);
            _velocity.add(v1);
        }
        return;
    }

    private boolean checkForCollision(@NotNull Image map) {
        PixelReader pixelReader = map.getPixelReader();
        final double sin = Math.sin(_velocity.getAngle());
        final double cos = Math.cos(_velocity.getAngle());
        try {

            for (int i = (int) (0.5 * RADAR_LENGTH); i < RADAR_LENGTH; i++) {
                int x = (int) (getCenterX() + cos * i);
                int y = (int) (getCenterY() + sin * i);
                if (pixelReader.getColor(x, y).equals(Color.BLACK)) {
                    return true;
                }
            }
            return false;
        } catch (IndexOutOfBoundsException ex) {
            return true;
        }
    }

    private double checkRadarLineValue(Image map, Line radar, boolean red) {
        //boolean red is to define if red needs to be checked , false for green
        PixelReader pixelReader = map.getPixelReader();
        final double radarAngle = Math.atan2(radar.getEndY() - radar.getStartY(), radar.getEndX() - radar.getStartX());
        final double sin = Math.sin(radarAngle);
        final double cos = Math.cos(radarAngle);
        double totalRed = 0.0, totalGreen = 0.0;
        int pxRed, pxGreen, argb;

        int prevX = 0, prevY = 0;

        try {
            for (int i = 1; i < RADAR_LENGTH; i++) {
                int x = (int) (getCenterX() + cos * i);
                int y = (int) (getCenterY() + sin * i);
                if (x == prevX & y == prevY)
                    continue;
                argb = pixelReader.getArgb(x, y);
                pxRed = (argb >> 16) & 0xFF;
                pxGreen = (argb >> 8) & 0xFF;

                totalRed += pxRed;
                totalGreen += pxGreen;
                prevX = x;
                prevY = y;
            }
            return red ? totalRed : totalGreen;
        } catch (IndexOutOfBoundsException ex) {
            return red ? totalRed : totalGreen;
        }
    }

    private void updatePosition() {
        this.setCenterX(this.getCenterX() + _velocity.getX());
        this.setCenterY(this.getCenterY() + _velocity.getY());

        updateSearchRadarsPosition();
    }

    public void leaveTrace(WritableImage writableImage) {
        int x = (int) this.getCenterX();
        int y = (int) this.getCenterY();
        Color fill = (Color) this.getFill();

        //colorBackgroundPixel(x-1, y-1, fill, writableImage);
        colorPixel(x - 1, y, fill, writableImage);
        //colorBackgroundPixel(x-1, y+1, fill, writableImage);

        colorPixel(x, y - 1, fill, writableImage);
        colorPixel(x, y, fill, writableImage);
        colorPixel(x, y + 1, fill, writableImage);

        // colorBackgroundPixel(x+1, y-1, fill, writableImage);
        colorPixel(x + 1, y, fill, writableImage);
        //colorBackgroundPixel(x+1, y+1, fill, writableImage);
    }

    private boolean colorPixel(int x, int y, Color color, WritableImage writableImage) {
        if (x < 0 || x > writableImage.getWidth() - 1 || y < 0 || y > writableImage.getHeight() - 1)
            return false;
        try {
            writableImage.getPixelWriter().setColor(x, y, color);
            return true;
        } catch (IndexOutOfBoundsException ex) {
            System.out.println(String.format("Cx=%f, Cy=%f, x=%d   y=%d", this.getCenterX(), this.getCenterY(), x, y));
            return false;
        } finally {
            return false;
        }
    }

    private void updateSearchRadarsPosition() {
        for (int i = 0; i < SEARCH_RADARS_COUNT; i++) {
            double deltaAngle = initRadarAngle + deltaRadarAngle * i;
            searchRadars.get(i).setEndX(searchRadars.get(i).getStartX() + RADAR_LENGTH * Math.cos(_velocity.getAngle() + deltaAngle));
            searchRadars.get(i).setEndY(searchRadars.get(i).getStartY() + RADAR_LENGTH * Math.sin(_velocity.getAngle() + deltaAngle));
        }
    }


    private void initializeCollisionRadar() {
        collisionRadar = new Line();

        collisionRadar.startXProperty().bind(this.centerXProperty());
        collisionRadar.startYProperty().bind(this.centerYProperty());
        collisionRadar.endXProperty().bind(_velocity.CosAngleProperty().multiply(RADAR_LENGTH).add(collisionRadar.startXProperty()));
        collisionRadar.endYProperty().bind(_velocity.SinAngleProperty().multiply(RADAR_LENGTH).add(collisionRadar.startYProperty()));
        collisionRadar.setStroke(Color.GREEN);
    }

    private void initializeSearchRadar() {
        //Search radars definition
        searchRadars = new ArrayList<>();
        searchRadarLeft = new ArrayList<>();
        searchRadarMid = new ArrayList<>();
        searchRadarRight = new ArrayList<>();

        for (int i = 0; i < SEARCH_RADARS_COUNT; i++) {
            double deltaAngle = initRadarAngle + deltaRadarAngle * i;
            Line searchRadar = new Line();
            searchRadar.startXProperty().bind(this.centerXProperty());
            searchRadar.startYProperty().bind(this.centerYProperty());

            searchRadar.setEndX(searchRadar.getStartX() + RADAR_LENGTH * Math.cos(_velocity.getAngle() + deltaAngle));
            searchRadar.setEndY(searchRadar.getStartY() + RADAR_LENGTH * Math.sin(_velocity.getAngle() + deltaAngle));
            searchRadar.setStroke(Color.RED);
            searchRadars.add(searchRadar);
        }

        searchRadarLeft.add(searchRadars.get(0));
        searchRadarLeft.add(searchRadars.get(1));
        searchRadarLeft.add(searchRadars.get(2));

        searchRadarMid.add(searchRadars.get(2));
        searchRadarMid.add(searchRadars.get(3));
        searchRadarMid.add(searchRadars.get(4));

        searchRadarRight.add(searchRadars.get(4));
        searchRadarRight.add(searchRadars.get(5));
        searchRadarRight.add(searchRadars.get(6));

    }
}
