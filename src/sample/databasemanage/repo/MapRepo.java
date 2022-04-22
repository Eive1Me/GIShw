package sample.databasemanage.repo;



import sample.databasemanage.entity.Coordinate;
import sample.databasemanage.entity.Map;
import sample.databasemanage.entity.MapObject;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MapRepo implements IRestRepository<Map> {
    String url = "jdbc:postgresql://localhost:5432/gis";
    String user = "postgres";
    String password = "123";
    Connection con;

    private static String selectQuery = "SELECT \"id\",  \"start_shirota\", \"start_dolgota\", \"end_shirota\", \"end_dolgota\", \"start_x\", \"start_y\", \"end_x\", \"end_y\", \"image\"  " +
            "FROM \"map\" " +
            "ORDER BY \"id\"";

    private static String selectByIdQuery = "SELECT \"id\",  \"start_shirota\", \"start_dolgota\", \"end_shirota\", \"end_dolgota\", \"start_x\", \"start_y\", \"end_x\", \"end_y\", \"image\" " +
            "FROM \"map\" " +
            "WHERE \"id\" = ?";

    private static String insertQuery = "INSERT INTO \"map\"( \"start_shirota\", \"start_dolgota\", \"end_shirota\", \"end_dolgota\", \"start_x\", \"start_y\", \"end_x\", \"end_y\", \"image\" ) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) " +
            "RETURNING \"id\", \"start_shirota\", \"start_dolgota\", \"end_shirota\", \"end_dolgota\", \"start_x\", \"start_y\", \"end_x\", \"end_y\", \"image\" ";

//    private static String updateQuery = "UPDATE \"order\" " +
//            "SET \"client_id\" = ?, \"item_id\" = ?, \"work_id\" = ?, \"accessory_id\" = ?, \"payment\" = ?, \"prepayment\" = ?, \"order_date\" = ?, \"deadline\" = ?, \"status\" = ? " +
//            "WHERE \"id\" = ? " +
//            "RETURNING \"id\", \"client_id\", \"item_id\", \"work_id\", \"accessory_id\", \"payment\", \"prepayment\", \"order_date\", \"deadline\", \"status\"";

    private static String deleteQuery = "DELETE FROM \"map\" " +
            "WHERE \"id\" = ? " +
            "RETURNING \"id\", \"start_shirota\", \"start_dolgota\", \"end_shirota\", \"end_dolgota\", \"start_x\", \"start_y\", \"end_x\", \"end_y\", \"image\"  ";

    public MapRepo() throws SQLException {
        con = DriverManager.getConnection(url, user, password);
    }

    @Override
    public ArrayList<Map> select() {
        try (PreparedStatement pst = con.prepareStatement(selectQuery)) {
            ResultSet result = pst.executeQuery();
            System.out.println("Successfully selected.");
            ArrayList<Map> objects = new ArrayList<>();
            while (result.next()) objects.add(new Map(result.getInt(1),
                    result.getInt(2), result.getInt(3),
                    result.getInt(4), result.getInt(5),
                    result.getDouble(6), result.getDouble(7),
                    result.getDouble(8), result.getDouble(9),
                    result.getBytes(10)));
            return objects;

        } catch (SQLException ex) {

            Logger lgr = Logger.getLogger(MapRepo.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return null;
    }

    @Override
    public Map select(Integer id) {
        try (PreparedStatement pst = con.prepareStatement(selectByIdQuery)) {

            pst.setInt(1, id);
            ResultSet result = pst.executeQuery();
            System.out.println("Successfully selected.");
            while (result.next()) return new Map(result.getInt(1),
                    result.getInt(2), result.getInt(3),
                    result.getInt(4), result.getInt(5),
                    result.getDouble(6), result.getDouble(7),
                    result.getDouble(8), result.getDouble(9),
                    result.getBytes(10));

        } catch (SQLException ex) {

            Logger lgr = Logger.getLogger(MapRepo.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return null;
    }

    @Override
    public Map insert(Map entity) {
        try (PreparedStatement pst = con.prepareStatement(insertQuery)) {

            pst.setInt(1, entity.getStartShirota());
            pst.setInt(2, entity.getStartDolgota());
            pst.setInt(3, entity.getEndShirota());
            pst.setInt(4, entity.getEndDolgota());
            pst.setDouble(5, entity.getStartX());
            pst.setDouble(6, entity.getStartY());
            pst.setDouble(7, entity.getEndX());
            pst.setDouble(8, entity.getEndY());
            pst.setBytes(9, entity.getImage());
            ResultSet result = pst.executeQuery();
            System.out.println("Successfully created.");
            while (result.next()) return new Map(result.getInt(1),
                    result.getInt(2), result.getInt(3),
                    result.getInt(4), result.getInt(5),
                    result.getDouble(6), result.getDouble(7),
                    result.getDouble(8), result.getDouble(9),
                    result.getBytes(10));

        } catch (SQLException ex) {

            Logger lgr = Logger.getLogger(MapRepo.class.getName());
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
    public Map delete(Integer id) {
        try (PreparedStatement pst = con.prepareStatement(deleteQuery)) {

            pst.setInt(1, id);
            ResultSet result = pst.executeQuery();
            System.out.println("Successfully deleted.");
            while (result.next()) return new Map(result.getInt(1),
                    result.getInt(2), result.getInt(3),
                    result.getInt(4), result.getInt(5),
                    result.getDouble(6), result.getDouble(7),
                    result.getDouble(8), result.getDouble(9),
                    result.getBytes(10));

        } catch (SQLException ex) {

            Logger lgr = Logger.getLogger(MapRepo.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return null;
    }
}
