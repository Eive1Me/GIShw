package sample;

import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import sample.databasemanage.entity.MapObject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class Utils {
    public static MapObject.Shape toShape(String className) {
        if (className.contains("Circle")) return MapObject.Shape.CIRCLE;
        if (className.contains("Rectangle")) return MapObject.Shape.RECTANGLE;
        if (className.contains("Line")) return MapObject.Shape.LINE;
        if (className.contains("Path")) return MapObject.Shape.PATH;
        if (className.contains("Ellipse")) return MapObject.Shape.ELLIPSE;
        if (className.contains("Polyline")) return MapObject.Shape.POLYLINE;
        if (className.contains("Polygon")) return MapObject.Shape.POLYGON;
        return null;
    }

    public static boolean isCircle(Shape shape) {
        if (toShape(shape.getClass().getName()) == MapObject.Shape.CIRCLE)
            return true;
        return false;
    }

    public static boolean isEllipse(Shape shape) {
        if (toShape(shape.getClass().getName()) == MapObject.Shape.ELLIPSE)
            return true;
        return false;
    }

    public static boolean isLine(Shape shape) {
        if (toShape(shape.getClass().getName()) == MapObject.Shape.LINE)
            return true;
        return false;
    }

    public static boolean isRectangle(Shape shape) {
        if (toShape(shape.getClass().getName()) == MapObject.Shape.RECTANGLE)
            return true;
        return false;
    }

    public static boolean isPolyline(Shape shape) {
        if (toShape(shape.getClass().getName()) == MapObject.Shape.POLYLINE)
            return true;
        return false;
    }

    public static boolean isPolygon(Shape shape) {
        if (toShape(shape.getClass().getName()) == MapObject.Shape.POLYGON)
            return true;
        return false;
    }

    public static BufferedImage bytesToImage(byte[] bytes) throws IOException {
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        BufferedImage bImage2 = ImageIO.read(bis);
//        ImageIO.write(bImage2, , new File("output.jpg") );
        System.out.println("image created");
        return bImage2;
    }

    // Helper method
    private static String format(double val) {
        String in = Integer.toHexString((int) Math.round(val * 255));
        return in.length() == 1 ? "0" + in : in;
    }

    public static String toHexString(Color value) {
        return "#" + (format(value.getRed()) + format(value.getGreen()) + format(value.getBlue()) + format(value.getOpacity()))
                .toUpperCase();
    }

}
