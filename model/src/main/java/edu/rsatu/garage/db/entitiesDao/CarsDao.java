package edu.rsatu.garage.db.entitiesDao;

import edu.rsatu.garage.db.Dao;
import edu.rsatu.garage.db.JdbcConnection;
import edu.rsatu.garage.entities.Car;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CarsDao implements Dao<Car, String>{

    private static final Logger LOGGER =
            Logger.getLogger(CarsDao.class.getName());

    private final Optional<Connection> connection;

    public CarsDao() {
        this.connection = JdbcConnection.getConnection();
    }

    public Optional<Integer> save(Car car) {
        String message = "The car to be added should not be null";
        Car nonNullCar = Objects.requireNonNull(car, message);
        String sql = "INSERT INTO "
                + "car (carNum,"
                + "rental_start_date,"
                + "rental_end_date,"
                + "rectNum, "
                + "boxNum, "
                + "model_id, "
                + "client_id) "
                + "VALUES(?,?,?,?,?,?,?)";

        return connection.flatMap(conn -> {
            Optional<Integer> generatedId = Optional.empty();
            try (PreparedStatement statement =
                         conn.prepareStatement(
                                 sql,
                                 Statement.RETURN_GENERATED_KEYS)) {
                //statement.setInt(1, nonNullCar.getId());
                statement.setString(1, nonNullCar.getNumber());
                statement.setInt(2, nonNullCar.getModelId());
                statement.setInt(3, nonNullCar.getClientId());
                statement.setInt(4, nonNullCar.getBoxId());
                statement.setInt(5, nonNullCar.getReceiptNumber());
                statement.setDate(6, (Date) nonNullCar.getRentStartDate());
                statement.setDate(7, (Date)nonNullCar.getRentEndDate());
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
                        new Object[]{nonNullCar,
                                (numberOfInsertedRows > 0)});
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }

            return generatedId;
        });
    }

    public Optional<Car> get(String number) {
        return connection.flatMap(conn -> {
            Optional<Car> car = Optional.empty();
            String sql = "SELECT * FROM car WHERE carNum = " + number;

            try (Statement statement = conn.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {

                if (resultSet.next()) {
                    Integer modelId = resultSet.getInt("model_id");
                    Integer clientId = resultSet.getInt("client_id");
                    Integer boxId = resultSet.getInt("boxNum");
                    Integer receiptNumber = resultSet.getInt("rectNum");
                   java.util.Date rentStartDate =  resultSet.getDate("rental_start_date");
                   java.util.Date rentEndDate = resultSet.getDate("rental_end_date");
                    car = Optional.of(
                            new Car(number, modelId,clientId,boxId,receiptNumber,rentStartDate,rentEndDate));
                    LOGGER.log(Level.INFO, "Found {0} in database", car.get());
                }
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
            return car;
        });
    }

    public Collection<Car> getAll() {
        Collection<Car> cars = new ArrayList<>();
        String sql = "SELECT * FROM car";
        connection.ifPresent(conn -> {
            try (Statement statement = conn.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {

                while (resultSet.next()) {
                    String number = resultSet.getString("carNum");
                    Integer modelId = resultSet.getInt("model_id");
                    Integer clientId = resultSet.getInt("client_id");
                    Integer boxId = resultSet.getInt("boxNum");
                    Integer receiptNumber = resultSet.getInt("rectNum");
                    java.util.Date rentStartDate =  resultSet.getDate("rental_start_date");
                    java.util.Date rentEndDate = resultSet.getDate("rental_end_date");

                    Car car = new Car(number, modelId,clientId,boxId,receiptNumber,rentStartDate,rentEndDate);

                    cars.add(car);

                    LOGGER.log(Level.INFO, "Found {0} in database", car);
                }

            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        });
        return cars;
    }

    public void update(Car car) {
        String message = "The car to be updated should not be null";
        Car nonNullCar = Objects.requireNonNull(car, message);

        String sql = "UPDATE box "
                + "SET "
                + "rental_start_date = ?, "
                + "rental_end_date = ?, "
                + "rectNum = ?, "
                + "boxNum = ?, "
                + "model_id = ?, "
                + "client_id = ?, "
                + "WHERE "
                + "carNum = ?";

        connection.ifPresent(conn -> {
            try (PreparedStatement statement = conn.prepareStatement(sql)) {

                statement.setString(1, nonNullCar.getNumber());
                statement.setInt(2, nonNullCar.getModelId());
                statement.setInt(3, nonNullCar.getClientId());
                statement.setInt(4, nonNullCar.getBoxId());
                statement.setInt(5, nonNullCar.getReceiptNumber());
                statement.setDate(6, (Date) nonNullCar.getRentStartDate());
                statement.setDate(7, (Date)nonNullCar.getRentEndDate());

                int numberOfUpdatedRows = statement.executeUpdate();

                LOGGER.log(Level.INFO, "Was the car updated successfully? {0}",
                        numberOfUpdatedRows > 0);

            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        });
    }

    public void delete(Car car) {
        String message = "The car to be deleted should not be null";
        Car nonNullCar = Objects.requireNonNull(car, message);
        String sql = "DELETE FROM car WHERE carNum = ?";
        connection.ifPresent(conn -> {
            try (PreparedStatement statement = conn.prepareStatement(sql)) {

                statement.setString(1, nonNullCar.getNumber());


                int numberOfDeletedRows = statement.executeUpdate();

                LOGGER.log(Level.INFO, "Was the car deleted successfully? {0}",
                        numberOfDeletedRows > 0);

            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        });
    }
}
