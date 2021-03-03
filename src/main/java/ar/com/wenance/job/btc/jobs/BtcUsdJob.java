package ar.com.wenance.job.btc.jobs;

import ar.com.wenance.job.btc.services.BtcUsdService;
import lombok.extern.log4j.Log4j2;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


@Log4j2
@Component
public class BtcUsdJob implements Job {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    BtcUsdService btcUsdService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("Job {}, fired {}", jobExecutionContext.getJobDetail().getKey().getName(), jobExecutionContext.getFireTime());
        this.btcUsdService.obtainsBtcUsd();
        log.info("Next job scheduled {}", jobExecutionContext.getNextFireTime());
    }
}
