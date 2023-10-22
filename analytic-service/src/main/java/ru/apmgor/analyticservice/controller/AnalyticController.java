package ru.apmgor.analyticservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import ru.apmgor.analyticservice.dto.AnalyticProductDto;
import ru.apmgor.analyticservice.service.AnalyticBroadcastService;

import java.util.List;

@RestController
@RequestMapping("/analytics")
@RequiredArgsConstructor
public final class AnalyticController {

    private final AnalyticBroadcastService service;

    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<List<AnalyticProductDto>> analytics() {
        return service.getAnalytics();
    }
}
