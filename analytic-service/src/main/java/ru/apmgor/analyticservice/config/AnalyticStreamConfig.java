package ru.apmgor.analyticservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import ru.apmgor.analyticservice.dto.AnalyticProductDto;

@Configuration
public class AnalyticStreamConfig {

    @Bean
    public Sinks.Many<AnalyticProductDto> sink() {
        return Sinks.many().replay().limit(1);
    }

    @Bean
    public Flux<AnalyticProductDto> analyticProductFlux(final Sinks.Many<AnalyticProductDto> sink) {
        return sink.asFlux();
    }
}
