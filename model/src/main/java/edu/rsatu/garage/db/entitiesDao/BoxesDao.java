package db.entitiesDao;
import db.Dao;
import db.JdbcConnection;
import entities.Box;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

    @Override
    public Optional save(Box box) {
        String message = "The box to be added should not be null";
        Box nonNullBox = Objects.requireNonNull(box, message);
        String sql = "INSERT INTO "
                + "box(boxnum,rentprice) "
                + "VALUES(?, ?)";

        return connection.flatMap(conn -> {
            Optional generatedId = Optional.empty();

            try (PreparedStatement statement =
                         conn.prepareStatement(
                                 sql,
                                 Statement.RETURN_GENERATED_KEYS)) {
                statement.setInt(1,nonNullBox.getBoxNum());
                statement.setFloat(2,nonNullBox.getRentPrice());

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

}
