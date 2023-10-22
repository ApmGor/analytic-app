package ru.apmgor.analyticservice.service;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import ru.apmgor.analyticservice.dto.AnalyticProductDto;
import ru.apmgor.analyticservice.mapper.AnalyticMapper;
import ru.apmgor.analyticservice.repository.AnalyticRepository;

import java.time.Duration;
import java.util.List;
import java.util.function.Predicate;

@Service
@RequiredArgsConstructor
public final class AnalyticBroadcastService {

    private final AnalyticRepository repository;
    @Getter
    private Flux<List<AnalyticProductDto>> analytics;

    @PostConstruct
    public void broadcast() {
        analytics = repository.findTop3ByCount()
                .map(AnalyticMapper::toDto)
                .collectList()
                .filter(Predicate.not(List::isEmpty))
                .repeatWhen(repeat -> repeat.delayElements(Duration.ofSeconds(3)))
                .distinctUntilChanged()
                .cache(1);
    }
}












