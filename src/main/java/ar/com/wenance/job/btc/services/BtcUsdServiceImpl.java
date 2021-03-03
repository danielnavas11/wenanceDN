package ar.com.wenance.job.btc.services;


import ar.com.wenance.job.btc.dtos.BtcUsdResponse;
import ar.com.wenance.job.btc.dtos.SummaryStatistics;
import ar.com.wenance.job.btc.entities.BtcUsd;
import ar.com.wenance.job.btc.events.BtcUsdCreated;
import ar.com.wenance.job.btc.repositories.BtcUsdRepository;
import ar.com.wenance.job.page.PageSupport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BtcUsdServiceImpl implements BtcUsdService {

    private final BtcUsdRepository btcUsdRepository;
    private final RestTemplate restTemplate;
    private final ApplicationEventPublisher publisher;

    @Value("${endpoint.btc}")
    String urlBtc;

    @Autowired
    public BtcUsdServiceImpl(BtcUsdRepository btcUsdRepository, RestTemplate restTemplate, ApplicationEventPublisher publisher) {
        this.btcUsdRepository = btcUsdRepository;
        this.restTemplate = restTemplate;
        this.publisher = publisher;
    }

    @Override
    public void obtainsBtcUsd() {
        HttpEntity<String> entity = new HttpEntity<>(new HttpHeaders());
        ResponseEntity<BtcUsdResponse> response = restTemplate.exchange(urlBtc, HttpMethod.GET, entity, BtcUsdResponse.class);
        if (response.getBody() == null) {
            return;
        }
        BtcUsd btcUsd = btcUsdRepository.saveAndFlush(new BtcUsd(null, response.getBody().getLprice(), response.getBody().getCurr1(),
                response.getBody().getCurr2(), LocalDateTime.now()));
        log.info("Saved Btc-Usd: {}", btcUsd.toString());
        this.publisher.publishEvent(new BtcUsdCreated(btcUsd));
    }

    @Override
    public Mono<PageSupport<BtcUsd>> getBtcUsdPage(PageRequest page, LocalDateTime from, LocalDateTime to) {
        List<BtcUsd> btcUsdList;
        if (from == null && to == null) {
            btcUsdList = btcUsdRepository.findAll();
        } else {
            btcUsdList = btcUsdRepository.findByLocalDateTimeBetween(from, to);
        }
        return Flux.fromIterable(btcUsdList)
                .collectList()
                .map(list -> new PageSupport<>(
                        list.stream()
                                .skip(page.getPageNumber() * page.getPageSize())
                                .limit(page.getPageSize())
                                .collect(Collectors.toList()),
                        page.getPageNumber(), page.getPageSize(), list.size()));
    }

    @Override
    public Mono<BtcUsd> getBtcUsdByLocalDateTime(LocalDateTime timestamp) {
        BtcUsd byLocalDateTimeIs = btcUsdRepository.findByLocalDateTime(timestamp);
        return byLocalDateTimeIs != null ? Mono.just(byLocalDateTimeIs) : Mono.empty();
    }

    @Override
    public Mono<SummaryStatistics> getAvgBtcUsdByLocalDateTime(LocalDateTime from, LocalDateTime to) {
        List<BtcUsd> btcUsdList = btcUsdRepository.findByLocalDateTimeBetween(from, to);
        if (btcUsdList.isEmpty())
            return Mono.empty();
        IntSummaryStatistics stats = btcUsdList.stream()
                .mapToInt((x) -> x.getLprice().intValue())
                .summaryStatistics();
        SummaryStatistics summaryStatistics = new SummaryStatistics();
        summaryStatistics.setCount(stats.getCount());
        summaryStatistics.setSum(stats.getSum());
        summaryStatistics.setMin(stats.getMin());
        summaryStatistics.setMax(stats.getMax());
        summaryStatistics.setAverage(stats.getAverage());
        summaryStatistics.setPercentToMax(100 - ((stats.getAverage() / stats.getMax()) * 100));
        summaryStatistics.setPercentToMin(100 - ((stats.getMin() / stats.getAverage()) * 100));
        log.info(summaryStatistics.toString());
        return Mono.just(summaryStatistics);
    }
}
