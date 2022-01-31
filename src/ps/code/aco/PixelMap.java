package ps.code.aco;

import javafx.scene.SnapshotParameters;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class PixelMap extends ImageView {
    WritableImage writableImage;
    double dissolveRatio;


    public PixelMap(int width, int height, Color backColor) {
        super();
        dissolveRatio = 0.2;
        writableImage = new WritableImage(width, height);
        this.setImage(writableImage);
        fillRectangle(0, 0, width, height, Color.TRANSPARENT);
    }

    public double getDissolveRatio() {
        return dissolveRatio;
    }

    public void setDissolveRatio(double dissolveRatio) {
        this.dissolveRatio = dissolveRatio;
    }

    public PixelMap() {
        this(100, 100, Color.TRANSPARENT);
    }

    public WritableImage getWritableImage() {
        return writableImage;
    }


    private void fillRectangle(int startX, int startY, int width, int height, Color color) {
        int borderWidth = 10;

        for (int x = startX; x < startX + width; x++)
            for (int y = startY; y < startY + height; y++)
                writableImage.getPixelWriter().setColor(x, y, color);

    }

    public void addObstacleToMap(double x, double y) {
        int ix = (int) x;
        int iy = (int) y;
        for (int i = -4; i <= 4; i++)
            for (int j = -4; j <= 4; j++)
                colorPixel(i + ix, j + iy, Color.BLACK);

    }

    private boolean colorPixel(int x, int y, Color color) {
        try {
            writableImage.getPixelWriter().setColor(x, y, color);
            return true;
        } catch (IndexOutOfBoundsException ex) {
            return false;
        }
    }

    public void diffuse() {
        Rectangle clip = new Rectangle(writableImage.getWidth(), writableImage.getHeight());

        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(dissolveRatio);
        this.setClip(clip);
        this.setEffect(colorAdjust);

        writableImage = this.snapshot(new SnapshotParameters(), null);
        this.setImage(writableImage);
        this.setEffect(null);
    }

}
