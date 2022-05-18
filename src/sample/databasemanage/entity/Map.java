package sample.databasemanage.entity;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Optional;

public class Map extends BaseEntity {
    private byte[] image;
    private File fileImage;
    //54*60+40=минуты долготы
    private Integer startShirota;
    private Integer startDolgota;
    private Integer endShirota;
    private Integer endDolgota;
    private Double startX;
    private Double startY;
    private Double endX;
    private Double endY;

    public Double getStartX() {
        return startX;
    }

    public File getFileImage() {
        return fileImage;
    }

    public void setFileImage(File fileImage) {
        this.fileImage = fileImage;
    }

    public void setStartX(Double startX) {
        this.startX = startX;
    }

    public Double getStartY() {
        return startY;
    }

    public void setStartY(Double startY) {
        this.startY = startY;
    }

    public Double getEndX() {
        return endX;
    }

    public void setEndX(Double endX) {
        this.endX = endX;
    }

    public Double getEndY() {
        return endY;
    }

    public void setEndY(Double endY) {
        this.endY = endY;
    }

    public Map(Integer id, Integer startShirota, Integer startDolgota, Integer endShirota, Integer endDolgota, Double startX, Double startY, Double endX, Double endY, byte[] image) {
        super(id);
        this.image = image;
        this.startShirota = startShirota;
        this.startDolgota = startDolgota;
        this.endShirota = endShirota;
        this.endDolgota = endDolgota;
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
    }

    public Map(Integer id, Integer startShirota, Integer startDolgota, Integer endShirota, Integer endDolgota, Double startX, Double startY, Double endX, Double endY, File image) throws IOException {
        super(id);
        this.startShirota = startShirota;
        this.startDolgota = startDolgota;
        this.endShirota = endShirota;
        this.endDolgota = endDolgota;
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;

//        BufferedImage bImage = ImageIO.read(image);
//        ByteArrayOutputStream bos = new ByteArrayOutputStream();
//        ImageIO.write(bImage, getExtensionByStringHandling(image.getName()).toString(), bos );
//        byte [] data = bos.toByteArray();
//        this.image = data;
        this.fileImage = image;
    }

    public Optional<String> getExtensionByStringHandling(String filename) {
        return Optional.ofNullable(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(filename.lastIndexOf(".") + 1));
    }

    public Integer getStartShirota() {
        return startShirota;
    }

    public void setStartShirota(Integer startShirota) {
        this.startShirota = startShirota;
    }

    public Integer getStartDolgota() {
        return startDolgota;
    }

    public void setStartDolgota(Integer startDolgota) {
        this.startDolgota = startDolgota;
    }

    public Integer getEndShirota() {
        return endShirota;
    }

    public void setEndShirota(Integer endShirota) {
        this.endShirota = endShirota;
    }

    public Integer getEndDolgota() {
        return endDolgota;
    }

    public void setEndDolgota(Integer endDolgota) {
        this.endDolgota = endDolgota;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public void setImage(File image) throws IOException {
        BufferedImage bImage = ImageIO.read(image);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(bImage, "jpg", bos );
        byte [] data = bos.toByteArray();
    }

    public Map(Integer id, File image) throws IOException {
        super(id);

        BufferedImage bImage = ImageIO.read(image);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(bImage, "jpg", bos );
        byte [] data = bos.toByteArray();
        this.image = data;
    }
}
