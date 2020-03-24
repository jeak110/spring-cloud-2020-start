package my.test.spring.examinator;

import my.test.spring.examinator.model.Exercise;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

@Service
public class ExercisesServiceCaller {

    private final RestTemplate restTemplate;

    private final CircuitBreakerFactory<?, ?> circuitBreakerFactory;

    @Autowired
    public ExercisesServiceCaller(RestTemplate restTemplate,
                                  @Qualifier("resilience4jCircuitBreakerFactory") CircuitBreakerFactory<?, ?> circuitBreakerFactory) {
        this.restTemplate = restTemplate;
        this.circuitBreakerFactory = circuitBreakerFactory;
    }

    public List<Exercise> getExercisesForSection(Integer counter, String serviceName, String... additionalParams) {
        String url = "http://" + serviceName + "/exercise/random?counter=" + counter + "&params=" + String
                .join(",", additionalParams);
        return callExerciseService(url);
    }

    /**
     * see https://dzone.com/articles/resilience4j-intro for more examples
     */
    private List<Exercise> callExerciseService(String url) {
        Supplier<Exercise[]> serviceCaller = () -> restTemplate.getForObject(url, Exercise[].class);
        Exercise[] exercises;
        exercises = circuitBreakerFactory.create("getExercisesForSection")
                                         .run(serviceCaller, this::getDefExercises);

        /*try {
            exercises = serviceCaller.get();
        } catch (Exception e) {
            exercises= getDefExercises(e);
        }*/
        return Arrays.asList(exercises);
    }

    private Exercise[] getDefExercises(Throwable throwable) {
        return new Exercise[]{new Exercise()
                .setQuestion("no question")
                .setAnswer(throwable.toString())};
    }
}
