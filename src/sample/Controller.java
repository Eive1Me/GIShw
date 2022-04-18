package sample;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
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
import java.util.Optional;


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

    public boolean saveF(){
        return true;
    }

    public void closeF(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Закрытие карты");
        alert.setHeaderText("Сохранить карту?");
        ButtonType ye = new ButtonType("Да");
        ButtonType nyo = new ButtonType("Нет");
        ButtonType goBack = new ButtonType("Отмена");
        alert.getButtonTypes().clear();
        alert.getButtonTypes().addAll(ye,nyo,goBack);

        Optional<ButtonType> option = alert.showAndWait();

        if (option.isPresent()) {
            if (option.get() == ye) {
                if (saveF()) imgView.setImage(null);
            } else if (option.get() == nyo) {
                imgView.setImage(null);
            }
        }
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
