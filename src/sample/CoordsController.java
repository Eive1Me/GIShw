package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class CoordsController {
    @FXML
    Label llCrdsLbl;
    @FXML
    Label urCrdsLbl;

    public void onCanvasClick(MouseEvent e){
        if (e.getButton() == MouseButton.PRIMARY) {
            System.out.println("1");
            Coords coords = new Coords(e.getSceneX(), e.getSceneY());
            llCrdsLbl.setText(coords.toString());
        } else if (e.getButton() == MouseButton.SECONDARY){
            System.out.println("2");
            Coords coords = new Coords(e.getSceneX(), e.getSceneY());
            urCrdsLbl.setText(coords.toString());
        }
    }

    public void confirmCoords(){
        Controller controller = Controller.getInstance();

    }
}
