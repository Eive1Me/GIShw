package sample;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.shape.Shape;
import javafx.util.Callback;
import sample.entities.MapObject;

import java.util.ArrayList;

public class ObjectTableController {
    @FXML
    TableView table;
    @FXML
    TableColumn name;
    @FXML
    TableColumn description;
    @FXML
    TableColumn shape;
    @FXML
    TableColumn color;
    @FXML
    TableColumn layer;
    @FXML
    TableColumn area;
    @FXML
    TableColumn perimeter;
    public void initData(ArrayList<MapObject> data) {
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        description.setCellValueFactory(new PropertyValueFactory<>("description"));
        shape.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<MapObject, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<MapObject, String> p) {
                if (p.getValue() != null) {
                    return new SimpleStringProperty(Utils.toShape(p.getValue().getShape().getClass().getName()).toString());
                } else {
                    return new SimpleStringProperty("<no name>");
                }
            }
        });
        color.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<MapObject, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<MapObject, String> p) {
                if (p.getValue() != null) {
                    if (Utils.toShape(p.getValue().getShape().getClass().getName()) != sample.databasemanage.entity.MapObject.Shape.LINE) {
                        Shape shape = (Shape) p.getValue().getShape();
                        return new SimpleStringProperty(shape.getFill().toString());
                    }
                    return new SimpleStringProperty("<no name>");
                } else {
                    return new SimpleStringProperty("<no name>");
                }
            }
        });
        layer.setCellValueFactory(new PropertyValueFactory<>("layer"));

        ObservableList<MapObject> list = FXCollections.observableArrayList(data);
        table.setItems(list);
    }
}
