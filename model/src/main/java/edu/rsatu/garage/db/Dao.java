package db;

import java.util.Collection;
import java.util.Optional;

public interface Dao {
    Optional get(int id);
    Collection getAll();
    <T> Optional save(T t);
    <T> void update(T t);
    <T> void delete(T t);
}
