package sample.databasemanage.entity;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class Map extends BaseEntity {
    private byte[] image;

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

    public Map(Integer id, byte[] image) {
        super(id);
        this.image = image;
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
