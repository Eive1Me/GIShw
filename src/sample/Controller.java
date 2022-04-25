package sample;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
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
import javafx.scene.paint.Color;
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
import java.util.List;
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

            File temp = new File("tempbg.jpg");

            try {
                ImageIO.write(img, "png", temp);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            file = temp;

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

    //TEST POLYLINE - DELETE
    Polygon debugPolygon;
    public boolean saveToDatabase() throws IOException {
        //filler debug data - delete
//        mapObjects.add(new MapObject("obj", "descr", new Rectangle(99, 88, 77, 66), 1));
//        mapObjects.add(new MapObject("obj2", "descr2", new Circle(99, 88, 77), 1));
//        mapObjects.add(new MapObject("obj3", "descr2", new Line(99, 88, 77, 10), 1));
//        mapObjects.add(new MapObject("obj4", "descr2", new Ellipse(99, 88, 77, 10), 1));
//        mapObjects.add(new MapObject("objPOLYgon", "POLYGON!!!!!!", debugPolygon, 1));
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
            else if (Utils.isPolyline(shape)) {
                Polyline sh = (Polyline) object.getShape();
                Double[] points = sh.getPoints().toArray(new Double[0]);
                for (int i = 0; i < points.length; i = i + 2) {
                    Coordinate coord = new Coordinate(1, obj.getId(), points[i], points[i+1]);
                    coordinateRepo.insert(coord);
                }
            }
            else if (Utils.isPolygon(shape)) {
                Polygon sh = (Polygon) object.getShape();
                Double[] points = sh.getPoints().toArray(new Double[0]);
                for (int i = 0; i < points.length; i = i + 2) {
                    Coordinate coord = new Coordinate(1, obj.getId(), points[i], points[i+1]);
                    coordinateRepo.insert(coord);
                }
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

    //==========================drawing obj==============================

    EventHandler<MouseEvent> mousePressedHandler = mouseEvent -> {},
                             mouseDraggedHandler = mouseEvent -> {},
                             mouseReleaseHandler = mouseEvent -> {},
                             mouseClickHandler = mouseEvent -> {};

    public void removeHandlers(){
        pane.removeEventHandler(MouseEvent.MOUSE_PRESSED,mousePressedHandler);
        pane.removeEventHandler(MouseEvent.MOUSE_DRAGGED,mouseDraggedHandler);
        pane.removeEventHandler(MouseEvent.MOUSE_RELEASED,mouseReleaseHandler);
        pane.removeEventHandler(MouseEvent.MOUSE_CLICKED,mouseClickHandler);
        pane.removeEventHandler(MouseEvent.MOUSE_PRESSED,mouseClickHandler);
    }

    public void line(){
        removeHandlers();
        final Coords[] start = new Coords[1];
        final Coords[] end = new Coords[1];
        final Line[] line = new Line[1];
        try {
            mousePressedHandler = event -> {
                start[0] = new Coords(event.getX(), event.getY());
                line[0] = new Line(start[0].x, start[0].y, start[0].x, start[0].y);
                pane.getChildren().add(line[0]);
            };

            mouseDraggedHandler = event -> {
                line[0].setEndX(event.getX());
                line[0].setEndY(event.getY());
            };

            mouseClickHandler = event -> {};

            mouseReleaseHandler = event -> {
                end[0] = new Coords(event.getX(), event.getY());
                line[0].setEndX(end[0].x);
                line[0].setEndY(end[0].y);
            };

            pane.addEventHandler(MouseEvent.MOUSE_PRESSED,mousePressedHandler);
            pane.addEventHandler(MouseEvent.MOUSE_DRAGGED,mouseDraggedHandler);
            pane.addEventHandler(MouseEvent.MOUSE_RELEASED,mouseReleaseHandler);
        } catch (IllegalArgumentException ignored){}
    }

    public void ellipsis(){
        removeHandlers();
        final Coords[] start = new Coords[1];
        final Coords[] end = new Coords[1];
        final Ellipse[] ellipses = new Ellipse[1];
        try {
            mousePressedHandler = event -> {
                ellipses[0] = new Ellipse();
                ellipses[0].setFill(Color.TRANSPARENT);
                ellipses[0].setStrokeWidth(1.0);
                ellipses[0].setStroke(Color.BLACK);
                start[0] = new Coords(event.getX(), event.getY());
                ellipses[0].setCenterX(start[0].x);
                ellipses[0].setCenterY(start[0].y);
                ellipses[0].setRadiusX(1.0);
                ellipses[0].setRadiusY(1.0);
                pane.getChildren().add(ellipses[0]);
            };

            mouseDraggedHandler = event -> {
                ellipses[0].setCenterX(start[0].x + start[0].xDist(new Coords(event.getX(),event.getY()))/2);
                ellipses[0].setCenterY(start[0].y + start[0].yDist(new Coords(event.getX(),event.getY()))/2);
                ellipses[0].setRadiusX(start[0].xDist(new Coords(event.getX(),event.getY()))/2);
                ellipses[0].setRadiusY(start[0].yDist(new Coords(event.getX(),event.getY()))/2);
            };

            mouseReleaseHandler = event -> {
                end[0] = new Coords(event.getX(), event.getY());
                ellipses[0].setRadiusX(start[0].xDist(end[0])/2);
                ellipses[0].setRadiusY(start[0].yDist(end[0])/2);
            };

            mouseClickHandler = event -> {
            };

            pane.addEventHandler(MouseEvent.MOUSE_PRESSED,mousePressedHandler);
            pane.addEventHandler(MouseEvent.MOUSE_DRAGGED,mouseDraggedHandler);
            pane.addEventHandler(MouseEvent.MOUSE_RELEASED,mouseReleaseHandler);
        } catch (IllegalArgumentException ignored){}
    }

    public void circle(){
        removeHandlers();
        final Coords[] start = new Coords[1];
        final Coords[] end = new Coords[1];
        final Circle[] circles = new Circle[1];
        try {
            mousePressedHandler = event -> {
                circles[0] = new Circle();
                circles[0].setFill(Color.TRANSPARENT);
                circles[0].setStrokeWidth(1.0);
                circles[0].setStroke(Color.BLACK);
                start[0] = new Coords(event.getX(), event.getY());
                circles[0].setRadius(1.0);
                circles[0].setCenterX(start[0].x);
                circles[0].setCenterY(start[0].y);
                pane.getChildren().add(circles[0]);
            };

            mouseDraggedHandler = event -> {
                circles[0].setCenterX(start[0].x + start[0].xDist(new Coords(event.getX(),event.getY()))/2);
                circles[0].setCenterY(start[0].y + start[0].yDist(new Coords(event.getX(),event.getY()))/2);
                circles[0].setRadius(start[0].distanceTo(new Coords(event.getX(),event.getY()))/2);
            };

            mouseReleaseHandler = event -> {
                end[0] = new Coords(event.getX(), event.getY());
                circles[0].setRadius(start[0].distanceTo(end[0])/2);
            };

            mouseClickHandler = event -> {
            };

            pane.addEventHandler(MouseEvent.MOUSE_PRESSED,mousePressedHandler);
            pane.addEventHandler(MouseEvent.MOUSE_DRAGGED,mouseDraggedHandler);
            pane.addEventHandler(MouseEvent.MOUSE_RELEASED,mouseReleaseHandler);
        } catch (IllegalArgumentException ignored){}
    }

    public void wierdLine(){
        removeHandlers();
        List<Coords> coordsArr = new ArrayList<>();
        final Polyline[] line = new Polyline[1];
        try {
            mousePressedHandler = event -> {};

            mouseDraggedHandler = event -> {};

            mouseClickHandler = event -> {
                if (event.isPrimaryButtonDown()) {
                    coordsArr.add(new Coords(event.getX(), event.getY()));
                    if (coordsArr.size() == 1){
                        line[0] = new Polyline();
                        pane.getChildren().add(line[0]);
                    }
                    line[0].getPoints().addAll(event.getX(), event.getY());


//                    System.out.println("_____POLYLINE SAVING");
//                    /////////////debug polyline saving
//                    debugPolyline = line[0];
//                    ///////////////////
                } else if (event.isSecondaryButtonDown()) {
                    coordsArr.clear();
                }
            };


            mouseReleaseHandler = event -> {};

            pane.addEventHandler(MouseEvent.MOUSE_PRESSED,mouseClickHandler);
        } catch (IllegalArgumentException ignored){}
    }

    public void rectangle(){
        removeHandlers();
        final Coords[] start = new Coords[1];
        final Coords[] end = new Coords[1];
        final Rectangle[] rectangles = new Rectangle[1];
        try {
            mousePressedHandler = event -> {
                rectangles[0] = new Rectangle();
                rectangles[0].setFill(Color.TRANSPARENT);
                rectangles[0].setStrokeWidth(1.0);
                rectangles[0].setStroke(Color.BLACK);
                start[0] = new Coords(event.getX(), event.getY());
                rectangles[0].setX(start[0].x);
                rectangles[0].setY(start[0].y);
                rectangles[0].setWidth(1.0);
                rectangles[0].setHeight(1.0);
                pane.getChildren().add(rectangles[0]);
            };

            mouseDraggedHandler = event -> {
                rectangles[0].setWidth(start[0].xDist(new Coords(event.getX(),event.getY())));
                rectangles[0].setHeight(start[0].yDist(new Coords(event.getX(),event.getY())));
            };

            mouseReleaseHandler = event -> {
                end[0] = new Coords(event.getX(), event.getY());
                rectangles[0].setWidth(start[0].xDist(end[0]));
                rectangles[0].setHeight(start[0].yDist(end[0]));
            };

            mouseClickHandler = event -> {
            };

            pane.addEventHandler(MouseEvent.MOUSE_PRESSED,mousePressedHandler);
            pane.addEventHandler(MouseEvent.MOUSE_DRAGGED,mouseDraggedHandler);
            pane.addEventHandler(MouseEvent.MOUSE_RELEASED,mouseReleaseHandler);
        } catch (IllegalArgumentException ignored){}
    }

    public void wierdRectangle(){
        removeHandlers();
        List<Coords> coordsArr = new ArrayList<>();
        final Polygon[] line = new Polygon[1];
        try {
            mousePressedHandler = event -> {};

            mouseDraggedHandler = event -> {};

            mouseClickHandler = event -> {
                if (event.isPrimaryButtonDown()) {
                    coordsArr.add(new Coords(event.getX(), event.getY()));
                    if (coordsArr.size() == 1){
                        line[0] = new Polygon();
                        line[0].setFill(Color.TRANSPARENT);
                        line[0].setStrokeWidth(1.0);
                        line[0].setStroke(Color.BLACK);
                        pane.getChildren().add(line[0]);
                    }
                    line[0].getPoints().addAll(event.getX(), event.getY());


                    System.out.println("_____POLYLINE SAVING");
                    /////////////debug polyline saving
                    debugPolygon = line[0];
                    ///////////////////
                } else if (event.isSecondaryButtonDown()) {
                    coordsArr.clear();
                }
            };

            mouseReleaseHandler = event -> {};

            pane.addEventHandler(MouseEvent.MOUSE_PRESSED,mouseClickHandler);
        } catch (IllegalArgumentException ignored){}
    }

    public void cursor(){
        removeHandlers();
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
        pane.addEventHandler(MouseEvent.MOUSE_PRESSED,mousePressedHandler);
        pane.addEventHandler(MouseEvent.MOUSE_DRAGGED,mouseDraggedHandler);
        pane.addEventHandler(MouseEvent.MOUSE_RELEASED,mouseReleaseHandler);
        pane.addEventHandler(MouseEvent.MOUSE_CLICKED,mouseClickHandler);
        imgView.fitWidthProperty().bind(pane.widthProperty());
        imgView.fitHeightProperty().bind(pane.heightProperty());
        pane.autosize();
    }

}
