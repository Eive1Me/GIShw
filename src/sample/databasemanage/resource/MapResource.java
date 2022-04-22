package sample.databasemanage.resource;

import sample.databasemanage.entity.Map;

public class MapResource extends BaseResource {
    private Integer id;
    private byte[] image;

    public MapResource(Integer id, byte[] image) {
        this.id = id;
        this.image = image;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

//    public Map toEntity() {
//        return new Map(id,
//                image);
//    }
}
