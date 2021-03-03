package ar.com.wenance.job.btc.processors;

import ar.com.wenance.job.btc.events.BtcUsdCreated;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import reactor.core.publisher.FluxSink;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

@Slf4j
@Component
public class BtcUsdCreatedEventProcessor implements ApplicationListener<BtcUsdCreated>, Consumer<FluxSink<BtcUsdCreated>> {

    private final Executor executor;
    private final BlockingQueue<BtcUsdCreated> queue = new LinkedBlockingQueue<>();

    BtcUsdCreatedEventProcessor(@Qualifier("applicationTaskExecutor") Executor executor) {
        this.executor = executor;
    }

    @Override
    public void onApplicationEvent(BtcUsdCreated event) {
        this.queue.offer(event);
    }

    @Override
    public void accept(FluxSink<BtcUsdCreated> sink) {
        this.executor.execute(() -> {
            while (true) try {
                BtcUsdCreated event = queue.take();
                sink.next(event);
            } catch (InterruptedException e) {
                ReflectionUtils.rethrowRuntimeException(e);
            }
        });
    }
}
