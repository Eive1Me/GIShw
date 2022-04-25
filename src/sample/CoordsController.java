package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class CoordsController {
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
        Controller controller = Controller.getInstance();
        controller.setStartShirota(Integer.parseInt(llshirota1.getText())*60 + Integer.parseInt(llshirota2.getText()));
        controller.setStartDolgota(Integer.parseInt(lldolgota1.getText())*60 + Integer.parseInt(lldolgota2.getText()));
        controller.setEndShirota(Integer.parseInt(urshirota1.getText())*60 + Integer.parseInt(urshirota2.getText()));
        controller.setEndDolgota(Integer.parseInt(urdolgota1.getText())*60 + Integer.parseInt(urdolgota2.getText()));
        controller.setStartX(llC.x);
        controller.setStartY(llC.y);
        controller.setEndX(urC.x);
        controller.setEndY(urC.y);
        Stage stage = (Stage) llCrdsLbl.getScene().getWindow();
        stage.close();
    }
}
