package dao.jpa;

import dao.GenericDao;
import dao.JPADisplacementSystem;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

@Stateless
@JPADisplacementSystem
public abstract class GenericDaoJPAImpl<T> implements GenericDao<T> {

    @PersistenceContext(unitName = "CarTackerPU")
    protected EntityManager entityManager;

    private Class<T> type;

    public GenericDaoJPAImpl() {
        Type t = getClass().getGenericSuperclass();
        ParameterizedType pt = (ParameterizedType) t;
        type = (Class) pt.getActualTypeArguments()[0];
    }

    public T create(T t) {
        this.entityManager.persist(t);
        return t;
    }

    public T update(T t) {
        return this.entityManager.merge(t);
    }

    public void deleteById(Long id) {
        this.entityManager.remove(findById(id));
    }

    public void delete(T t) {
        this.entityManager.remove(this.entityManager.merge(t));
    }

    public T findById(Long id) {
        return this.entityManager.find(type, id);
    }

    public List<T> findAll() {
        return entityManager.createQuery("SELECT t FROM " + type.getSimpleName() + " t", type).getResultList();
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

}
