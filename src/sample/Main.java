package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import sample.databasemanage.entity.MapObject;
import sample.databasemanage.repo.MapObjectRepo;

import java.sql.SQLException;
import java.util.ArrayList;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("GIS");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();


    }


    public static void main(String[] args) throws SQLException {


//        MapObjectRepo repo = new MapObjectRepo();
//        MapObject object = repo.insert(new MapObject(1, 1, "name", "desc", MapObject.Shape.ELLIPSE, "#456445", 1));
//        System.out.println(object.getId() + object.getName());
//        MapObject m = new MapObject("", "", new Rectangle(3, 12, Color.GRAY), 1);
//        System.out.println(m.getArea());
        launch(args);
    }
}
