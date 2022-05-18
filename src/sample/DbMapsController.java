package sample;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import sample.databasemanage.entity.Map;
import sample.databasemanage.repo.MapObjectRepo;

import javax.swing.text.View;
import java.util.ArrayList;

public class DbMapsController {
    @FXML
    TableView table;
    @FXML
    TableColumn map;
    @FXML
    TableColumn object_amount;

    MapObjectRepo mapObjectRepo;

    Integer chosenMapId;
    public boolean openMap = true;

    public void cancel() {
        openMap = false;
        Stage stage = (Stage) this.table.getScene().getWindow();
        stage.close();
    }

    public void chooseMap() {
        Stage stage = (Stage) this.table.getScene().getWindow();
        stage.close();
    }

    public void initData(ArrayList<Map> data, MapObjectRepo repo) {
        mapObjectRepo = repo;
        map.setCellValueFactory(new PropertyValueFactory<>("id"));
//        object_amount.setCellValueFactory(new PropertyValueFactory<>("description"));
        object_amount.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Map, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Map, String> p) {
                if (p.getValue() != null) {
                    return new SimpleStringProperty(Integer.toString(mapObjectRepo.selectByMapId(p.getValue().getId()).size()));
                } else {
                    return new SimpleStringProperty("<no name>");
                }
            }
        });
//        color.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<MapObject, String>, ObservableValue<String>>() {
//
//            @Override
//            public ObservableValue<String> call(TableColumn.CellDataFeatures<MapObject, String> p) {
//                if (p.getValue() != null) {
//                    if (Utils.toShape(p.getValue().getShape().getClass().getName()) != sample.databasemanage.entity.MapObject.Shape.LINE) {
//                        Shape shape = (Shape) p.getValue().getShape();
//                        return new SimpleStringProperty(shape.getFill().toString());
//                    }
//                    return new SimpleStringProperty("<no name>");
//                } else {
//                    return new SimpleStringProperty("<no name>");
//                }
//            }
//        });
//        layer.setCellValueFactory(new PropertyValueFactory<>("layer"));

        ObservableList<Map> list = FXCollections.observableArrayList(data);
        table.setItems(list);

        table.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object oldValue, Object newValue) {
                //Check whether item is selected and set value of selected item to Label
                if(table.getSelectionModel().getSelectedItem() != null)
                {
                    Map map = (Map) table.getSelectionModel().getSelectedItem();
                    chosenMapId = map.getId();
                    System.out.println(map.getId());
                }
            }
        });
    }
}
