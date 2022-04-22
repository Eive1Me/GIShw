package sample.entities;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import sample.Utils;

import java.util.List;

public class MapObject {
    private String name;
    private String description;
    private Node shape;
    private int layer;

    public MapObject(String name, String description, Node shape, int layer) {
        this.name = name;
        this.description = description;
        this.shape = shape;
        this.layer = layer;
    }

    public sample.databasemanage.entity.MapObject toDbEntity(sample.entities.MapObject object, Integer mapID) {
        sample.databasemanage.entity.MapObject entity = new sample.databasemanage.entity.MapObject();
        javafx.scene.shape.Shape sh = (javafx.scene.shape.Shape) object.getShape();

        entity.setShape(Utils.toShape(sh.getClass().getName()));
        entity.setName(object.getName());
        entity.setDescription(object.getDescription());
        if (sh.getFill() != null)
                entity.setColor(sh.getFill().toString());
        entity.setLayer(1);
        entity.setMapID(mapID);

        return entity;
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

    public Node getShape() {
        return shape;
    }

    public void setShape(Shape shape) {
        this.shape = shape;
    }

    public int getLayer() {
        return layer;
    }

    public void setLayer(int layer) {
        this.layer = layer;
    }

    public double getArea() {
        String shapeName = shape.getClass().getName();
        switch (shapeName) {
            case "javafx.scene.shape.Circle": {
                Circle circle = (Circle) shape;
                return Math.pow(circle.getRadius(), 2)*3.14;
            }
            case "javafx.scene.shape.Rectangle": {
                Rectangle rectangle = (Rectangle) shape;
                return rectangle.getHeight()*rectangle.getWidth();
            }
            case "javafx.scene.shape.Ellipse": {
                Ellipse ellipse = (Ellipse) shape;
                return ellipse.getRadiusX()*ellipse.getRadiusY()*3.14;
            }
            case "javafx.scene.shape.Polygon": {
                //todo
                return shape.getScaleX();
//                Polygon polygon = (Polygon) shape;
//                return 0;
            }
            case "javafx.scene.shape.Line":
            case "javafx.scene.shape.Path": {
                return 0;
            }
        }
        return -1;
    }

    public double getPerimeter() {
        String shapeName = shape.getClass().getName();
        switch (shapeName) {
            case "javafx.scene.shape.Circle": {
                Circle circle = (Circle) shape;
                return 2*3.14*circle.getRadius();
            }
            case "javafx.scene.shape.Rectangle": {
                Rectangle rectangle = (Rectangle) shape;
                return 2*(rectangle.getHeight()+rectangle.getWidth());
            }
            case "javafx.scene.shape.Ellipse": {
                Ellipse ellipse = (Ellipse) shape;
                return 4*(ellipse.getRadiusX()*ellipse.getRadiusY()*3.14 + Math.pow((ellipse.getRadiusX()-ellipse.getRadiusY()), 2)) / (ellipse.getRadiusY() + ellipse.getRadiusX());
            }
            case "javafx.scene.shape.Polygon": {
                Polygon polygon = (Polygon) shape;
                List<Double> points = polygon.getPoints();
                //todo
                return 0;
            }
            case "javafx.scene.shape.Line": {
                Line line = (Line) shape;
                return Math.pow(line.getEndX() - line.getStartX(), 2) + Math.pow(line.getEndY() - line.getStartY(), 2);
            }
            case "javafx.scene.shape.Path": {
                //todo
                return 1;
            }
        }
        return -1;
    }
}
