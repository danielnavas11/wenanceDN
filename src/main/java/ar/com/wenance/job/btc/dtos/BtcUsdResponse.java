package ar.com.wenance.job.btc.dtos;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BtcUsdResponse {
    private BigDecimal lprice;
    private String curr1;
    private String curr2;
}