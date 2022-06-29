package edu.rsatu.garage.db.entitiesDao;

import edu.rsatu.garage.db.Dao;
import edu.rsatu.garage.db.JdbcConnection;
import edu.rsatu.garage.db.Pair;
import edu.rsatu.garage.entities.Car;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CarsDao implements Dao<Car, String>{

    private static final Logger LOGGER =
            Logger.getLogger(CarsDao.class.getName());

    private final Optional<Connection> connection;

    public CarsDao() {
        this.connection = JdbcConnection.getConnection();
    }

    public Optional<Long> save(Car car) {
        String message = "The car to be added should not be null";
        Car nonNullCar = Objects.requireNonNull(car, message);
        String sql = "INSERT INTO "
                + "car (carnum, "
                + "rental_start_date, "
                + "rental_end_date, "
                + "boxnum, "
                + "model_id, "
                + "client_id) "
                + "VALUES(?,?,?,?,?,?)";

        //String[] generatedKeysColumns = {"rectum"};
        return connection.flatMap(conn -> {
            Optional<Long> generatedReceiptNumber = Optional.empty();
            try (PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                statement.setString(1, nonNullCar.getNumber());
                statement.setObject(2, nonNullCar.getRentStartDate());
                statement.setObject(3, nonNullCar.getRentEndDate());
                statement.setInt(4, nonNullCar.getBoxId());
                statement.setLong(5, nonNullCar.getModelId());
                statement.setLong(6, nonNullCar.getClientId());

                int numberOfInsertedRows = statement.executeUpdate();

                // Retrieve the auto-generated receipt number
                if (numberOfInsertedRows > 0) {
                    try (ResultSet resultSet = statement.getGeneratedKeys()) {
                        if (resultSet.next()) {
                            generatedReceiptNumber = Optional.of(resultSet.getLong("rectnum"));
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

            return generatedReceiptNumber;
        });
    }

    public Optional<Car> get(String number) {
        return connection.flatMap(conn -> {
            Optional<Car> car = Optional.empty();
            String sql = "SELECT * FROM car WHERE carnum = ?";

            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setString(1, number);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    LocalDate rentStartDate = resultSet.getObject("rental_start_date", LocalDate.class);
                    LocalDate rentEndDate = resultSet.getObject("rental_end_date", LocalDate.class);
                    Long receiptNumber = resultSet.getLong("rectnum");
                    Integer boxId = resultSet.getInt("boxnum");
                    Long modelId = resultSet.getLong("model_id");
                    Long clientId = resultSet.getLong("client_id");

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

    public List<Car> getAll() {
        List<Car> cars = new ArrayList<>();
        String sql = "SELECT * FROM car";
        connection.ifPresent(conn -> {
            try (Statement statement = conn.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {

                while (resultSet.next()) {
                    String number = resultSet.getString("carNum");
                    LocalDate rentStartDate =  resultSet.getObject("rental_start_date", LocalDate.class);
                    LocalDate rentEndDate = resultSet.getObject("rental_end_date", LocalDate.class);
                    Long receiptNumber = resultSet.getLong("rectNum");
                    Integer boxId = resultSet.getInt("boxNum");
                    Long modelId = resultSet.getLong("model_id");
                    Long clientId = resultSet.getLong("client_id");


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

        String sql = "UPDATE car "
                + "SET "
                + "rental_start_date = ?, "
                + "rental_end_date = ?, "
                + "boxnum = ?, "
                + "model_id = ?, "
                + "client_id = ? "
                + "WHERE "
                + "carnum = ?";

        connection.ifPresent(conn -> {
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setObject(1, nonNullCar.getRentStartDate());
                statement.setObject(2, nonNullCar.getRentEndDate());
                statement.setInt(3, nonNullCar.getBoxId());
                statement.setLong(4, nonNullCar.getModelId());
                statement.setLong(5, nonNullCar.getClientId());
                statement.setString(6, nonNullCar.getNumber());



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

    public List<Car> getCarsForClientId(Long clientId) {
        List<Car> cars = new ArrayList<>();
        String sql = "SELECT * FROM car WHERE client_id = ? ";
        connection.ifPresent(conn -> {
            try (PreparedStatement statement = conn.prepareStatement(sql)) {

                statement.setLong(1, clientId);
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    String number = resultSet.getString("carNum");
                    LocalDate rentStartDate =  resultSet.getObject("rental_start_date", LocalDate.class);
                    LocalDate rentEndDate = resultSet.getObject("rental_end_date", LocalDate.class);
                    Long receiptNumber = resultSet.getLong("rectNum");
                    Integer boxId = resultSet.getInt("boxNum");
                    Long modelId = resultSet.getLong("model_id");
                    Car car = new Car(number, modelId,clientId,boxId,receiptNumber,rentStartDate,rentEndDate);

                    cars.add(car);
                }

            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        });
        return cars;
    }

    public List<Car> getCarsForBoxNumber(Integer boxNum) {
        List<Car> cars = new ArrayList<>();
        String sql = "SELECT * FROM car WHERE boxnum = ?";
        connection.ifPresent(conn -> {
            try (PreparedStatement statement = conn.prepareStatement(sql)) {

                statement.setInt(1, boxNum);
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    String number = resultSet.getString("carNum");
                    LocalDate rentStartDate =  resultSet.getObject("rental_start_date", LocalDate.class);
                    LocalDate rentEndDate = resultSet.getObject("rental_end_date", LocalDate.class);
                    Long receiptNumber = resultSet.getLong("rectNum");
                    Long modelId = resultSet.getLong("model_id");
                    Long clientId = resultSet.getLong("client_id");
                    Car car = new Car(number, modelId,clientId,boxNum,receiptNumber,rentStartDate,rentEndDate);

                    cars.add(car);
                }

            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        });
        return cars;
    }

    public List<Car> getCarsForModelId(Long modelId) {
        List<Car> cars = new ArrayList<>();
        String sql = "SELECT * FROM car WHERE model_id = ? ";
        connection.ifPresent(conn -> {
            try (PreparedStatement statement = conn.prepareStatement(sql)) {

                statement.setLong(1, modelId);
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    String number = resultSet.getString("carNum");
                    LocalDate rentStartDate =  resultSet.getObject("rental_start_date", LocalDate.class);
                    LocalDate rentEndDate = resultSet.getObject("rental_end_date", LocalDate.class);
                    Long receiptNumber = resultSet.getLong("rectNum");
                    Integer boxId = resultSet.getInt("boxNum");
                    Long clientId = resultSet.getLong("client_id");
                    Car car = new Car(number, modelId,clientId,boxId,receiptNumber,rentStartDate,rentEndDate);

                    cars.add(car);
                }

            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        });
        return cars;
    }

    public List<Pair<Integer>> getRentIntervalsStatistic() {
        List<Pair<Integer>> pairs = new ArrayList<>();
        String sql = "select rental_end_date - rental_start_date as i, count(carNum) from car group by i order by i";
        connection.ifPresent(conn -> {
            try (Statement statement = conn.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {
                while (resultSet.next()) {
                    int  interval = resultSet.getInt(1);
                    int count = resultSet.getInt(2);
                    pairs.add(new Pair<>(interval, count));
                }
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        });
        return pairs;
    }

    public double getAverageRentInterval() {
        AtomicReference<Double> result = new AtomicReference<>((double) 0);
        String sql = "select AVG(rental_end_date - rental_start_date) from car";
        connection.ifPresent(conn -> {
            try (Statement statement = conn.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {
                if (resultSet.next()) {
                    result.set(resultSet.getDouble(1));
                }
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        });
        return result.get();
    }
}
