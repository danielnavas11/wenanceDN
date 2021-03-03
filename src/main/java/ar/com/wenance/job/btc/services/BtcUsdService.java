package ar.com.wenance.job.btc.services;

import ar.com.wenance.job.btc.dtos.SummaryStatistics;
import ar.com.wenance.job.btc.entities.BtcUsd;
import ar.com.wenance.job.page.PageSupport;
import org.springframework.data.domain.PageRequest;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.IntSummaryStatistics;

public interface BtcUsdService {
    void obtainsBtcUsd();

    Mono<PageSupport<BtcUsd>> getBtcUsdPage(PageRequest of,LocalDateTime from, LocalDateTime to);

    Mono<BtcUsd> getBtcUsdByLocalDateTime(LocalDateTime timestamp);

    Mono<SummaryStatistics> getAvgBtcUsdByLocalDateTime(LocalDateTime from, LocalDateTime to);
}
