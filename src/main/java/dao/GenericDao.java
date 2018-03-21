package dao;

import java.util.List;

public interface GenericDao<T> {

    T create(T t);

    T update(T t);

    void deleteById(Long id);

    void delete(T t);

    T findById(Long id);

    List<T> findAll();
}
