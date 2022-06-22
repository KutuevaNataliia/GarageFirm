package edu.rsatu.garage.db.entitiesDao;

import edu.rsatu.garage.db.JdbcConnection;
import edu.rsatu.garage.entities.Box;
import edu.rsatu.garage.entities.Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BoxesModelsDao {
    private static final Logger LOGGER =
            Logger.getLogger(BoxesDao.class.getName());
    private final Optional<Connection> connection;

    public BoxesModelsDao() {
        this.connection = JdbcConnection.getConnection();
    }

    public void addModelsForBox(Box box, List<Model> models) {
        String message = "The box to be added should not be null";
        Box nonNullBox = Objects.requireNonNull(box, message);
        String sql = "INSERT INTO fit (box_num, model_id) VALUES (?, ?)";
        connection.ifPresent(conn -> {
            for (Model model : models) {
                try (PreparedStatement statement = conn.prepareStatement(sql)) {
                    statement.setInt(1, nonNullBox.getId());
                    statement.setLong(2, model.getId());
                    statement.executeUpdate();
                } catch (SQLException ex) {
                    LOGGER.log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    public void deleteModelsFromBox(Box box, List<Model> models) {
        String message = "The box to be added should not be null";
        Box nonNullBox = Objects.requireNonNull(box, message);
        String sql = "DELETE FROM fit WHERE box_num = ? AND model_id = ?)";
        connection.ifPresent(conn -> {
            for (Model model : models) {
                try (PreparedStatement statement = conn.prepareStatement(sql)) {
                    statement.setInt(1, nonNullBox.getId());
                    statement.setLong(2, model.getId());
                    statement.executeUpdate();
                } catch (SQLException ex) {
                    LOGGER.log(Level.SEVERE, null, ex);
                }
            }
        });
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

    public List<Model> getModelsForBox(Box box) {
        List<Model> models = new ArrayList<>();
        String message = "The box should not be null";
        Box nonNullBox = Objects.requireNonNull(box, message);
        String sql = "SELECT * FROM model WHERE id in " +
                "(SELECT model_id FROM fit WHERE box_num = ?)";
        connection.ifPresent(conn -> {
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setInt(1, nonNullBox.getId());

                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    long id = resultSet.getLong("id");
                    String name = resultSet.getString("name");
                    Model model = new Model(id, name);
                    models.add(model);

                    LOGGER.log(Level.INFO, "Found {0} in database", model);
                }

            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        });
        return models;
    }
}
