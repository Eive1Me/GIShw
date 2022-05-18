package sample.databasemanage.entity;

public class Coordinate extends BaseEntity{
    private Integer objectID;
    private Double X;
    private Double Y;

    public Coordinate(Integer id, Integer objectID, Double x, Double y) {
        super(id);
        this.objectID = objectID;
        X = x;
        Y = y;
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
