package my.test.spring.examinator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class ExaminatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExaminatorApplication.class, args);
    }
}
