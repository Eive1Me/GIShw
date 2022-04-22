package sample.databasemanage.repo;


import sample.databasemanage.entity.Coordinate;
import sample.databasemanage.entity.MapObject;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CoordinateRepo implements IRestRepository<Coordinate> {
    String url = "jdbc:postgresql://localhost:5432/gis";
    String user = "postgres";
    String password = "123";
    Connection con;

    private static String selectQuery = "SELECT \"id\", \"object_id\", \"value_x\", \"value_y\" " +
            "FROM \"coordinate\" " +
            "ORDER BY \"id\"";

    private static String selectByIdQuery = "SELECT \"id\",  \"object_id\", \"value_x\", \"value_y\" " +
            "FROM \"coordinate\" " +
            "WHERE \"id\" = ?";

    private static String insertQuery = "INSERT INTO \"coordinate\"( \"object_id\", \"value_x\", \"value_y\") " +
            "VALUES (?, ?, ?) " +
            "RETURNING \"id\",  \"object_id\", \"value_x\", \"value_y\" ";

//    private static String updateQuery = "UPDATE \"order\" " +
//            "SET \"client_id\" = ?, \"item_id\" = ?, \"work_id\" = ?, \"accessory_id\" = ?, \"payment\" = ?, \"prepayment\" = ?, \"order_date\" = ?, \"deadline\" = ?, \"status\" = ? " +
//            "WHERE \"id\" = ? " +
//            "RETURNING \"id\", \"client_id\", \"item_id\", \"work_id\", \"accessory_id\", \"payment\", \"prepayment\", \"order_date\", \"deadline\", \"status\"";

    private static String deleteQuery = "DELETE FROM \"coordinate\" " +
            "WHERE \"id\" = ? " +
            "RETURNING \"id\", \"object_id\", \"value_x\", \"value_y\" ";

    public CoordinateRepo() throws SQLException {
        con = DriverManager.getConnection(url, user, password);
    }

    @Override
    public ArrayList<Coordinate> select() {
        try (PreparedStatement pst = con.prepareStatement(selectQuery)) {
            ResultSet result = pst.executeQuery();
            System.out.println("Successfully selected.");
            ArrayList<Coordinate> objects = new ArrayList<>();
            while (result.next()) objects.add(new Coordinate(result.getInt(1), result.getInt(2), result.getDouble(3), result.getDouble(4)));
            return objects;

        } catch (SQLException ex) {

            Logger lgr = Logger.getLogger(CoordinateRepo.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return null;
    }

    @Override
    public Coordinate select(Integer id) {
        try (PreparedStatement pst = con.prepareStatement(selectByIdQuery)) {

            pst.setInt(1, id);
            ResultSet result = pst.executeQuery();
            System.out.println("Successfully selected.");
            while (result.next()) return new Coordinate(result.getInt(1), result.getInt(2), result.getDouble(3), result.getDouble(4));

        } catch (SQLException ex) {

            Logger lgr = Logger.getLogger(CoordinateRepo.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return null;
    }

    @Override
    public Coordinate insert(Coordinate entity) {
        try (PreparedStatement pst = con.prepareStatement(insertQuery)) {

            pst.setInt(1, entity.getObjectID());
            pst.setDouble(2, entity.getX());
            pst.setDouble(3, entity.getY());
            ResultSet result = pst.executeQuery();
            System.out.println("Successfully created.");
            while (result.next()) return new Coordinate(result.getInt(1), result.getInt(2), result.getDouble(3), result.getDouble(4));

        } catch (SQLException ex) {

            Logger lgr = Logger.getLogger(CoordinateRepo.class.getName());
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
    public Coordinate delete(Integer id) {
        try (PreparedStatement pst = con.prepareStatement(deleteQuery)) {

            pst.setInt(1, id);
            ResultSet result = pst.executeQuery();
            System.out.println("Successfully deleted.");
            while (result.next()) return new Coordinate(result.getInt(1), result.getInt(2), result.getDouble(3), result.getDouble(4));

        } catch (SQLException ex) {

            Logger lgr = Logger.getLogger(CoordinateRepo.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return null;
    }
}
