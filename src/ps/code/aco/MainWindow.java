package ps.code.aco;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.Collection;
import java.util.List;

public class MainWindow extends Stage {
    AnchorPane root;
    private int width, height;
//    WritableImage scentTraces, envMap;
//    ImageView iwScent, iwMap;


    public MainWindow(int width, int height) {
        super();
        this.width = width;
        this.height = height;
        root = new AnchorPane();
        this.setTitle("2D Ant Colony Simulation (Search algorithm)");
        this.setScene(new Scene(root, width, height));
        this.getScene().setFill(Color.WHITE);

        //setupScentTraces();
        //setupMap();

        set00inMiddle();
        //_debug();
    }

    private void set00inMiddle() {

    }

    public void addChild(Node node) {
        this.root.getChildren().add(node);
    }

    public void addAll(Collection<? extends Node> nodes) {
        this.root.getChildren().addAll(nodes);
    }

    private void _debug() {
        Line lx = new Line(0, height / 2., width, height / 2.);
        lx.setStroke(Color.DARKGREEN);
        this.root.getChildren().add(lx);

        Line ly = new Line(width / 2., 0, width / 2., height);
        ly.setStroke(Color.DARKRED);
        this.root.getChildren().add(ly);
    }

//    private void setupScentTraces() {
//        scentTraces = new WritableImage(width, height);
//
//        for (int x = 0; x < width; x++)
//            for (int y = 0; y < height; y++)
//                scentTraces.getPixelWriter().setColor(x, y, Color.WHITE);
//
//        iwScent = new ImageView(scentTraces);
//        root.getChildren().add(iwScent);
//    }
//
//    public WritableImage get_scentTraces() {
//        return scentTraces;
//    }
//
//
//    public Image getMap() {
//        return envMap;
//    }


}
