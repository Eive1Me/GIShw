package sample.databasemanage.entity;

public class Radius extends BaseEntity{
    private Integer objectID;
    private Double X;
    private Double Y;
    private Double value;

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Radius(Integer id, Integer objectID, Double X, Double Y, Double value) {
        super(id);
        this.objectID = objectID;
        this.X = X;
        this.Y = Y;
        this.value = value;
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
        this.X = x;
    }

    public Double getY() {
        return Y;
    }

    public void setY(Double y) {
        this.Y = y;
    }
}
