package ar.com.wenance.job.btc.controllers;

import ar.com.wenance.job.btc.dtos.SummaryStatistics;
import ar.com.wenance.job.btc.entities.BtcUsd;
import ar.com.wenance.job.btc.events.BtcUsdCreated;
import ar.com.wenance.job.btc.processors.BtcUsdCreatedEventProcessor;
import ar.com.wenance.job.btc.services.BtcUsdService;
import ar.com.wenance.job.page.PageSupport;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

import static ar.com.wenance.job.page.PageSupport.DEFAULT_PAGE_SIZE;
import static ar.com.wenance.job.page.PageSupport.FIRST_PAGE_NUM;

@RestController
@Slf4j
@Api(tags = "BTC-USD")
public class BtcUsdController {

    private final BtcUsdService btcUsdService;
    private final Flux<BtcUsdCreated> events;
    private final ModelMapper modelMapper;

    public BtcUsdController(BtcUsdService btcUsdService, BtcUsdCreatedEventProcessor processor, ModelMapper modelMapper) {
        this.btcUsdService = btcUsdService;
        this.events = Flux.create(processor).share();
        this.modelMapper = modelMapper;
    }

    @GetMapping(value = "/btc", produces = "text/event-stream;charset=UTF-8")
    public Flux<BtcUsd> stream() {
        log.info("Start listening to the BtcUsd collection.");
        return this.events.map(btcUsdCreated -> modelMapper.map(btcUsdCreated.getSource(), BtcUsd.class));
    }

    @GetMapping("/btc/pageable")
    public Mono<PageSupport<BtcUsd>> getBtcUsdPage(@RequestParam(name = "page", defaultValue = FIRST_PAGE_NUM) int page,
                                                   @RequestParam(name = "size", defaultValue = DEFAULT_PAGE_SIZE) int size,
                                                   @RequestParam(name = "from", required = false)
                                                   @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS") LocalDateTime from,
                                                   @RequestParam(name = "to", required = false)
                                                   @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS") LocalDateTime to) {
        return btcUsdService.getBtcUsdPage(PageRequest.of(page, size), from, to);
    }

    @GetMapping("/btc/value")
    public Mono<BtcUsd> getBtcUsdByLocalDateTime(@RequestParam(name = "timestamp")
                                                 @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS") LocalDateTime timestamp) {
        return btcUsdService.getBtcUsdByLocalDateTime(timestamp);
    }

    @GetMapping("/btc/avg")
    public Mono<SummaryStatistics> getAvgBtcUsd(@RequestParam(name = "from")
                                                @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS") LocalDateTime from,
                                                @RequestParam(name = "to")
                                                @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS") LocalDateTime to) {
        return btcUsdService.getAvgBtcUsdByLocalDateTime(from, to);
    }
}
