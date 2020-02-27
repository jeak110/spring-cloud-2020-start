package my.test.spring.mathservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class MathServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MathServiceApplication.class, args);
	}

}
