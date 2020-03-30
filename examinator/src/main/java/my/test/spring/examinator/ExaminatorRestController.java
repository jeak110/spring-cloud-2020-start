package my.test.spring.examinator;

import my.test.spring.examinator.model.Exercise;
import my.test.spring.examinator.section.Section;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
public class ExaminatorRestController {

    private final DiscoveryClient discoveryClient;

    private final ExercisesServiceCaller exercisesServiceCaller;

    @Autowired
    public ExaminatorRestController(DiscoveryClient discoveryClient,
                                    ExercisesServiceCaller exercisesServiceCaller) {
        this.discoveryClient = discoveryClient;
        this.exercisesServiceCaller = exercisesServiceCaller;
    }

    /**
     * localhost:8080/exam?math=3&theology=2&additionalParams=timeout,exception
     *
     * @param map service name and exercises count
     * @return list of exercises
     */
    @GetMapping("/exam")
    public List<Exercise> getExercises(@RequestParam Map<String, String> map) {
        List<Section> sections = getSections();

        List<Exercise> result = new ArrayList<>();
        for (Section section : sections) {
            String counterStr = map.get(section.getName());
            Integer counter = Objects.isNull(counterStr) ? section.getExerciseCount() : Integer.parseInt(counterStr);
            String additionalParams = map.get("additionalParams");
            List<Exercise> exercises = exercisesServiceCaller.getExercisesForSection(counter, section.getName(), additionalParams);
            result.addAll(exercises);
        }
        return result;
    }

    private List<Section> getSections() {
        return discoveryClient.getServices().stream()
                              .map(discoveryClient::getInstances)
                              .map(instances -> instances.get(0))
                              .map(instance -> new Section(
                                      instance.getServiceId().toLowerCase(),
                                      instance.getHost() + ":" + instance.getPort(),
                                      2))
                              .collect(Collectors.toList());
    }
}
