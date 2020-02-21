package my.test.spring.theologyservice;

import my.test.spring.theologyservice.model.Exercise;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/exercise")
public class ExerciseController {
    @Autowired
    private ExerciseRestRepository exerciseRestRepository;

    @GetMapping("/random")
    public List<Exercise> random(@RequestParam Integer counter) {
        List<Exercise> exercises = exerciseRestRepository.findAll()
                .stream()
                .limit(counter)
                .collect(toList());
        Collections.shuffle(exercises);
        return exercises;
    }
}
