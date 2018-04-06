package service;

import com.fasterxml.jackson.databind.ObjectMapper;
import domain.CarTracker;

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
    CarTrackerService carTrackerService;
    List<String> files = new ArrayList<>();


    public CarTracker readJsonFiles() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        CarTracker tracker = mapper.readValue(new File("C:\\Users\\Gebruiker\\Documents\\Development\\ESD6.NL.DisplacementSystem\\TestFiles\\test.json"), CarTracker.class);
        System.out.println(tracker.toString());
        return tracker;
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
