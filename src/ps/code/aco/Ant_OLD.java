package ps.code.aco;

import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

import java.util.Timer;

public class Ant_OLD
        extends Group {
    private int _size;
    private double _LocX, _LocY;

    private double _velocity, _direction;
    private Timer _timer;

    public Ant_OLD(int size, double x, double y, double direction) {
        super();
        this._size = size;
        this._LocX = x;
        this._LocY = y;
        this._direction = direction;

        this.layoutXProperty().set(this._LocX - size / 2.0);
        this.layoutYProperty().set(this._LocY - size / 2.0);

        this.setTranslateX(_size / 2.0);
        this.setTranslateY(-_size / 2.0);
        this.setScaleY(-1.0);




        drawBody();
        drawLegs();

        _debug();
    }

    public Ant_OLD() {
        this(50, 200, 200, 0);
    }

    private void _debug() {


        Line lx = new Line(-_size/2, 0, _size/2, 0);
        lx.setStroke(Color.GREEN);
        this.getChildren().add(lx);

        Line ly = new Line(0, -_size/2, 0, _size/2);
        ly.setStroke(Color.RED);
        this.getChildren().add(ly);
    }


    private void drawBody()
    {
        Ellipse _back = new Ellipse(0,-_size/4,_size/8,_size/4);
        _back.setFill(Color.BROWN);
        this.getChildren().add(_back);
        Ellipse _mid = new Ellipse(0,_size/8,_size/16,_size/8);
        _mid.setFill(Color.BROWN);
        this.getChildren().add(_mid);

        Ellipse _head = new Ellipse(0,_size*3/8,_size/10,_size/12);
        _head.setFill(Color.BROWN);
        this.getChildren().add(_head);
    }

    private void drawLegs()
    {
        //Line leg1 = new Line(0,0,_size/8*Math.cos())

    }

    private Line createLeg(double x0, double y0, double x1, double y1)
    {
        return null;//(new Line(x0,y1,x1,y1,));
    }
}
