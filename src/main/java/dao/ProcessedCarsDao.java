package dao;

import domain.ProcessedCar;

import java.util.List;

public interface ProcessedCarsDao extends GenericDao<ProcessedCar> {

    List<ProcessedCar> getNotProcessedDataById(String id);
}
