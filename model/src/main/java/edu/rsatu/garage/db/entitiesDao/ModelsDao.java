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
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ModelsDao implements Dao<Model, Integer>{

    private static final Logger LOGGER =
            Logger.getLogger(ModelsDao.class.getName());
    private final Optional<Connection> connection;

    public ModelsDao() {
        this.connection = JdbcConnection.getConnection();
    }

    @Override
    public Optional get(Integer id) {
        return Optional.empty();
    }

    @Override
    public Collection getAll() {
        return null;
    }

    @Override
    public Optional save(Model model) {
        return Optional.empty();
    }

    @Override
    public void update(Model model) {

    }

    @Override
    public void delete(Model model) {

    }
}
