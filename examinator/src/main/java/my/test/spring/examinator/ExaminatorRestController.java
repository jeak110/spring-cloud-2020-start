package my.test.spring.examinator;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import my.test.spring.examinator.model.Exercise;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.netflix.ribbon.RibbonClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

@RestController
public class ExaminatorRestController {
    @Autowired
    private DiscoveryClient discoveryClient;
    @Autowired
    private RestTemplate restTemplate;

    public ExaminatorRestController() {
    }

    @GetMapping("/exam")
    public List<Exercise> getExercises(@RequestParam Map<String, String> map) {
        List<String> services = discoveryClient.getServices().stream()
                .filter(x -> !"configserver".equalsIgnoreCase(x))
                .collect(toList());

        List<Exercise> result = new ArrayList<>();
        for (String service : services) {
            String counterStr = map.get(service.toLowerCase());
            Integer counter = counterStr != null ? Integer.parseInt(counterStr) : 2;

            Exercise[] arr = restTemplate.getForObject("http://" + service +
                    "/exercise/random?counter=" + counter, Exercise[].class);

            List<Exercise> exercises = asList(arr);
            result.addAll(exercises);
        }
        return result;
    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
