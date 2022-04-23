package sample;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sample.databasemanage.entity.Map;
import sample.databasemanage.repo.CoordinateRepo;
import sample.databasemanage.repo.MapObjectRepo;
import sample.databasemanage.repo.MapRepo;
import sample.databasemanage.repo.RadiusRepo;
import sample.entities.MapObject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;


public class Controller implements Initializable {

    private static Controller instance = new Controller();
    public static Controller getInstance(){
        return instance;
    }

    @FXML
    AnchorPane pane;
    @FXML
    Label coordsLbl;
    @FXML
    ImageView imgView;

    FileChooser fileChooser = new FileChooser();
    File file;
    BufferedImage img;

    //map data
    ArrayList<MapObject> mapObjects;
    Integer startShirota = 5;
    Integer startDolgota = 5;
    Integer endShirota = 0;
    Integer endDolgota = 0;

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

    public Double getStartX() {
        return startX;
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

    private Double startX = 0.0;
    private Double startY = 0.0;
    private Double endX = 0.0;
    private Double endY = 0.0;

    //repo
    CoordinateRepo coordinateRepo;
    MapRepo mapRepo;
    MapObjectRepo mapObjectRepo;
    RadiusRepo radiusRepo;

    public void openF(){
        fileChooser.setTitle("Choose your picture");
        Stage stage = new Stage();
        file = fileChooser.showOpenDialog(stage);
        try {
            try {
                //create new map with image from fileChooser
                img = ImageIO.read(new FileInputStream(file));
                imgView.setImage(null);
                imgView.setImage(SwingFXUtils.toFXImage(img,null));

                //creating list of objects
                mapObjects = new ArrayList<>();
            } catch (NullPointerException ignored){}
        } catch (IOException ignored){}
    }

    public boolean saveF() throws IOException {
        String saveChoice = "toDatabase";

        switch (saveChoice) {
            case "toDatabase": saveToDatabase(); break;
            case "toDevice": saveToDevice(); break;
        }
        return true;
    }

    public void manageCoords(){
        try {
            AnchorPane pane = FXMLLoader.load(CoordsController.class.getResource("coords.fxml"));
            Stage coordsStage = new Stage();
            coordsStage.setTitle("Введите координаты на карте");
            Scene scene = new Scene(pane);
            coordsStage.setScene(scene);
            coordsStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean saveToDatabase() throws IOException {
        mapObjects.add(new MapObject("obj", "descr", new Circle(4, 10, 10), 1));
        //save to database as map
        //save as new map
        Map map = new Map(1, startShirota, startDolgota, endShirota, endDolgota, startX, startY, endX, endY, file);
        map = mapRepo.insert(map);
        for (MapObject object: mapObjects) {
            sample.databasemanage.entity.MapObject obj = object.toDbEntity(object, map.getId());
            obj = mapObjectRepo.insert(obj);
        }
        return true;
    }

    public boolean saveToDevice() {
        //save to device as picture
        return true;
    }

    public void closeF() throws IOException {
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        try {
//            coordinateRepo = new CoordinateRepo();
//            mapObjectRepo = new MapObjectRepo();
//            radiusRepo = new RadiusRepo();
//            mapRepo = new MapRepo();
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }

    }

}
