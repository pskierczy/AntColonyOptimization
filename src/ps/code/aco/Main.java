package ps.code.aco;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class Main extends Application {
    ///
    EventHandler<MouseEvent> mouseEventEventHandler;
    Group mouseHandlerNode;
    final int WIDTH = 800, HEIGHT = 800;
    ///


    MainWindow mainWindow;
    PixelMap mapTerrain, mapPathToNest, mapPhenom;


    Ant ant;
    List<Ant> ants;
    int iCounter = 0;


    @Override
    public void start(Stage primaryStage) throws Exception {

        initialize();
        initializeHandlers();

        ants = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            ants.add(new Ant(WIDTH / 2, HEIGHT / 2));
        }

        mainWindow.addAll(ants);
        for (Ant ant : ants
        ) {
           // mainWindow.addChild(ant.getCollisionRadar());
            mainWindow.root.getChildren().addAll(ant.getSearchRadars());
        }

        AnimationTimer animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                for (Ant ant : ants
                ) {
                    ant.Move(mapTerrain.getImage(), mapPathToNest.getImage(), mapPhenom.getImage());
                    ant.leaveTrace(mapPathToNest.getWritableImage());
                }
                iCounter++;
                if (iCounter > 10) {
                    mapPathToNest.diffuse();
                    iCounter = 0;
                }
            }
        };

        animationTimer.start();
    }


    public static void main(String[] args) {
        launch(args);
    }

    private void initialize() {
        mainWindow = new MainWindow(WIDTH, HEIGHT);
        mainWindow.show();

        mapPathToNest = new PixelMap(WIDTH, HEIGHT, Color.TRANSPARENT);
        mainWindow.addChild(mapPathToNest);

        mapPhenom = new PixelMap(WIDTH, HEIGHT, Color.TRANSPARENT);
        //mainWindow.addChild(mapPathToNest);

        mapTerrain = new PixelMap(WIDTH, HEIGHT, Color.TRANSPARENT);
        mainWindow.addChild(mapTerrain);
    }


    private void initializeHandlers() {

        mouseEventEventHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.isPrimaryButtonDown()) {
                    mapTerrain.addObstacleToMap(event.getX(), event.getY());
                }
            }
        };

        mainWindow.getScene().addEventFilter(MouseEvent.ANY, mouseEventEventHandler);
    }

}
