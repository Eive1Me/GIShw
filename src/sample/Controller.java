package sample;

import com.sun.xml.internal.ws.api.model.wsdl.WSDLOutput;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.*;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Rotate;
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
import java.util.stream.Collectors;


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
    @FXML
    ColorPicker colorPicker;
    @FXML
    ToolBar toolBar;

   EventHandler<MouseEvent> getDetailsEvent(MapObject object, String name, String desc) {
       Alert al = new Alert(Alert.AlertType.NONE);
       return new EventHandler<MouseEvent>() {
           @Override
           public void handle(MouseEvent mouseEvent) {
               al.setAlertType(Alert.AlertType.CONFIRMATION);
               al.setHeaderText(name);
               String length = object.getShape().getClass().toString().toLowerCase().contains("line") ? "\nДлина: " + object.getLength() : "";
               String perimeter = object.getShape().getClass().toString().toLowerCase().contains("line") ? "" : "\nПериметр: " + object.getPerimeter();
               String area = object.getShape().getClass().toString().toLowerCase().contains("line") ? "" : "\nПлощадь: " + object.getArea();
               al.setContentText(desc + length + perimeter + area);
               al.show();
           }
       };
   }
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
                clearObjects();
                toolBar.setVisible(true);
            } catch (NullPointerException ignored){}
        } catch (IOException ignored){}
    }

    public boolean saveF() throws IOException {
        if (imgView.getImage() == null) {
            throwNoMapError();
            return false;
        }
        else {
            String saveChoice = "toDatabase";

            switch (saveChoice) {
                case "toDatabase":
                    saveToDatabase();
                    break;
                case "toDevice":
                    saveToDevice();
                    break;
            }
            return true;
        }
    }

    public void clearObjects() {
//        ArrayList<Node> objects = new ArrayList<>();
//        for (Node c: pane.getChildren()) {
//            if (!c.getClass().getName().contains("ImageView")) {
//                objects.add(c);
//            }
//        }
//        pane.getChildren().removeAll(objects);
        pane.getChildren().removeAll(pane.getChildren().stream().filter(c -> !c.getClass().getName().contains("ImageView")).collect(Collectors.toList()));
    }

    public void chooseMapFromDatabase() throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource(
                        "db_maps.fxml"
                )
        );

        Stage stage = new Stage(StageStyle.DECORATED);
        stage.setScene(
                new Scene(loader.load())
        );

        DbMapsController controller = loader.getController();
        controller.initData(mapRepo.select(), mapObjectRepo);

        stage.showAndWait();

        if (controller.openMap) openFromDatabase(controller.chosenMapId);
    }

    public boolean openFromDatabase(Integer chosenMapId) throws IOException {
        clearObjects();
        //retrieve map image
        ArrayList<Map> maps = mapRepo.select();
        Map chosenMap = maps.get(chosenMapId-1);

        //retrieve map data
        startShirota = chosenMap.getStartShirota();
        startDolgota = chosenMap.getStartDolgota();
        endShirota = chosenMap.getEndShirota();
        endDolgota = chosenMap.getEndDolgota();

        startX = chosenMap.getStartX();
        startY = chosenMap.getStartY();
        endX = chosenMap.getEndX();
        endY = chosenMap.getEndY();

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
        System.out.println(mapObjectsDB);
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

        drawObjects();
        toolBar.setVisible(true);
        return true;
    }

    public void drawObjects(){
        for (MapObject a:mapObjects) {
            Shape shape = (Shape) a.getShape();
//            shape.setFill(Color.TRANSPARENT);
            shape.setStrokeWidth(1.0);
            shape.setStroke(Color.BLACK);
            shape.setOnMouseClicked(getDetailsEvent(a, a.getName(), a.getDescription()));
            pane.getChildren().add(shape);
            System.out.println("drawn objects");
        }
    }

    public Stage openObjectTable() throws IOException {
        if (imgView.getImage() == null) {
            throwNoMapError();
            return null;
        }
        else {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(
                            "map_object_table.fxml"
                    )
            );

            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setMinWidth(800);
            stage.setMaxHeight(600);
            stage.setScene(
                    new Scene(loader.load())
            );

            ObjectTableController controller = loader.getController();
            controller.initData(mapObjects, this);

            stage.show();

            return stage;
        }
    }

    public void deleteObject(MapObject object) {
        mapObjects.remove(object);
        pane.getChildren().remove(object.getShape());
        System.out.println("deleted successfully");
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
//        mapObjects.add(new MapObject("obj", "descr", new Rectangle(99, 88, 77, 66), 1));
//        mapObjects.add(new MapObject("obj2", "descr2", new Circle(99, 88, 77), 1));
//        mapObjects.add(new MapObject("obj3", "descr2", new Line(99, 88, 77, 10), 1));
//        mapObjects.add(new MapObject("obj4", "descr2", new Ellipse(99, 88, 77, 10), 1));
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
                Coordinate objCoord3 = new Coordinate(1, obj.getId(), sh.getX(), sh.getY() + sh.getHeight());
                Coordinate objCoord4 = new Coordinate(1, obj.getId(), sh.getX() + sh.getWidth(), sh.getY() + sh.getHeight());
                coordinateRepo.insert(objCoord1);
                coordinateRepo.insert(objCoord2);
                coordinateRepo.insert(objCoord3);
                coordinateRepo.insert(objCoord4);
            }
            else if (Utils.isPolyline(shape)) {
                Polyline sh = (Polyline) object.getShape();
                Double[] points = sh.getPoints().toArray(new Double[0]);
                for (int i = 0; i < points.length; i+=2) {
                    Coordinate objCoord = new Coordinate(1, obj.getId(), points[i], points[i+1]);
                    coordinateRepo.insert(objCoord);
                }
            }
            else if (Utils.isPolygon(shape)) {
                Polygon sh = (Polygon) object.getShape();
                Double[] points = sh.getPoints().toArray(new Double[0]);
                for (int i = 0; i < points.length; i+=2) {
                    Coordinate objCoord = new Coordinate(1, obj.getId(), points[i], points[i+1]);
                    coordinateRepo.insert(objCoord);
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

        if (imgView .getImage() == null) {
            throwNoMapError();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Закрытие карты");
            alert.setHeaderText("Сохранить карту?");
            ButtonType ye = new ButtonType("Да");
            ButtonType nyo = new ButtonType("Нет");
            ButtonType goBack = new ButtonType("Отмена");
            alert.getButtonTypes().clear();
            alert.getButtonTypes().addAll(ye, nyo, goBack);

            Optional<ButtonType> option = alert.showAndWait();

            if (option.isPresent()) {
                if (option.get() == ye) {
                    if (saveF()) imgView.setImage(null);
                } else if (option.get() == nyo) {
                    imgView.setImage(null);
                }
            }
            clearObjects();
            toolBar.setVisible(false);
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

                line[0].setStrokeWidth(3);

                String[] result;
                result = getValues("Линия","Расстояние");
                MapObject a = addToList(result[0],result[1],line[0]);

                //on click show details
                line[0].setOnMouseClicked(getDetailsEvent(a, result[0], result[1]));
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
//                ellipses[0].setFill(Color.TRANSPARENT);
                ellipses[0].setFill(colorPicker.getValue());
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
                String[] result;
                result = getValues("Озеро","Территория");
                MapObject m = addToList(result[0],result[1],ellipses[0]);

                //on click show details
                ellipses[0].setOnMouseClicked(getDetailsEvent(m, result[0], result[1]));
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
//                circles[0].setFill(Color.TRANSPARENT);
                circles[0].setFill(colorPicker.getValue());
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
                String[] result;
                result = getValues("Круг","Территория");
                MapObject m = addToList(result[0],result[1],circles[0]);

                //on click show details
                circles[0].setOnMouseClicked(getDetailsEvent(m, result[0], result[1]));
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

                        line[0].setStrokeWidth(3);

                        pane.getChildren().add(line[0]);
                    }
                    line[0].getPoints().addAll(event.getX(), event.getY());
                } else if (event.isSecondaryButtonDown()) {
                    coordsArr.clear();
                    String[] result;
                    result = getValues("Путь","Дорога");
                    MapObject m = addToList(result[0],result[1],line[0]);

                    //on click show details
                    line[0].setOnMouseClicked(getDetailsEvent(m, result[0], result[1]));
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
//                rectangles[0].setFill(Color.TRANSPARENT);
                rectangles[0].setFill(colorPicker.getValue());
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
                String[] result;
                result = getValues("Прямоугольник","Здание");
                MapObject m = addToList(result[0],result[1],rectangles[0]);

                //on click show details
                rectangles[0].setOnMouseClicked(getDetailsEvent(m, result[0], result[1]));
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
//                        line[0].setFill(Color.TRANSPARENT);
                        line[0].setFill(colorPicker.getValue());
                        line[0].setStrokeWidth(1.0);
                        line[0].setStroke(Color.BLACK);
                        pane.getChildren().add(line[0]);
                    }
                    line[0].getPoints().addAll(event.getX(), event.getY());
                } else if (event.isSecondaryButtonDown()) {
                    coordsArr.clear();
                    String[] result;
                    result = getValues("Многоугольник","Территория");
                    MapObject m = addToList(result[0],result[1],line[0]);

                    //on click show details
                    line[0].setOnMouseClicked(getDetailsEvent(m, result[0], result[1]));
                }
            };

            mouseReleaseHandler = event -> {};

            pane.addEventHandler(MouseEvent.MOUSE_PRESSED,mouseClickHandler);
        } catch (IllegalArgumentException ignored){}
    }

    public void cursor(){
        removeHandlers();
    }

    public String[] getValues(String defName, String defDesc){
        String[] result = new String[2];
        TextInputDialog nameDialog = new TextInputDialog(defName);

        nameDialog.setTitle("Имя");
        nameDialog.setHeaderText("Введите имя объекта:");
        nameDialog.setContentText("Имя:");

        Optional<String> name = nameDialog.showAndWait();
        result[0] = name.toString();

        TextInputDialog dialog = new TextInputDialog(defDesc);

        dialog.setTitle("Описание");
        dialog.setHeaderText("Введите описание объекта:");
        dialog.setContentText("Описание:");

        Optional<String> desc = dialog.showAndWait();
        result[1] = desc.toString();

        return result;
    }

    public MapObject addToList(String name, String desc, Shape shape){
        MapObject m = new MapObject(name,desc,shape,1);
        mapObjects.add(m);
        return m;
    }

    double lastX, lastY;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        pane.setOnMousePressed((MouseEvent event) -> {
//            lastX = event.getX();
//            lastY = event.getY();
//            toFront();
            //  postView.toFront();

//            pane.setScaleX(1.2);
//            pane.setScaleY(1.2);
        });

        toolBar.setVisible(false);
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

    public void throwNoMapError() {
        final Alert invalidDataAlert = new Alert(Alert.AlertType.ERROR);

        final String message = "Сначала откройте карту или создайте новую.";

        invalidDataAlert.setTitle("Ошибка");
        invalidDataAlert.setHeaderText("Нет открытой карты");
        invalidDataAlert.setResizable(true);
        invalidDataAlert.setContentText(message);
        invalidDataAlert.showAndWait();
    }

    public void shortestRoad() {
        if (imgView.getImage() == null) {
            throwNoMapError();
        }
        else {
            ArrayList<MapObject> roads = (ArrayList<MapObject>) mapObjects.clone();
            roads.removeIf(a -> !a.getShape().getClass().getName().toLowerCase().contains("line"));
            if (roads.size() == 0) {
                statisticsAlert("Нет дорог", "На этой карте не найдено дорог!");
            }
            else {
                MapObject shortestRoad = roads.get(0);
                for (MapObject road : roads) {
                    if (road.getLength() < shortestRoad.getLength())
                        shortestRoad = road;
                }

                statisticsAlert("Самая короткая дорога", shortestRoad.getName() + ", длина: " + shortestRoad.getLength());
            }
        }
    }

    public void longestRoad() {
        if (imgView.getImage() == null) {
            throwNoMapError();
        }
        else {
            ArrayList<MapObject> roads = (ArrayList<MapObject>) mapObjects.clone();
            roads.removeIf(a -> !a.getShape().getClass().getName().toLowerCase().contains("line"));
            if (roads.size() == 0) {
                statisticsAlert("Нет дорог", "На этой карте не найдено дорог!");
            }
            else {
                MapObject shortestRoad = roads.get(0);
                for (MapObject road : roads) {
                    if (road.getLength() > shortestRoad.getLength())
                        shortestRoad = road;
                }

                statisticsAlert("Самая длинная дорога", shortestRoad.getName() + ", длина: " + shortestRoad.getLength());
            }
        }
    }

    public void allRoads() {
        if (imgView.getImage() == null) {
            throwNoMapError();
        }
        else {
            ArrayList<MapObject> roads = (ArrayList<MapObject>) mapObjects.clone();
            roads.removeIf(a -> !a.getShape().getClass().getName().toLowerCase().contains("line"));
            if (roads.size() == 0) {
                statisticsAlert("Нет дорог", "На этой карте не найдено дорог!");
            }
            else {
                double sum = 0;
                for (MapObject road : roads) {
                    sum += road.getLength();
                }

                statisticsAlert("Сумма длин всех дорог", Double.toString(sum));
            }
        }
    }

    public void biggestLake() {
        if (imgView.getImage() == null) {
            throwNoMapError();
        }
        else {
            ArrayList<MapObject> lakes = (ArrayList<MapObject>) mapObjects.clone();
            lakes.removeIf(a -> !a.getName().toLowerCase().contains("озеро") && !a.getDescription().toLowerCase().contains("озеро"));
            if (lakes.size() == 0) {
                statisticsAlert("Нет озер", "На этой карте не найдено озер!");
            }
            else {
                MapObject biggestLake = lakes.get(0);
                for (MapObject lake : lakes) {
                    if (lake.getArea() > biggestLake.getArea())
                        biggestLake = lake;
                }

                statisticsAlert("Самое большое озеро", biggestLake.getName() + ", площадь: " + biggestLake.getArea());
            }
        }
    }

    public void statisticsAlert(String header, String content) {
        final Alert invalidDataAlert = new Alert(Alert.AlertType.INFORMATION);

        invalidDataAlert.setTitle("Статистика");
        invalidDataAlert.setHeaderText(header);
        invalidDataAlert.setResizable(true);
        invalidDataAlert.setContentText(content);
        invalidDataAlert.showAndWait();
    }

    String windDirection = "Ю";
    public double[] getAffectedAreaInput(double defWind, double defDepth){
        double[] result = new double[2];
        TextInputDialog nameDialog = new TextInputDialog(Double.toString(defWind));

        nameDialog.setTitle("Скорость ветра");
        nameDialog.setHeaderText("Введите скорость ветра:");
//        nameDialog.setContentText("Имя:");

        Optional<String> wind = nameDialog.showAndWait();
        result[0] = Double.parseDouble(wind.get());

        TextInputDialog dialog = new TextInputDialog(Double.toString(defDepth));

        dialog.setTitle("Глубина заражения");
        dialog.setHeaderText("Введите глубину заражения:");

        Optional<String> depth = dialog.showAndWait();
        result[1] = Double.parseDouble(depth.get());

        if (result[0] > 0.5) {
            TextInputDialog dialog3 = new TextInputDialog("Ю");

            dialog3.setTitle("Направление ветра");
            dialog3.setHeaderText("Введите направление ветра:");

            Optional<String> windDir = dialog3.showAndWait();
            windDirection = windDir.get();
        }

        return result;
    }

    public void affectedArea() {
        removeHandlers();
        mouseClickHandler = event -> {
          Coords coords = new Coords(event.getX(), event.getY());
          double[] input = getAffectedAreaInput(1, 100);

          if (input[0] <= 0.5) {
              Circle posArea = new Circle();
              posArea.setCenterX(coords.x);
              posArea.setCenterY(coords.y);
              posArea.setRadius(input[1]);
              posArea.setStrokeWidth(1.0);
              posArea.setStroke(Color.BLACK);
              Image map = new Image(new File("C:/My stuff/IDEAProjects/gis/simple/src/sample/bg.png").toURI().toString());
              ImagePattern pattern = new ImagePattern(map, 20, 20, 40, 40, false);
              posArea.setOpacity(0.6);
              posArea.setFill(pattern);
              posArea.setId("affectedArea");

              pane.getChildren().add(posArea);
          }
          else {
              Path path = new Path();
              Image map = new Image(new File("src/sample/bg.png").toURI().toString());
              ImagePattern pattern = new ImagePattern(map, 20, 20, 40, 40, false);
              path.setOpacity(0.6);
              path.setFill(pattern);
              path.setStroke(Color.BLACK);
              path.setFillRule(FillRule.EVEN_ODD);

              if (input[0] > 0.5 && input[0] <= 1) {
                  MoveTo moveTo = new MoveTo();
                  moveTo.setX(coords.x + input[1]);
                  moveTo.setY(coords.y);

                  ArcTo arcToInner = new ArcTo();
                  arcToInner.setX(coords.x - input[1]);
                  arcToInner.setY(coords.y);
                  arcToInner.setRadiusX(input[1]);
                  arcToInner.setRadiusY(input[1]);

                  path.getElements().add(moveTo);
                  path.getElements().add(arcToInner);
              }
              else if (input[0] > 1.0) {
                  MoveTo moveTo = new MoveTo();
                  moveTo.setX(coords.x);
                  moveTo.setY(coords.y);

                  LineTo lineTo = new LineTo();
                  ArcTo arcToInner = new ArcTo();

                  if (input[0] <= 2) {
                      lineTo.setX(coords.x - (input[1]/2));
                      lineTo.setY(coords.y - input[1]);

                      arcToInner.setX(coords.x + (input[1]/2));
                      arcToInner.setY(coords.y - input[1]);
                  }
                  else {
                      lineTo.setX(coords.x - (input[1]/4));
                      lineTo.setY(coords.y - input[1]);

                      arcToInner.setX(coords.x + (input[1]/4));
                      arcToInner.setY(coords.y - input[1]);
                  }

                  arcToInner.setRadiusX(input[1]*2);
                  arcToInner.setRadiusY(input[1]*2);

                  path.getElements().add(moveTo);
                  path.getElements().add(lineTo);
                  path.getElements().add(arcToInner);
              }
              double angle = 0;
              switch (windDirection) {
                  case "Ю": {angle = 180; break;}
                  case "В": {angle = 90; break;}
                  case "З": {angle = -90; break;}
                  case "ЮЗ": {angle = 225; break;}
                  case "ЮВ": {angle = 135; break;}
                  case "СЗ": {angle = -45; break;}
                  case "СВ": {angle = 45; break;}
              }
              //Adding the transformation to rectangle
              path.getTransforms().add(new Rotate(angle, coords.x, coords.y));
              pane.getChildren().add(path);
          }
        };
        pane.addEventHandler(MouseEvent.MOUSE_PRESSED,mouseClickHandler);
    }
}
