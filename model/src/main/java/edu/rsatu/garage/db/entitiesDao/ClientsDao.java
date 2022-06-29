package edu.rsatu.garage.db.entitiesDao;

import edu.rsatu.garage.db.Dao;
import edu.rsatu.garage.db.JdbcConnection;
import edu.rsatu.garage.entities.Box;
import edu.rsatu.garage.entities.Client;
import edu.rsatu.garage.entities.Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientsDao implements Dao<Client, Long> {

    private static final Logger LOGGER =
            Logger.getLogger(ClientsDao.class.getName());
    private final Optional<Connection> connection;

    public ClientsDao() {
        this.connection = JdbcConnection.getConnection();
    }

    @Override
    public Optional<Client> get(Long id) {
        return connection.flatMap(conn -> {
            Optional<Client> client = Optional.empty();
            String sql = "SELECT * FROM client WHERE id = " + id;

            try (Statement statement = conn.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {
                if (resultSet.next()) {
                    String surname = resultSet.getString("surname");
                    String address = resultSet.getString("address");
                    client = Optional.of(new Client(id, surname, address));
                    LOGGER.log(Level.INFO, "Found {0} in database", client.get());
                }
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
            return client;
        });
    }

    @Override
    public List<Client> getAll() {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM client ORDER BY surname";
        connection.ifPresent(conn -> {
            try (Statement statement = conn.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {

                while (resultSet.next()) {
                    long id = resultSet.getLong("id");
                    String surname = resultSet.getString("surname");
                    String address = resultSet.getString("address");

                    Client client = new Client(id, surname, address);

                    clients.add(client);

                    LOGGER.log(Level.INFO, "Found {0} in database", client);
                }

            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        });
        return clients;
    }

    @Override
    public Optional<Long> save(Client client) {
        String message = "The client to be added should not be null";
        Client nonNullClient = Objects.requireNonNull(client, message);
        String sql = "INSERT INTO "
                + "client(surname, address)"
                + "VALUES(?, ?)";

        return connection.flatMap(conn -> {
            Optional<Long> generatedId = Optional.empty();
            try (PreparedStatement statement =
                         conn.prepareStatement(
                                 sql,
                                 Statement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, nonNullClient.getSurname());
                statement.setString(2, nonNullClient.getAddress());
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
                        new Object[]{nonNullClient,
                                (numberOfInsertedRows > 0)});
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }

            return generatedId;
        });
    }

    @Override
    public void update(Client client) {
        String message = "The client to be updated should not be null";
        Client nonNullClient = Objects.requireNonNull(client, message);
        String sql = "UPDATE client"
                + "SET "
                + "surname = ?, address = ? "
                + "WHERE "
                + "id = ?";

        connection.ifPresent(conn -> {
            try (PreparedStatement statement = conn.prepareStatement(sql)) {

                statement.setString(1, nonNullClient.getSurname());
                statement.setString(2, nonNullClient.getAddress());
                statement.setLong(3, nonNullClient.getId());

                int numberOfUpdatedRows = statement.executeUpdate();

                LOGGER.log(Level.INFO, "Was the model updated successfully? {0}",
                        numberOfUpdatedRows > 0);

            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        });
    }

    @Override
    public void delete(Client client) {
        String message = "The client to be deleted should not be null";
        Client nonNullClient = Objects.requireNonNull(client, message);
        String sql = "DELETE FROM client WHERE id = ?";
        connection.ifPresent(conn -> {
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setLong(1, nonNullClient.getId());

                int numberOfDeletedRows = statement.executeUpdate();

                LOGGER.log(Level.INFO, "Was the client deleted successfully? {0}",
                        numberOfDeletedRows > 0);

            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        });
    }

    public Optional<Client> getClientForBox(Box box) {
        String message = "The box should not be null";
        Box nonNullBox = Objects.requireNonNull(box, message);
        return connection.flatMap(conn -> {
            Optional<Client> client = Optional.empty();
            String sql = "SELECT * FROM client WHERE id IN" +
            "(SELECT car.client_id FROM car WHERE car.boxnum = ?)";

            try (PreparedStatement statement = conn.prepareStatement(sql)) {

                 statement.setInt(1, nonNullBox.getId());

                 ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    Long id = resultSet.getLong("id");
                    String surname = resultSet.getString("surname");
                    String address = resultSet.getString("address");
                    client = Optional.of(new Client(id, surname, address));
                    LOGGER.log(Level.INFO, "Found {0} in database", client.get());
                }
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
            return client;
        });
    }

    public List<Client> getClientsForModel(Model model) {
        String message = "The model should not be null";
        Model nonNullModel = Objects.requireNonNull(model, message);
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM client WHERE id IN" +
                "(SELECT client_id FROM car WHERE model_id = ?) ORDER BY surname";
        connection.ifPresent(conn -> {
            try (PreparedStatement statement = conn.prepareStatement(sql)) {

                statement.setLong(1, nonNullModel.getId());

                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    long id = resultSet.getLong("id");
                    String surname = resultSet.getString("surname");
                    String address = resultSet.getString("address");

                    Client client = new Client(id, surname, address);

                    clients.add(client);

                    LOGGER.log(Level.INFO, "Found {0} in database", client);
                }

            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        });
        return clients;
    }

    public List<Client> getClientsForDate(LocalDate rentEndDate) {
        String message = "The date should not be null";
        LocalDate nonNullDate = Objects.requireNonNull(rentEndDate, message);
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM client WHERE id IN " +
                "(SELECT client_id FROM car WHERE rental_end_date <= ?) ORDER BY surname";
        connection.ifPresent(conn -> {
            try (PreparedStatement statement = conn.prepareStatement(sql)) {

                statement.setObject(1, nonNullDate);

                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    long id = resultSet.getLong("id");
                    String surname = resultSet.getString("surname");
                    String address = resultSet.getString("address");

                    Client client = new Client(id, surname, address);

                    clients.add(client);

                    LOGGER.log(Level.INFO, "Found {0} in database", client);
                }

            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        });
        return clients;
    }

    public List<Client> getClientsForModelDate(Model model, LocalDate date) {
        String message1 = "The date should not be null";
        Model nonNullModel = Objects.requireNonNull(model, message1);
        String message2 = "The model should not be null";
        LocalDate nonNullDate = Objects.requireNonNull(date, message2);
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM client WHERE id IN " +
                "(SELECT client_id FROM car WHERE model_id = ?)" +
                "AND id IN (SELECT client_id FROM car WHERE rental_end_date <= ?) ORDER BY surname";
        connection.ifPresent(conn -> {
            try (PreparedStatement statement = conn.prepareStatement(sql)) {

                statement.setLong(1, nonNullModel.getId());
                statement.setObject(2, nonNullDate);

                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    long id = resultSet.getLong("id");
                    String surname = resultSet.getString("surname");
                    String address = resultSet.getString("address");

                    Client client = new Client(id, surname, address);

                    clients.add(client);

                    LOGGER.log(Level.INFO, "Found {0} in database", client);
                }

            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        });
        return clients;
    }
}
