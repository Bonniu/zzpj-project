package app.hotel.services;

import java.util.List;
import java.util.Optional;

public interface GenericService<T extends Object> {


    Optional<T> find(String id);

    List<T> findAll();

    T insert(T entity);

    void update(T entity);

    void delete(T entity);

}
