package my.test.spring.examinator;

import com.netflix.discovery.EurekaClient;
import my.test.spring.examinator.model.Exercise;
import my.test.spring.examinator.section.Section;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.http.RestTemplateTransportClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

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
        List<Section> sections = getSections();

        List<Exercise> result = new ArrayList<>();
        for (Section section : sections) {
            String counterStr = map.get(section.getName());
            Integer counter = counterStr != null ? Integer.parseInt(counterStr) : 2;
            List<Exercise> exercises = getExercisesForSection(counter, section.getName());
            result.addAll(exercises);
        }
        return result;
    }

    private List<Exercise> getExercisesForSection(Integer counter, String serviceId) {
        Exercise[] arr = restTemplate.getForObject("http://" + serviceId + "/exercise/random?counter=" + counter, Exercise[].class);
        return asList(arr);
    }

    private List<Section> getSections() {
        return discoveryClient.getServices().stream()
                .map(serviceId -> discoveryClient.getInstances(serviceId))
                .map(instances -> instances.get(0))
                .map(instance -> new Section(instance.getServiceId().toLowerCase(), instance.getHost() + ":" + instance.getPort()))
                .collect(Collectors.toList());
    }

    @Bean @LoadBalanced
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }}
