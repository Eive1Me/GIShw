package sample;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

import static sample.Controller.img2;

public class CoordsController implements Initializable {
    @FXML
    Label llCrdsLbl;
    @FXML
    Label urCrdsLbl;
    @FXML
    TextField llshirota1;
    @FXML
    TextField llshirota2;
    @FXML
    TextField lldolgota1;
    @FXML
    TextField lldolgota2;
    @FXML
    TextField urshirota1;
    @FXML
    TextField urshirota2;
    @FXML
    TextField urdolgota1;
    @FXML
    TextField urdolgota2;
    @FXML
    ImageView imgView;

    Coords llC = new Coords(0.0,0.0);
    Coords urC = new Coords(600.0,400.0);

    public void onCanvasClick(MouseEvent e){
        if (e.getButton() == MouseButton.PRIMARY) {
            System.out.println("1");
            llC = new Coords(e.getSceneX(), e.getSceneY());
            llCrdsLbl.setText(llC.toString());
        } else if (e.getButton() == MouseButton.SECONDARY){
            System.out.println("2");
            urC = new Coords(e.getSceneX(), e.getSceneY());
            urCrdsLbl.setText(urC.toString());
        }
    }

    public void confirmCoords(){

        Controller.coordsMas = new double[]{Double.parseDouble(llshirota1.getText())*60 + Double.parseDouble(llshirota2.getText()),
                                            Double.parseDouble(lldolgota1.getText())*60 + Double.parseDouble(lldolgota2.getText()),
                                            Double.parseDouble(urshirota1.getText())*60 + Double.parseDouble(urshirota2.getText()),
                                            Double.parseDouble(urdolgota1.getText())*60 + Double.parseDouble(urdolgota2.getText()),
                                            llC.x,
                                            llC.y,
                                            urC.x,
                                            urC.y};

        Stage stage = (Stage) llCrdsLbl.getScene().getWindow();
        stage.close();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        imgView.setImage(SwingFXUtils.toFXImage(img2,null));
    }
}
