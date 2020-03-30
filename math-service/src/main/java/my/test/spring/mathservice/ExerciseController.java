package my.test.spring.mathservice;

import my.test.spring.mathservice.model.Exercise;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/exercise")
public class ExerciseController {

    private final Random random = new Random();

    @GetMapping("/random")
    public List<Exercise> random(@RequestParam Integer counter,
                                 @RequestParam(required = false) String... params) throws InterruptedException {
        List<String> paramsList = Arrays.asList(params);
        if (paramsList.contains("timeout")) {
            for (int i = 0; i < 10; i++) {
                Thread.sleep(1000);
            }
        }
        if (paramsList.contains("exception")) {
            throw new InterruptedException(String.valueOf(new Date()));
        }
        List<Exercise> result = new ArrayList<>();
        for (int i = 0; i < counter; i++) {
            result.add(generateExercise());
        }
        return result;
    }

    private Exercise generateExercise() {
        Exercise exercise = new Exercise();
        int a = random.nextInt(100);
        int b = random.nextInt(100);

        exercise.setQuestion(a + " + " + b + " = ?");
        exercise.setAnswer("" + (a + b));
        return exercise;
    }
}
