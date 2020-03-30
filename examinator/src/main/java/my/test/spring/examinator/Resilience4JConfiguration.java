package my.test.spring.examinator;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class Resilience4JConfiguration {

    @Autowired(required = false)
    private List<Customizer<Resilience4JCircuitBreakerFactory>> customizers = new ArrayList<>();

    public static final String CB_SLOW = "cbSlow";

    @Bean
    public Resilience4JCircuitBreakerFactory resilience4jCircuitBreakerFactory() {
        Resilience4JCircuitBreakerFactory factory = new Resilience4JCircuitBreakerFactory();
        customizers.forEach(customizer -> customizer.customize(factory));
        return factory;
    }

    @Bean
    public Customizer<Resilience4JCircuitBreakerFactory> defaultCustomizer() {
        return factory -> factory.configureDefault(id -> {
            CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig
                    .custom()
                    .slidingWindowSize(5)
                    .permittedNumberOfCallsInHalfOpenState(5)
                    .failureRateThreshold(51.0F)
                    .waitDurationInOpenState(Duration.ofMillis(52))
                    .slowCallDurationThreshold(Duration.ofMillis(203))
                    .slowCallRateThreshold(54.0F)
                    .build();
            TimeLimiterConfig timeLimiterConfig = TimeLimiterConfig
                    .custom()
                    .timeoutDuration(Duration.ofSeconds(4))
                    .build();
            return new Resilience4JConfigBuilder(id)
                    .circuitBreakerConfig(circuitBreakerConfig)
                    .timeLimiterConfig(timeLimiterConfig)
                    .build();
        });

    }

    @Bean
    public Customizer<Resilience4JCircuitBreakerFactory> slowCustomizer() {
        return factory -> factory.configure(builder -> builder
                        .circuitBreakerConfig(
                                CircuitBreakerConfig.custom()
                                                    .slidingWindowSize(11)
                                                    .build())
                        .timeLimiterConfig(TimeLimiterConfig
                                .custom()
                                .timeoutDuration(Duration.ofSeconds(16))
                                .build()),
                CB_SLOW);
    }

}
