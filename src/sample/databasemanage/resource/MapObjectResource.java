package sample.databasemanage.resource;

import sample.databasemanage.entity.MapObject;

public class MapObjectResource extends BaseResource{
    private Integer id;
    private Integer mapID;
    private String name;
    private String description;
    private MapObject.Shape shape;
    private String color;
    private int layer;

    public MapObject toEntity() {
        return new MapObject(id,
                            mapID,
                            name,
                            description,
                            shape,
                            color,
                            layer);
    }

    public MapObjectResource(Integer id, Integer mapID, String name, String description, MapObject.Shape shape, String color, int layer) {
        this.id = id;
        this.mapID = mapID;
        this.name = name;
        this.description = description;
        this.shape = shape;
        this.color = color;
        this.layer = layer;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public MapObject.Shape getShape() {
        return shape;
    }

    public void setShape(MapObject.Shape shape) {
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
}
