package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

public class CoordsController {
    @FXML
    Label llCrdsLblb;
    @FXML
    Label urCrdsLblb;

    public void onLCanvasClick(MouseEvent e){
        if (e.isPrimaryButtonDown()) {
            Coords coords = new Coords(e.getSceneX(), e.getSceneY());
            llCrdsLblb.setText(coords.toString());
        } else if (e.isSecondaryButtonDown()){
            Coords coords = new Coords(e.getSceneX(), e.getSceneY());
            urCrdsLblb.setText(coords.toString());
        }
    }

    public void onRCanvasClick(MouseEvent e){
    }
}
