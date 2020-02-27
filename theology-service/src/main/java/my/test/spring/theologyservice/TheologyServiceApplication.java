package my.test.spring.theologyservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class TheologyServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TheologyServiceApplication.class, args);
    }

}
