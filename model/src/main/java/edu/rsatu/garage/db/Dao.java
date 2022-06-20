package edu.rsatu.garage.db;

import java.util.Collection;
import java.util.Optional;

public interface Dao<T, K> {
    Optional get(K id);
    Collection getAll();
    Optional save(T t);
    void update(T t);
    void delete(T t);
}
