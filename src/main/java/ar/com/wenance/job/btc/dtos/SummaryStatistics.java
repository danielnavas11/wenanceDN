package ar.com.wenance.job.btc.dtos;

import lombok.Data;

@Data
public class SummaryStatistics {
    long count;
    long sum;
    long min;
    long max;
    double average;
    double percentToMax;
    double percentToMin;
}
