package app.hotel.generic;

import java.util.List;
import java.util.Optional;

public interface GenericService<T extends Object> {

    Optional<T> find(String id);

    List<T> findAll();

    void insert(T entity);

    void update(T entity);

    void delete(T entity);

}
