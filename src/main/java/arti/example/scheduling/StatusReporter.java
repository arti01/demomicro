package arti.example.scheduling; // Pamiętaj o nazwie swojego pakietu!

import arti.example.service.TransakcjaService;
import io.micronaut.scheduling.annotation.Scheduled;
import io.micronaut.transaction.annotation.Transactional;
import jakarta.inject.Singleton;
import net.javacrumbs.shedlock.micronaut.SchedulerLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Singleton
public class StatusReporter {

    private final TransakcjaService transakcjaService; // Wstrzyknięcie (Injection)
    private static final Logger LOG = LoggerFactory.getLogger(StatusReporter.class);
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    public StatusReporter(TransakcjaService transakcjaService) {
        this.transakcjaService = transakcjaService;
    }

    // Odpala się co 1 minutę od startu aplikacji
    @Scheduled(fixedDelay = "${moje-zadania.raport-statusu.interwal:5m}")
    void raportujStatusStrazy() {
        String czas = LocalDateTime.now().format(FORMATTER);

        LOG.info("🕰️ HARMONOGRAM: Wybiła godzina {}. Wszystkie systemy sprawne.", czas);
        LOG.info("📊 STATUS: RabbitMQ i Postgres pod kontrolą.");
    }

    @Scheduled(fixedDelay = "${moje-zadania.raport-transakcji.interwal:10m}")
    @SchedulerLock(name = "raportujIloscTransakcji", lockAtMostFor = "10m", lockAtLeastFor = "1m")
    @Transactional
    void raportujIloscTransakcji() {
        long ilosc = transakcjaService.policzWszystkieTransakcje();
        LOG.info("📊 RAPORT BAZY: W systemie znajduje się obecnie {} transakcji.", ilosc);
    }
}