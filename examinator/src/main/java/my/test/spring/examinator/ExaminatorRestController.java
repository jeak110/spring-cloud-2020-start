package my.test.spring.examinator;

import my.test.spring.examinator.model.Exercise;
import my.test.spring.examinator.section.Section;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;

@RestController
public class ExaminatorRestController {
    public ExaminatorRestController() {
    }

    @GetMapping("/exam")
    public List<Exercise> getExercises(@RequestParam Map<String, String> map) {
        List<Section> sections = getSections();

        List<Exercise> result = new ArrayList<>();
        for (Section section : sections) {
            String counterStr = map.get(section.getName());
            Integer counter = counterStr != null ? Integer.parseInt(counterStr) : 2;
            List<Exercise> exercises = getExercisesForSection(counter, section.getBaseUrl());
            result.addAll(exercises);
        }
        return result;
    }

    private List<Exercise> getExercisesForSection(Integer counter, String baseUrl) {
        RestTemplate restTemplate = new RestTemplate();
        Exercise[] arr = restTemplate.getForObject("http://" + baseUrl + "/exercise/random?counter=" + counter, Exercise[].class);
        return asList(arr);
    }

    private List<Section> getSections() {
        return asList(
                new Section("math", "localhost:8090"),
                new Section("theology", "localhost:8100"));

    }
}
