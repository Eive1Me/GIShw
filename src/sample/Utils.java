package sample;

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
}
