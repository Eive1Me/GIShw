package sample.entities;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class MapObject {
    private String name;
    private String description;
    private javafx.scene.shape.Shape shape;
    private int layer;

    public MapObject(String name, String description, Shape shape, int layer) {
        this.name = name;
        this.description = description;
        this.shape = shape;
        this.layer = layer;
    }
}
