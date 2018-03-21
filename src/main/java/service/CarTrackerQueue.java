package service;

import domain.CarTracker;

import javax.inject.Inject;
import java.util.LinkedList;


public class CarTrackerQueue {

    @Inject
    private CarTrackerService carTrackerService;

    LinkedList<CarTracker> queue = new LinkedList<>();

    public CarTrackerQueue() {
    }

    public void addQueue(CarTracker carTracker) {
        queue.add(carTracker);

        if (queue.size() == 20) {
            ProcessQueue(queue);
            queue = new LinkedList<>();
        }
    }

    public void ProcessQueue(LinkedList<CarTracker> carTrackers) {
        for (CarTracker data : carTrackers) {
            carTrackerService.create(data);
        }
    }
}
