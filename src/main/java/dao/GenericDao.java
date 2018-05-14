package dao;

import javax.persistence.TypedQuery;
import java.util.List;

public interface GenericDao<T> {

    T create(T t);

    T update(T t);

    void deleteById(Long id);

    void delete(T t);

    T findById(Long id);

    T oneResult(TypedQuery<T> query);

    List<T> findAll();
}
