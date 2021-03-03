package ar.com.wenance.job.btc.repositories;

import ar.com.wenance.job.btc.entities.BtcUsd;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface BtcUsdRepository extends JpaRepository<BtcUsd, UUID> {
    BtcUsd findByLocalDateTime(LocalDateTime localDateTime);
    List<BtcUsd> findByLocalDateTimeBetween(LocalDateTime from, LocalDateTime to);
}
