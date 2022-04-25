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
import javafx.scene.shape.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sample.databasemanage.entity.Coordinate;
import sample.databasemanage.entity.Map;
import sample.databasemanage.entity.Radius;
import sample.databasemanage.repo.CoordinateRepo;
import sample.databasemanage.repo.MapObjectRepo;
import sample.databasemanage.repo.MapRepo;
import sample.databasemanage.repo.RadiusRepo;
import sample.entities.MapObject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;


public class Controller implements Initializable {

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
                //create new map with image from filechooser
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

    public boolean openFromDatabase() throws IOException {
        //retrieve map image
        ArrayList<Map> maps = mapRepo.select();
        Map chosenMap = maps.get(19);

        //retrieve map data
        startShirota = chosenMap.getStartShirota();
        startDolgota = chosenMap.getStartDolgota();
        endShirota = chosenMap.getEndShirota();
        endDolgota = chosenMap.getEndDolgota();

        try {
            img = ImageIO.read(new ByteArrayInputStream(chosenMap.getImage()));
            imgView.setImage(null);
            imgView.setImage(SwingFXUtils.toFXImage(img,null));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //retrieve objects
        ArrayList<sample.databasemanage.entity.MapObject> mapObjectsDB = mapObjectRepo.selectByMapId(chosenMap.getId());
        mapObjects = new ArrayList<>();
        for (sample.databasemanage.entity.MapObject objDB: mapObjectsDB) {
            ArrayList<Coordinate> coords = coordinateRepo.selectByObjectId(objDB.getId());

            if (objDB.getShape() == sample.databasemanage.entity.MapObject.Shape.CIRCLE
            || objDB.getShape() == sample.databasemanage.entity.MapObject.Shape.ELLIPSE) {
                ArrayList<Radius> radius = radiusRepo.selectByObjectId(objDB.getId());
                mapObjects.add(objDB.toObject(coords, radius.get(0)));
            }
            else
                mapObjects.add(objDB.toObject(coords));
        }

        System.out.println(mapObjects.size());
        System.out.println(mapObjects.get(0).getName());
        System.out.println(mapObjects.get(0).getShape().getClass().getName());
        System.out.println(mapObjects.get(1).getName());
        System.out.println(mapObjects.get(1).getShape().getClass().getName());
        System.out.println(mapObjects.get(2).getName());
        System.out.println(mapObjects.get(2).getShape().getClass().getName());
        System.out.println(mapObjects.get(3).getName());
        System.out.println(mapObjects.get(3).getShape().getClass().getName());
        return true;
    }

    public Stage openObjectTable() throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource(
                        "map_object_table.fxml"
                )
        );

        Stage stage = new Stage(StageStyle.DECORATED);
        stage.setScene(
                new Scene(loader.load())
        );

        ObjectTableController controller = loader.getController();
        controller.initData(mapObjects);

        stage.show();

        return stage;
    }

    public boolean saveToDatabase() throws IOException {
        mapObjects.add(new MapObject("obj", "descr", new Rectangle(99, 88, 77, 66), 1));
        mapObjects.add(new MapObject("obj2", "descr2", new Circle(99, 88, 77), 1));
        mapObjects.add(new MapObject("obj3", "descr2", new Line(99, 88, 77, 10), 1));
        mapObjects.add(new MapObject("obj4", "descr2", new Ellipse(99, 88, 77, 10), 1));
        //save to database as map
        //save as new map
        Map map = new Map(1, startShirota, startDolgota, endShirota, endDolgota, startX, startY, endX, endY, file);
        map = mapRepo.insert(map);
        for (MapObject object: mapObjects) {
            sample.databasemanage.entity.MapObject obj = object.toDbEntity(object, map.getId());
            obj = mapObjectRepo.insert(obj);

            //insert coordinates and radiuses
            Shape shape = (Shape) object.getShape();
            if (Utils.isCircle(shape)) {
                Circle sh = (Circle) object.getShape();
                Coordinate objCoordinates = new Coordinate(1, obj.getId(), sh.getCenterX(), sh.getCenterY());
                objCoordinates = coordinateRepo.insert(objCoordinates);
                Radius radius = new Radius(1, obj.getId(), sh.getRadius(), sh.getRadius());
                radius = radiusRepo.insert(radius);
            }
            else if (Utils.isEllipse(shape)) {
                Ellipse sh = (Ellipse) object.getShape();
                Coordinate objCoordinates = new Coordinate(1, obj.getId(), sh.getCenterX(), sh.getCenterY());
                objCoordinates = coordinateRepo.insert(objCoordinates);
                Radius radius = new Radius(1, obj.getId(), sh.getRadiusX(), sh.getRadiusY());
                radius = radiusRepo.insert(radius);
            }
            else if (Utils.isLine(shape)) {
                Line sh = (Line) object.getShape();
                Coordinate objCoordinates = new Coordinate(1, obj.getId(), sh.getStartX(), sh.getStartY());
                coordinateRepo.insert(objCoordinates);
                objCoordinates = new Coordinate(1, obj.getId(), sh.getEndX(), sh.getEndY());
                coordinateRepo.insert(objCoordinates);
            }
            else if (Utils.isRectangle(shape)) {
                Rectangle sh = (Rectangle) object.getShape();
                Coordinate objCoord1 = new Coordinate(1, obj.getId(), sh.getX(), sh.getY());
                Coordinate objCoord2 = new Coordinate(1, obj.getId(), sh.getX() + sh.getWidth(), sh.getY());
                Coordinate objCoord3 = new Coordinate(1, obj.getId(), sh.getX(), sh.getY() - sh.getHeight());
                Coordinate objCoord4 = new Coordinate(1, obj.getId(), sh.getX() + sh.getWidth(), sh.getY() - sh.getHeight());
                coordinateRepo.insert(objCoord1);
                coordinateRepo.insert(objCoord2);
                coordinateRepo.insert(objCoord3);
                coordinateRepo.insert(objCoord4);
            }
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
        try {
            coordinateRepo = new CoordinateRepo();
            mapObjectRepo = new MapObjectRepo();
            radiusRepo = new RadiusRepo();
            mapRepo = new MapRepo();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

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
