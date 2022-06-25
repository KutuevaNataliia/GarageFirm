package edu.rsatu.garage.db.entitiesDao;

import edu.rsatu.garage.db.Dao;
import edu.rsatu.garage.db.JdbcConnection;
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

public class ModelsDao implements Dao<Model, Long>{

    private static final Logger LOGGER =
            Logger.getLogger(ModelsDao.class.getName());
    private final Optional<Connection> connection;

    public ModelsDao() {
        this.connection = JdbcConnection.getConnection();
    }

    @Override
    public Optional<Model> get(Long id) {
        return connection.flatMap(conn -> {
            Optional<Model> model = Optional.empty();
            String sql = "SELECT * FROM model WHERE id = " + id;

            try (Statement statement = conn.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {
                if (resultSet.next()) {
                    String name = resultSet.getString("name");
                    model = Optional.of(new Model(id, name));
                    LOGGER.log(Level.INFO, "Found {0} in database", model.get());
                }
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
            return model;
        });
    }

    @Override
    public List<Model> getAll() {
        List<Model> models = new ArrayList<>();
        String sql = "SELECT * FROM model";
        connection.ifPresent(conn -> {
            try (Statement statement = conn.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {

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

    @Override
    public Optional<Long> save(Model model) {
        String message = "The model to be added should not be null";
        Model nonNullModel = Objects.requireNonNull(model, message);
        String sql = "INSERT INTO "
                + "model(name)"
                + "VALUES(?)";

        return connection.flatMap(conn -> {
            Optional<Long> generatedId = Optional.empty();
            try (PreparedStatement statement =
                         conn.prepareStatement(
                                 sql,
                                 Statement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, nonNullModel.getName());
                int numberOfInsertedRows = statement.executeUpdate();

                if (numberOfInsertedRows > 0) {
                    try (ResultSet resultSet = statement.getGeneratedKeys()) {
                        if (resultSet.next()) {
                            generatedId = Optional.of(resultSet.getLong(1));
                        }
                    }
                }

                LOGGER.log(
                        Level.INFO,
                        "{0} created successfully? {1}",
                        new Object[]{nonNullModel,
                                (numberOfInsertedRows > 0)});
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }

            return generatedId;
        });
    }

    @Override
    public void update(Model model) {
        String message = "The model to be updated should not be null";
        Model nonNullModel = Objects.requireNonNull(model, message);
        String sql = "UPDATE model"
                + "SET "
                + "name = ? "
                + "WHERE "
                + "id = ?";

        connection.ifPresent(conn -> {
            try (PreparedStatement statement = conn.prepareStatement(sql)) {

                statement.setString(1, nonNullModel.getName());
                statement.setLong(2, nonNullModel.getId());

                int numberOfUpdatedRows = statement.executeUpdate();

                LOGGER.log(Level.INFO, "Was the model updated successfully? {0}",
                        numberOfUpdatedRows > 0);

            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        });
    }

    @Override
    public void delete(Model model) {
        String message = "The model to be deleted should not be null";
        Model nonNullModel = Objects.requireNonNull(model, message);
        String deleteFromBoxes = "DELETE FROM fit WHERE model_id = ?";
        String deleteModel = "DELETE FROM model WHERE id = ?";
        connection.ifPresent(conn -> {
            try (PreparedStatement boxesStatement = conn.prepareStatement(deleteFromBoxes);
                        PreparedStatement modelsStatement = conn.prepareStatement(deleteModel)) {
                conn.setAutoCommit(false);
                boxesStatement.setLong(1, nonNullModel.getId());
                boxesStatement.executeUpdate();
                modelsStatement.setLong(1, nonNullModel.getId());
                int numberOfDeletedRows = modelsStatement.executeUpdate();
                conn.commit();

                LOGGER.log(Level.INFO, "Was the model deleted successfully? {0}",
                        numberOfDeletedRows > 0);

            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
                try {
                    conn.rollback();
                } catch (SQLException e) {
                    LOGGER.log(Level.SEVERE, null, e);
                }
            }
        });
    }
}
