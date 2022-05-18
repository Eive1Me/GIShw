package sample.databasemanage.entity;

public class Radius {
    private Integer id;
    private Integer objectID;
    private Double valueX;
    private Double valueY;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getValueY() {
        return valueY;
    }

    public void setValueY(Double valueY) {
        this.valueY = valueY;
    }

    public Radius(Integer id, Integer objectID, Double valueX, Double valueY) {
        this.id = id;
        this.objectID = objectID;
        this.valueX = valueX;
        this.valueY = valueY;
    }

    public Double getValueX() {
        return valueX;
    }

    public void setValueX(Double valueX) {
        this.valueX = valueX;
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }
}

