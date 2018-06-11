package dao.jpa;

import dao.JPA;
import dao.ProcessedCarsDao;
import domain.ProcessedCar;

import javax.ejb.Stateless;
import java.util.List;

@Stateless
@JPA
public class ProcessedCarsDaoJPAImpl extends GenericDaoJPAImpl<ProcessedCar> implements ProcessedCarsDao {

    @Override
    public List<ProcessedCar> getNotProcessedDataById(String id) {
        return getEntityManager().createNamedQuery("processedCars.getNotProcessedDataById", ProcessedCar.class)
                .setParameter("id", id)
                .getResultList();
    }
}
