package service;

import com.fasterxml.jackson.databind.ObjectMapper;
import domain.CarTracker;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class JsonReader {

    CarTrackerService carTrackerService;
    List<String> files = new ArrayList<>();


    public void readJsonFiles() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        CarTracker tracker = mapper.readValue(new File("E:\\Fontys\\ESD\\ESD6_RekeningRijden_NL\\DisplacementSystem\\test.json"), CarTracker.class);
        carTrackerService.runAllChecks(tracker);
    }

    public void getAllFiles(String fileType) throws IOException {
        Files.newDirectoryStream(Paths.get("."),
                path -> path.toString().endsWith(fileType))
                .forEach(System.out::println);
    }
}
