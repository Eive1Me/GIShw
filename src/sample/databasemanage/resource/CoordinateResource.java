package sample.databasemanage.resource;

import sample.databasemanage.entity.Coordinate;

public class CoordinateResource extends BaseResource{
    private Integer id;
    private Integer objectID;
    private Double X;
    private Double Y;

    public Coordinate toEntity() {
        return new Coordinate(id, objectID, X, Y);
    }

    public CoordinateResource(Integer id, Integer objectID, Double x, Double y) {
        this.id = id;
        this.objectID = objectID;
        X = x;
        Y = y;
    }

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

    public Double getX() {
        return X;
    }

    public void setX(Double x) {
        X = x;
    }

    public Double getY() {
        return Y;
    }

    public void setY(Double y) {
        Y = y;
    }
}
