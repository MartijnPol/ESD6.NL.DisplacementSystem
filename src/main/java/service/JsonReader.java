package service;

import com.fasterxml.jackson.databind.ObjectMapper;
import domain.CarTracker;
import domain.CarTrackerRule;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class JsonReader {

    @Inject
    private CarTrackerService carTrackerService;
    List<String> files = new ArrayList<>();


    public CarTracker readJsonFiles() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        CarTracker tracker = mapper.readValue(new File("E:\\Fontys\\ESD\\ESD6.NL.DisplacementSystem\\TestFiles\\test.json"), CarTracker.class);
        System.out.println("JsonReader" + " " + tracker.toString());
        return tracker;
    }

    public CarTracker updateCarTracker(CarTracker newCarTracker) {
        CarTracker foundCarTracker = carTrackerService.findById(newCarTracker.getId());

        List<CarTrackerRule> carTrackerRulesTest = new ArrayList<>();
        for (CarTrackerRule rule : newCarTracker.getRules()) {
            carTrackerRulesTest.add(rule);
        }

        for (CarTrackerRule rule : newCarTracker.getRules()) {
            foundCarTracker.getRules().add(rule);
        }

        foundCarTracker.setTotalRules(foundCarTracker.getTotalRules() + new Long(carTrackerRulesTest.size()));
//        carTrackerTest.setTotalRules(carTracker.getTotalRules()+ new Long(carTrackerRulesTest.size()));

        return carTrackerService.update(foundCarTracker);
    }

//        public JsonObject toJson() {
//        return Json.createObjectBuilder()
//                .add("id", this.id)
//                .add("username", this.username)
//                .add("email", this.email)
//                .add("group", this.group.toString())
//                .add("profile", Json.createObjectBuilder()
//                        .add("firstName", profile.getFirstName())
//                        .add("lastName", profile.getLastName()).build())
//                .build();
//    }

    public void getAllFiles(String fileType) throws IOException {
        Files.newDirectoryStream(Paths.get("."),
                path -> path.toString().endsWith(fileType))
                .forEach(System.out::println);
    }
}