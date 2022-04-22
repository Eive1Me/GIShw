package sample.databasemanage.resource;

import sample.databasemanage.entity.Radius;

public class RadiusResource extends BaseResource{
    private Integer id;
    private Integer objectID;
    private Double radX;
    private Double radY;

//    public Radius toEntity() {
//        return new Radius(id, objectID, radX, radY);
//    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Double getRadX() {
        return radX;
    }

    public void setRadX(Double radX) {
        this.radX = radX;
    }

    public Double getRadY() {
        return radY;
    }

    public void setRadY(Double radY) {
        this.radY = radY;
    }

    public RadiusResource(Integer id, Integer objectID, Double radX, Double radY) {
        this.id = id;
        this.objectID = objectID;
        this.radX = radX;
        this.radY = radY;
    }
}
