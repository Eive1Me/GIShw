package sample.databasemanage.entity;

import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class MapObject {
    private Integer id;
    private Integer mapID;
    private String name;
    private String description;
    private Shape shape;
    private String color;
    private int layer;

    public MapObject() {
    }
    public MapObject toDbEntity(sample.entities.MapObject object, Integer mapID) {
        MapObject entity = new MapObject();
        javafx.scene.shape.Shape sh = (javafx.scene.shape.Shape) object.getShape();
        System.out.println(sh.getClass().toString());
        switch (sh.getClass().toString()) {
            case "javafx.scene.shape.Circle": {
                entity.setShape(Shape.CIRCLE);
            }
            case "javafx.scene.shape.Rectangle": {
                entity.setShape(Shape.RECTANGLE);
            }
            case "javafx.scene.shape.Ellipse": {
                entity.setShape(Shape.ELLIPSE);
            }
            case "javafx.scene.shape.Polygon": {
                entity.setShape(Shape.POLYGON);
            }
            case "javafx.scene.shape.Line": {
                entity.setShape(Shape.LINE);
            }
            case "javafx.scene.shape.Path": {
                entity.setShape(Shape.PATH);
            }
        }
        entity.setName(object.getName());
        entity.setDescription(object.getDescription());
        entity.setColor(sh.getFill().toString());
        entity.setLayer(1);
        entity.setMapID(mapID);

        return entity;
    }

    public MapObject(Integer id, Integer mapID, String name, String description, Shape shape, String color, int layer) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.shape = shape;
        this.color = color;
        this.layer = layer;
        this.mapID = mapID;
    }

    public Integer getMapID() {
        return mapID;
    }

    public void setMapID(Integer mapID) {
        this.mapID = mapID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Shape getShape() {
        return shape;
    }

    public void setShape(Shape shape) {
        this.shape = shape;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getLayer() {
        return layer;
    }

    public void setLayer(int layer) {
        this.layer = layer;
    }

    public enum Shape {
        LINE,
        PATH,
        RECTANGLE,
        POLYGON,
        CIRCLE,
        ELLIPSE
    }
}
