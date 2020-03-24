package my.test.spring.mathservice;

import my.test.spring.mathservice.model.Exercise;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/exercise")
public class ExerciseController {

    @GetMapping("/random")
    public List<Exercise> random(@RequestParam Integer counter, @RequestParam(required = false) String... params) throws InterruptedException {
        List<String> paramsList = Arrays.asList(params);
        if (paramsList.contains("throwException")) {
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
        int a = (int) (Math.random() * 100);
        int b = (int) (Math.random() * 100);

        exercise.setQuestion(a + " + " + b + " = ?");
        exercise.setAnswer("" + (a + b));
        return exercise;
    }
}
