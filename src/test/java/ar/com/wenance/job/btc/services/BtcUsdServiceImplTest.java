package ar.com.wenance.job.btc.services;

import ar.com.wenance.job.btc.dtos.SummaryStatistics;
import ar.com.wenance.job.btc.entities.BtcUsd;
import ar.com.wenance.job.btc.repositories.BtcUsdRepository;
import ar.com.wenance.job.page.PageSupport;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.Collections;

import static org.mockito.Mockito.*;

class BtcUsdServiceImplTest {
    @Mock
    BtcUsdRepository btcUsdRepository;
    @Mock
    RestTemplate restTemplate;
    @Mock
    ApplicationEventPublisher publisher;
    @Mock
    Logger log;
    @InjectMocks
    BtcUsdServiceImpl btcUsdServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetBtcUsdPage() {
        when(btcUsdRepository.findByLocalDateTimeBetween(any(), any())).thenReturn(Collections.singletonList(new BtcUsd(1L, new BigDecimal(45000), "curr1", "curr2", LocalDateTime.of(2021, Month.MARCH, 3, 19, 4, 22))));

        Mono<PageSupport<BtcUsd>> result = btcUsdServiceImpl.getBtcUsdPage(PageRequest.of(0, 20), LocalDateTime.of(2021, Month.MARCH, 3, 19, 4, 22), LocalDateTime.of(2021, Month.MARCH, 3, 19, 4, 22));
        Assertions.assertNotNull(result);
    }
    @Test
    void testGetBtcUsdPageDateNull() {
        when(btcUsdRepository.findAll()).thenReturn(Collections.singletonList(new BtcUsd(1L, new BigDecimal(45000), "curr1", "curr2", LocalDateTime.of(2021, Month.MARCH, 3, 19, 4, 22))));

        Mono<PageSupport<BtcUsd>> result = btcUsdServiceImpl.getBtcUsdPage(PageRequest.of(0, 20), null, null);
        Assertions.assertNotNull(result);
    }

    @Test
    void testGetBtcUsdByLocalDateTime() {
        when(btcUsdRepository.findByLocalDateTime(any())).thenReturn(new BtcUsd(1L, new BigDecimal(45000), "curr1", "curr2", LocalDateTime.of(2021, Month.MARCH, 3, 19, 4, 22)));

        Mono<BtcUsd> result = btcUsdServiceImpl.getBtcUsdByLocalDateTime(LocalDateTime.of(2021, Month.MARCH, 3, 19, 4, 22));
        Assertions.assertNotNull(result);
    }

    @Test
    void testGetAvgBtcUsdByLocalDateTime() {
        when(btcUsdRepository.findByLocalDateTimeBetween(any(), any())).thenReturn(Collections.singletonList(new BtcUsd(1L, new BigDecimal(45000), "curr1", "curr2", LocalDateTime.of(2021, Month.MARCH, 3, 19, 4, 22))));

        Mono<SummaryStatistics> result = btcUsdServiceImpl.getAvgBtcUsdByLocalDateTime(LocalDateTime.of(2021, Month.MARCH, 3, 19, 4, 22), LocalDateTime.of(2021, Month.MARCH, 3, 19, 4, 22));
        Assertions.assertNotNull(result);
    }
    @Test
    void testGetAvgBtcUsdByLocalDateTimeListNull() {
        when(btcUsdRepository.findByLocalDateTimeBetween(any(), any())).thenReturn(Collections.emptyList());

        Mono<SummaryStatistics> result = btcUsdServiceImpl.getAvgBtcUsdByLocalDateTime(LocalDateTime.of(2021, Month.MARCH, 3, 19, 4, 22), LocalDateTime.of(2021, Month.MARCH, 3, 19, 4, 22));
        Assertions.assertNotNull(result);
    }
}
