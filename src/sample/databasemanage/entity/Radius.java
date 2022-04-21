package sample.databasemanage.entity;

public class Radius extends BaseEntity{
    private Integer objectID;
    private Double radX;
    private Double radY;

    public Radius(Integer id, Integer objectID, Double radX, Double radY) {
        super(id);
        this.objectID = objectID;
        this.radX = radX;
        this.radY = radY;
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
}
