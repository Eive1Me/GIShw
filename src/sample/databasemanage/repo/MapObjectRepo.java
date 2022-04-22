package sample.databasemanage.repo;

import sample.databasemanage.entity.Map;
import sample.databasemanage.entity.MapObject;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MapObjectRepo implements IRestRepository<MapObject> {
    String url = "jdbc:postgresql://localhost:5432/gis";
    String user = "postgres";
    String password = "15968";
    Connection con;

    private static String selectQuery = "SELECT \"id\", \"map_id\", \"name\", \"description\", \"shape\", \"color\", \"layer\" " +
            "FROM \"map_object\" " +
            "ORDER BY \"id\"";

    private static String selectByIdQuery = "SELECT \"id\", \"map_id\", \"name\", \"description\", \"shape\", \"color\", \"layer\" " +
            "FROM \"map_object\" " +
            "WHERE \"id\" = ?";

    private static String insertQuery = "INSERT INTO \"map_object\"(\"map_id\", \"name\", \"description\", \"shape\", \"color\", \"layer\") " +
            "VALUES (?, ?, ?, ?, ?, ?) " +
            "RETURNING \"id\", \"map_id\", \"name\", \"description\", \"shape\", \"color\", \"layer\" ";

//    private static String updateQuery = "UPDATE \"order\" " +
//            "SET \"client_id\" = ?, \"item_id\" = ?, \"work_id\" = ?, \"accessory_id\" = ?, \"payment\" = ?, \"prepayment\" = ?, \"order_date\" = ?, \"deadline\" = ?, \"status\" = ? " +
//            "WHERE \"id\" = ? " +
//            "RETURNING \"id\", \"client_id\", \"item_id\", \"work_id\", \"accessory_id\", \"payment\", \"prepayment\", \"order_date\", \"deadline\", \"status\"";

    private static String deleteQuery = "DELETE FROM \"map_object\" " +
            "WHERE \"id\" = ? " +
            "RETURNING \"id\", \"map_id\", \"name\", \"description\", \"shape\", \"color\", \"layer\" ";

    public MapObjectRepo() throws SQLException {
        con = DriverManager.getConnection(url, user, password);
    }

    @Override
    public ArrayList<MapObject> select() {
        try (PreparedStatement pst = con.prepareStatement(selectQuery)) {
            ResultSet result = pst.executeQuery();
            System.out.println("Successfully selected.");
            ArrayList<MapObject> objects = new ArrayList<>();
            while (result.next()) objects.add(new MapObject(result.getInt(1), result.getInt(2),
                    result.getString(3), result.getString(4),
                    MapObject.Shape.valueOf(result.getString(5)), result.getString(6), result.getInt(7)));
            return objects;

        } catch (SQLException ex) {

            Logger lgr = Logger.getLogger(MapObjectRepo.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return null;
    }

    @Override
    public MapObject select(Integer id) {
        try (PreparedStatement pst = con.prepareStatement(selectByIdQuery)) {

            pst.setInt(1, id);
            ResultSet result = pst.executeQuery();
            System.out.println("Successfully selected.");
            while (result.next()) return new MapObject(result.getInt(1), result.getInt(2),
                    result.getString(3), result.getString(4),
                    MapObject.Shape.valueOf(result.getString(5)), result.getString(6), result.getInt(7));

        } catch (SQLException ex) {

            Logger lgr = Logger.getLogger(MapObjectRepo.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return null;
    }

    @Override
    public MapObject insert(MapObject entity) {
        try (PreparedStatement pst = con.prepareStatement(insertQuery)) {

            pst.setInt(1, entity.getMapID());
            pst.setString(2, entity.getName());
            pst.setString(3, entity.getDescription());
            pst.setString(4, entity.getShape().toString());
            pst.setString(5, entity.getColor());
            pst.setInt(6, entity.getLayer());
            ResultSet result = pst.executeQuery();
            System.out.println("Successfully created.");
            while (result.next()) return new MapObject(result.getInt(1), result.getInt(2),
                                result.getString(3), result.getString(4),
                    MapObject.Shape.valueOf(result.getString(5)), result.getString(6), result.getInt(7));

        } catch (SQLException ex) {

            Logger lgr = Logger.getLogger(MapObjectRepo.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return null;
    }

//    @Override
//    public Order update(Integer id, Order entity) {
//        Object[] params = new Object[] { entity.getClientId(), entity.getItemId(), entity.getWorkId(), entity.getAccessoryId(), entity.getPayment(), entity.getPrepayment(), entity.getOrderDate(), entity.getDeadline(), entity.getStatus(), id };
//        int[] types = new int[] { Types.INTEGER, Types.INTEGER, Types.INTEGER, Types.INTEGER, Types.FLOAT, Types.FLOAT, Types.TIMESTAMP, Types.TIMESTAMP, Types.VARCHAR, Types.INTEGER };
//        SqlRowSet rowSet = jdbcOperations.queryForRowSet(updateQuery, params, types);
//        if (!rowSet.next()) {
//            return null;
//        }
//        return new Order(
//                rowSet.getInt(1),
//                rowSet.getInt(2),
//                rowSet.getInt(3),
//                rowSet.getInt(4),
//                rowSet.getInt(5),
//                rowSet.getFloat(6),
//                rowSet.getFloat(7),
//                rowSet.getTimestamp(8),
//                rowSet.getTimestamp(9),
//                rowSet.getString(10)
//        );
//    }
//
    @Override
    public MapObject delete(Integer id) {
        try (PreparedStatement pst = con.prepareStatement(deleteQuery)) {

            pst.setInt(1, id);
            ResultSet result = pst.executeQuery();
            System.out.println("Successfully deleted.");
            while (result.next()) return new MapObject(result.getInt(1), result.getInt(2),
                    result.getString(3), result.getString(4),
                    MapObject.Shape.valueOf(result.getString(5)), result.getString(6), result.getInt(7));

        } catch (SQLException ex) {

            Logger lgr = Logger.getLogger(MapObjectRepo.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return null;
    }
}
