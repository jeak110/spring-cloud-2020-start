package my.test.spring.examinator;

import my.test.spring.examinator.model.Exercise;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
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
    @Autowired
    private DiscoveryClient discoveryClient;
    @Autowired
    private LoadBalancerClient balancerClient;

    public ExaminatorRestController() {
    }

    @GetMapping("/exam")
    public List<Exercise> getExercises(@RequestParam Map<String, String> map) {
        List<String> services = discoveryClient.getServices();

        List<Exercise> result = new ArrayList<>();
        for (String service : services) {
            String counterStr = map.get(service.toLowerCase());
            Integer counter = counterStr != null ? Integer.parseInt(counterStr) : 2;

            ServiceInstance instance = balancerClient.choose(service);

            List<Exercise> exercises = getExercisesForSection(counter, instance.getHost() + ":" + instance.getPort());
            result.addAll(exercises);
        }
        return result;
    }

    private List<Exercise> getExercisesForSection(Integer counter, String baseUrl) {
        RestTemplate restTemplate = new RestTemplate();
        Exercise[] arr = restTemplate.getForObject("http://" + baseUrl + "/exercise/random?counter=" + counter, Exercise[].class);
        return asList(arr);
    }
}
