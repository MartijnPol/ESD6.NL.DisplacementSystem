package dao.jpa;

import dao.JPA;
import dao.ProcessedCarsDao;
import domain.ProcessedCars;

import javax.ejb.Stateless;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
@JPA
public class ProcessedCarsDaoJPAImpl extends GenericDaoJPAImpl<ProcessedCars> implements ProcessedCarsDao {

    @Override
    public List<ProcessedCars> getNotProcessedDataById(long id) {
        TypedQuery<ProcessedCars> query = entityManager.createNamedQuery("processedCars.getNotProcessedDataById", ProcessedCars.class);
        query.setParameter("id", id);
        return query.getResultList();
    }
}
