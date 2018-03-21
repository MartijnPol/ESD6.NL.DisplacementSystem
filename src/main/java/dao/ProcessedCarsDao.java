package dao;

import domain.ProcessedCars;

import java.util.List;

public interface ProcessedCarsDao extends GenericDao<ProcessedCars> {

    List<ProcessedCars> getNotProcessedDataById(long id);
}
