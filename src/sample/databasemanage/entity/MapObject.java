package sample.databasemanage.entity;

public class MapObject extends BaseEntity {
    private Integer mapID;
    private String name;
    private String description;
    private Shape shape;
    private String color;
    private int layer;

    public MapObject(Integer id) {
        super(id);
    }

    public MapObject(Integer id, Integer mapID, String name, String description, Shape shape, String color, int layer) {
        super(id);
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
