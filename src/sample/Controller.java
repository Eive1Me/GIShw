package sample;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;


public class Controller {

    @FXML
    AnchorPane pane;
    @FXML
    Label coordsLbl;
    @FXML
    ImageView imgView;

    FileChooser fileChooser = new FileChooser();
    File file;
    BufferedImage img;

    public void openF(){
        fileChooser.setTitle("Choose your picture");
        Stage stage = new Stage();
        file = fileChooser.showOpenDialog(stage);
        try {
            try {
                img = ImageIO.read(new FileInputStream(file));
                imgView.setImage(null);
                imgView.setImage(SwingFXUtils.toFXImage(img,null));
            } catch (NullPointerException ignored){}
        } catch (IOException ignored){}
    }

    public void saveF(){

    }

    public void closeF(){
        imgView.setImage(null);
    }

    public void setCoords(MouseEvent e){
        coordsLbl.setText(new Coords(e.getSceneX(), e.getSceneY()-25).toString());
    }

    public static class Coords{
        Double x;
        Double y;

        Coords(Double x, Double y){
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {
            return x + "; " + y;
        }
    }
}
