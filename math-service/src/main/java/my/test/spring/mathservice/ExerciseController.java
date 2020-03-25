package my.test.spring.mathservice;

import my.test.spring.mathservice.model.Exercise;
import org.apache.el.stream.Stream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RefreshScope
@RestController
@RequestMapping("/exercise")
public class ExerciseController {
    @Value("${calculation.score}")
    private Integer calculationScore;

    @Value("${server.port}")
    private String port;

    @GetMapping("/random")
    public List<Exercise> random(@RequestParam Integer counter) {
        System.out.println("random -> math:" + port);
        List<Exercise> result = new ArrayList<>();
        for (int i = 0; i < counter; i++) {
            result.add(generateExercise());
        }
        return result;
    }

    private Exercise generateExercise() {
        Exercise exercise = new Exercise();
        int a = (int) (Math.random() * calculationScore);
        int b = (int) (Math.random() * calculationScore);

        exercise.setQuestion(a + " + " + b + " = ?");
        exercise.setAnswer("" + (a + b));
        return exercise;
    }
}
