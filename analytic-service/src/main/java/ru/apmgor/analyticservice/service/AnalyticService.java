package ru.apmgor.analyticservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Sinks;
import ru.apmgor.analyticservice.dto.AnalyticProductDto;
import ru.apmgor.analyticservice.mapper.AnalyticMapper;
import ru.apmgor.analyticservice.repository.AnalyticRepository;

@Service
@RequiredArgsConstructor
public final class AnalyticService {

    private final AnalyticRepository repository;
    private final Sinks.Many<AnalyticProductDto> sink;

    @Scheduled(fixedDelay = 3000)
    public void getAnalyticReport() {
        repository.findAll()
                .map(AnalyticMapper::toDto)
                .doOnNext(sink::tryEmitNext)
                .subscribe();
    }
}
