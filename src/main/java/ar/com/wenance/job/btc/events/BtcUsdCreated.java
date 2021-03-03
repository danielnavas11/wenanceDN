package ar.com.wenance.job.btc.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class BtcUsdCreated extends ApplicationEvent {
    public BtcUsdCreated(Object source) {
        super(source);
    }
}
