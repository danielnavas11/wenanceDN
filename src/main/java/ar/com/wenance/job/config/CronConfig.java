package ar.com.wenance.job.config;


import ar.com.wenance.job.btc.jobs.BtcUsdJob;
import lombok.extern.log4j.Log4j2;
import org.quartz.SimpleTrigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

import java.util.Objects;

@Log4j2
@Configuration
@ComponentScan("ar.com.wenance.job")
public class CronConfig {

    @Value("${quartz.expression}")
    String expression;

    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    public SpringBeanJobFactory springBeanJobFactory() {
        AutoWiringSpringBeanJobFactory jobFactory = new AutoWiringSpringBeanJobFactory();
        log.debug("Configuring Job factory");
        jobFactory.setApplicationContext(applicationContext);
        return jobFactory;
    }
    @Bean
    public SchedulerFactoryBean quartzScheduler() {
        SchedulerFactoryBean quartzScheduler = new SchedulerFactoryBean();
        quartzScheduler.setTriggers(btcUsdTrigger().getObject());
        quartzScheduler.setConfigLocation(new ClassPathResource("quartz.properties"));
        quartzScheduler.setJobFactory(springBeanJobFactory());
        return quartzScheduler;
    }
    @Bean
    public CronTriggerFactoryBean btcUsdTrigger() {
        CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
        cronTriggerFactoryBean.setJobDetail(Objects.requireNonNull(jobDetailFactoryBean().getObject()));
        cronTriggerFactoryBean.setPriority(1);
        cronTriggerFactoryBean.setMisfireInstruction(SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);
        cronTriggerFactoryBean.setCronExpression(expression);
        cronTriggerFactoryBean.setGroup("w");
        cronTriggerFactoryBean.setName("W_CronTriggerFactoryBean");
        return cronTriggerFactoryBean;
    }
    @Bean
    public JobDetailFactoryBean jobDetailFactoryBean() {
        JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
        jobDetailFactory.setJobClass(BtcUsdJob.class);
        jobDetailFactory.setGroup("w");
        jobDetailFactory.setName("W_JobDetailFactoryBean");
        return jobDetailFactory;
    }
}
