package sample;

import javafx.scene.shape.Shape;
import sample.databasemanage.entity.MapObject;

public class Utils {
    public static MapObject.Shape toShape(String className) {
        if (className.contains("Circle")) return MapObject.Shape.CIRCLE;
        if (className.contains("Rectangle")) return MapObject.Shape.RECTANGLE;
        if (className.contains("Line")) return MapObject.Shape.LINE;
        if (className.contains("Path")) return MapObject.Shape.PATH;
        if (className.contains("Ellipse")) return MapObject.Shape.ELLIPSE;
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
}
