package edu.rsatu.garage.db.entitiesDao;

import edu.rsatu.garage.db.Dao;
import edu.rsatu.garage.db.JdbcConnection;
import edu.rsatu.garage.entities.Box;
import edu.rsatu.garage.entities.Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BoxesDao implements Dao<Box, Integer> {

    private static final Logger LOGGER =
            Logger.getLogger(BoxesDao.class.getName());
    private final Optional<Connection> connection;

    public BoxesDao() {
        this.connection = JdbcConnection.getConnection();
    }

    public Optional<Integer> save(Box box) {
        String message = "The box to be added should not be null";
        Box nonNullBox = Objects.requireNonNull(box, message);
        String sql = "INSERT INTO "
                + "box(boxnum, rentprice) "
                + "VALUES(?, ?)";

        return connection.flatMap(conn -> {
            Optional<Integer> generatedId = Optional.empty();
            try (PreparedStatement statement =
                         conn.prepareStatement(
                                 sql,
                                 Statement.RETURN_GENERATED_KEYS)) {
                statement.setInt(1, nonNullBox.getId());
                statement.setDouble(2, nonNullBox.getRentPrice());

                int numberOfInsertedRows = statement.executeUpdate();

                // Retrieve the auto-generated id
                if (numberOfInsertedRows > 0) {
                    try (ResultSet resultSet = statement.getGeneratedKeys()) {
                        if (resultSet.next()) {
                            generatedId = Optional.of(resultSet.getInt(1));
                        }
                    }
                }

                LOGGER.log(
                        Level.INFO,
                        "{0} created successfully? {1}",
                        new Object[]{nonNullBox,
                                (numberOfInsertedRows > 0)});
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }

            return generatedId;
        });
    }

    public Optional<Box> get(Integer boxNum) {
        return connection.flatMap(conn -> {
            Optional<Box> box = Optional.empty();
            String sql = "SELECT * FROM box WHERE boxNum = " + boxNum;

            try (Statement statement = conn.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {

                if (resultSet.next()) {
                    double rentPrice = resultSet.getDouble("rentprice");
                    box = Optional.of(
                            new Box(boxNum, rentPrice));
                    LOGGER.log(Level.INFO, "Found {0} in database", box.get());
                }
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
            return box;
        });
    }

    public List<Box> getAll() {
        List<Box> boxes = new ArrayList<>();
        String sql = "SELECT * FROM box";
        connection.ifPresent(conn -> {
            try (Statement statement = conn.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {

                while (resultSet.next()) {
                    int boxNum = resultSet.getInt("boxnum");
                    double rentPrice = resultSet.getDouble("rentprice");

                    Box box = new Box(boxNum,rentPrice);

                    boxes.add(box);

                    LOGGER.log(Level.INFO, "Found {0} in database", box);
                }

            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        });
        return boxes;
    }

    public void update(Box box) {
        String message = "The box to be updated should not be null";
        Box nonNullBox = Objects.requireNonNull(box, message);
        String sql = "UPDATE box "
                + "SET "
                + "rentprice = ? "
                + "WHERE "
                + "boxNum = ?";

        connection.ifPresent(conn -> {
            try (PreparedStatement statement = conn.prepareStatement(sql)) {

                statement.setDouble(1, nonNullBox.getRentPrice());
                statement.setInt(2, nonNullBox.getId());

                int numberOfUpdatedRows = statement.executeUpdate();

                LOGGER.log(Level.INFO, "Was the box updated successfully? {0}",
                        numberOfUpdatedRows > 0);

            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        });
    }

    public void delete(Box box) {
        String message = "The box to be deleted should not be null";
        Box nonNullBox = Objects.requireNonNull(box, message);
        String sql = "DELETE FROM box WHERE boxNum = ?";
        connection.ifPresent(conn -> {
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setInt(1, nonNullBox.getId());

                int numberOfDeletedRows = statement.executeUpdate();

                LOGGER.log(Level.INFO, "Was the box deleted successfully? {0}",
                        numberOfDeletedRows > 0);

            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        });
    }

    public void changeAllPrices(int number, boolean increase) {
        String sql;
        if (increase) {
            sql = "UPDATE box set rentprice = rentprice * ?";
        } else {
            sql = "UPDATE box set rentprice = rentprice / ?";
        }
        connection.ifPresent(conn -> {
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setInt(1, number);
                statement.executeUpdate();
                LOGGER.log(Level.INFO, "Was the prices updated successfully");
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        });
    }

    public List<Box> getFreeBoxes() {
        List<Box> boxes = new ArrayList<>();
        String sql = "SELECT * FROM box WHERE NOT EXISTS " +
        "(SELECT car.box_Num FROM car WHERE car.box_Num = box.boxNum)";
        connection.ifPresent(conn -> {
            try (Statement statement = conn.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {

                while (resultSet.next()) {
                    int boxNum = resultSet.getInt("boxnum");
                    double rentPrice = resultSet.getDouble("rentprice");

                    Box box = new Box(boxNum,rentPrice);

                    boxes.add(box);

                    LOGGER.log(Level.INFO, "Found {0} in database", box);
                }

            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        });
        return boxes;
    }

    public List<Box> getBoxesForModel(Model model) {
        ArrayList<Box> boxes = new ArrayList<>();
        String message = "The model should not be null";
        Model nonNullModel = Objects.requireNonNull(model, message);
        String sql = "SELECT * FROM box WHERE boxnum IN " +
                "(SELECT box_num FROM fit WHERE model_id = ?) ";
        connection.ifPresent(conn -> {
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setLong(1, nonNullModel.getId());

                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    int boxNum = resultSet.getInt("boxnum");
                    Double rentPrice = resultSet.getDouble("rentprice");
                    Box box = new Box(boxNum, rentPrice);
                    boxes.add(box);

                    LOGGER.log(Level.INFO, "Found {0} in database", box);
                }

            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        });
        return boxes;
    }

    public List<Box> getFreeBoxesForModel(Model model) {
        ArrayList<Box> boxes = new ArrayList<>();
        String message = "The model should not be null";
        Model nonNullModel = Objects.requireNonNull(model, message);
        String sql = "SELECT * FROM box WHERE boxnum IN " +
                "(SELECT box_num FROM fit WHERE model_id = ?) " +
                "AND NOT EXISTS (SELECT car.box_Num FROM car WHERE car.box_Num = box.boxNum)";
        connection.ifPresent(conn -> {
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setLong(1, nonNullModel.getId());

                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    int boxNum = resultSet.getInt("boxnum");
                    Double rentPrice = resultSet.getDouble("rentprice");
                    Box box = new Box(boxNum, rentPrice);
                    boxes.add(box);

                    LOGGER.log(Level.INFO, "Found {0} in database", box);
                }

            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        });
        return boxes;
    }
}


