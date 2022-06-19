package db.entitiesDao;
import db.Dao;
import db.JdbcConnection;
import entities.Box;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

//сделал по аналогии, почему-то не работает

public class BoxesDao implements Dao {

    private static final Logger LOGGER =
            Logger.getLogger(BoxesDao.class.getName());
    private final Optional connection;

    public BoxesDao() {
        this.connection = JdbcConnection.getConnection();
    }

    public Optional save(Box box) {
        String message = "The box to be added should not be null";
        Box nonNullBox = Objects.requireNonNull(box, message);
        String sql = "INSERT INTO "
                + "box(boxnum,rentprice) "
                + "VALUES(?, ?)";

        return connection.flatMap(conn -> {
            Optional generatedId = Optional.empty();

            //не понимаю, что за prepareStatement
            try (PreparedStatement statement =
                         conn.prepareStatement(
                                 sql,
                                 Statement.RETURN_GENERATED_KEYS)) {
                statement.setInt(1, nonNullBox.getBoxNum());
                statement.setFloat(2, nonNullBox.getRentPrice());

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

    public Optional get(int boxNum) {
        return connection.flatMap(conn -> {
            Optional box = Optional.empty();
            String sql = "SELECT * FROM box WHERE customer_id = " + boxNum;

            try (Statement statement = conn.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {

                if (resultSet.next()) {

                    float rentPrice = resultSet.getFloat("rentprice");
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


    public Collection getAll() {
        Collection boxes = new ArrayList<>();
        String sql = "SELECT * FROM box";

        connection.ifPresent(conn -> {
            try (Statement statement = conn.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {

                while (resultSet.next()) {
                    int boxNum = resultSet.getInt("boxnum");
                    float rentPrice = resultSet.getFloat("rentprice");

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
                + "rentprice = ?, "
                + "WHERE "
                + "boxNum = ?";

        connection.ifPresent(conn -> {
            try (PreparedStatement statement = conn.prepareStatement(sql)) {

                statement.setFloat(1, nonNullBox.getRentPrice());
                statement.setInt(2, nonNullBox.getBoxNum());

                int numberOfUpdatedRows = statement.executeUpdate();

                LOGGER.log(Level.INFO, "Was the customer updated successfully? {0}",
                        numberOfUpdatedRows > 0);

            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        });
    }

    public void delete(Box box) {
        String message = "The customer to be deleted should not be null";
        Box nonNullBox = Objects.requireNonNull(box, message);
        String sql = "DELETE FROM customer WHERE customer_id = ?";

        connection.ifPresent(conn -> {
            try (PreparedStatement statement = conn.prepareStatement(sql)) {

                statement.setInt(1, nonNullBox.getBoxNum());

                int numberOfDeletedRows = statement.executeUpdate();

                LOGGER.log(Level.INFO, "Was the customer deleted successfully? {0}",
                        numberOfDeletedRows > 0);

            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        });
    }



}


