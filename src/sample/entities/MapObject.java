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

    public MapObject() {

    }

    public sample.databasemanage.entity.MapObject toDbEntity(sample.entities.MapObject object, Integer mapID) {
        sample.databasemanage.entity.MapObject entity = new sample.databasemanage.entity.MapObject();
        javafx.scene.shape.Shape sh = (javafx.scene.shape.Shape) object.getShape();

        entity.setShape(Utils.toShape(sh.getClass().getName()));
        entity.setName(object.getName());
        entity.setDescription(object.getDescription());
        if (sh.getFill() != null)
                entity.setColor(sh.getFill().toString());
        entity.setLayer(object.getLayer());
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

    public int getArea() {
        String shapeName = shape.getClass().getName();
        double result = 0;

        switch (shapeName) {
            case "javafx.scene.shape.Circle": {
                Circle circle = (Circle) shape;
                result = Math.pow(circle.getRadius(), 2)*3.14;
                break;
            }
            case "javafx.scene.shape.Rectangle": {
                Rectangle rectangle = (Rectangle) shape;
                result = rectangle.getHeight()*rectangle.getWidth();
                break;
            }
            case "javafx.scene.shape.Ellipse": {
                Ellipse ellipse = (Ellipse) shape;
                result = ellipse.getRadiusX()*ellipse.getRadiusY()*3.14;
                break;
            }
            case "javafx.scene.shape.Polygon": {
                Polygon polygon = (Polygon) shape;
                double res = 0;
                List<Double> points = polygon.getPoints();
                double midSide = 0;
                for (int i = 0; i <= points.size()-3; i+= 2) {
                    midSide += Math.sqrt(Math.pow(points.get(i)-points.get(i+2), 2) + Math.pow(points.get(i+1)-points.get(i+3), 2));
                }
                midSide = midSide / (points.size() / 2);

                double sideAmount = points.size() / 2;
                res = sideAmount * Math.pow(midSide, 2) * (1.0 / Math.tan(Math.PI / sideAmount))/4;
                result = res;
                break;
            }
            case "javafx.scene.shape.Line":
            case "javafx.scene.shape.Path": {
                return 0;
            }
        }
        return (int) Math.round(result);
    }

    public int getPerimeter() {
        String shapeName = shape.getClass().getName();
        double result = 0;

        switch (shapeName) {
            case "javafx.scene.shape.Circle": {
                Circle circle = (Circle) shape;
                result = 2*3.14*circle.getRadius();
                break;
            }
            case "javafx.scene.shape.Rectangle": {
                Rectangle rectangle = (Rectangle) shape;
                result = 2*(rectangle.getHeight()+rectangle.getWidth());
                break;
            }
            case "javafx.scene.shape.Ellipse": {
                Ellipse ellipse = (Ellipse) shape;
                result = 4*(ellipse.getRadiusX()*ellipse.getRadiusY()*3.14 + Math.pow((ellipse.getRadiusX()-ellipse.getRadiusY()), 2)) / (ellipse.getRadiusY() + ellipse.getRadiusX());
                break;
            }
            case "javafx.scene.shape.Polygon": {
                Polygon polygon = (Polygon) shape;
                double res = 0;
                List<Double> points = polygon.getPoints();
                for (int i = 0; i <= points.size()-3; i+= 2) {
                    res += Math.sqrt(Math.pow(points.get(i)-points.get(i+2), 2) + Math.pow(points.get(i+1)-points.get(i+3), 2));
                }
                result = res;
                break;
            }
            case "javafx.scene.shape.Line":
            case "javafx.scene.shape.Polyline": {
                return 0;
            }
        }
        return (int) Math.round(result);
    }

    public int getLength() {
        String shapeName = shape.getClass().getName();
        double resLength = 0;
        switch (shapeName) {
            case "javafx.scene.shape.Polyline": {
                double result = 0;
                Polyline polyline = (Polyline) shape;
                List<Double> points = polyline.getPoints();
                for (int i = 0; i <= points.size()-3; i+= 2) {
                    result += Math.sqrt(Math.pow(points.get(i)-points.get(i+2), 2) + Math.pow(points.get(i+1)-points.get(i+3), 2));
                }
                resLength = result;
                break;
            }
            case "javafx.scene.shape.Line": {
                Line line = (Line) shape;
                resLength = Math.sqrt(Math.pow(line.getEndX() - line.getStartX(), 2) + Math.pow(line.getEndY() - line.getStartY(), 2));
                break;
            }
        }
        return (int) Math.round(resLength);
    }
}
