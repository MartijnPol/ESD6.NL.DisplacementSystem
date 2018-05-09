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
        this.queue.add(carTracker);

        if (this.queue.size() == 20) {
            ProcessQueue(this.queue);
            this.queue = new LinkedList<>();
        }
    }

    public void ProcessQueue(LinkedList<CarTracker> carTrackers) {
        for (CarTracker data : carTrackers) {
            this.carTrackerService.create(data);
        }
    }
}
