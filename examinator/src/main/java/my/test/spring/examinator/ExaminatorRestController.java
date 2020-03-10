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
import static java.util.stream.Collectors.toList;

@RestController
public class ExaminatorRestController {
    @Autowired
    private DiscoveryClient discoveryClient;
    @Autowired
    private LoadBalancerClient loadBalancer;

    public ExaminatorRestController() {
    }

    @GetMapping("/exam")
    public List<Exercise> getExercises(@RequestParam Map<String, String> map) {
        List<Exercise> result = new ArrayList<>();
        List<String> services = discoveryClient.getServices().stream()
                .map(String::toLowerCase)
                .collect(toList());

        for (String serviceId : services) {
            String counterStr = map.get(serviceId);
            Integer counter = counterStr != null ? Integer.parseInt(counterStr) : 2;

            ServiceInstance serviceInstance = loadBalancer.choose(serviceId);

            RestTemplate restTemplate = new RestTemplate();
            Exercise[] arr = restTemplate.getForObject(
                    "http://" + serviceInstance.getHost() + ":" + serviceInstance.getPort() +
                            "/exercise/random?counter=" + counter, Exercise[].class);
            List<Exercise> exercises = asList(arr);

            result.addAll(exercises);
        }
        return result;
    }
}