package arti.example;

import arti.example.model.Transakcja;
import arti.example.rabbit.TransakcjaClient;
import arti.example.repository.TransakcjaBulkRepository;
import io.micronaut.rabbitmq.annotation.Queue;
import io.micronaut.rabbitmq.annotation.RabbitListener;
import io.micronaut.scheduling.annotation.Scheduled; // Nowość!
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.transaction.annotation.Transactional;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@MicronautTest(environments = "dev")
@RabbitListener
public class BatchListenerTest {

    private static final Logger LOG = LoggerFactory.getLogger(BatchListenerTest.class);

    @Inject
    TransakcjaBulkRepository bulkRepository;

    @Inject
    TransakcjaClient transakcjaClient; // Micronaut sam go tu podstawi

    private final List<Transakcja> buffer = Collections.synchronizedList(new ArrayList<>());
    private final int BATCH_SIZE = 1000;

    // Zmniejszmy latch do 1, bo teraz Zegarmistrz "domknie" test za nas
    private static final CountDownLatch latch = new CountDownLatch(1);

    @Queue(value = "transakcje-do_wykonania", prefetch = 500)
    public void wykonajZapisTestowy(Transakcja transakcja) {
        buffer.add(transakcja);

        if (buffer.size() >= BATCH_SIZE) {
            flush("LIMIT BUFORA");
        }
    }

    /**
     * ZEGARMISTRZ: Co 5 sekund zagląda do biura.
     * Jeśli znajdzie choć jedną kartkę, wysyła ją do archiwum.
     */
    @Scheduled(fixedDelay = "50s")
    void periodicFlush() {
        if (!buffer.isEmpty()) {
            flush("ZEGARMISTRZ (CZAS)");
            latch.countDown(); // Meldujemy wykonanie zadania, nawet przy niepełnej paczce
        }
    }

    void flush(String powod) {
        // 2. Synchronizujemy się na buforze (konsekwentnie z resztą kodu)
        synchronized(buffer) {
            if (buffer.isEmpty()) return;

            List<Transakcja> paczka = new ArrayList<>(buffer);
            buffer.clear();

            LOG.info("🚀 Akcja [{}]: Przekazuję {} transakcji do bazy...", powod, paczka.size());

            try {
                // 3. Przekazujemy paczkę ORAZ klienta (gołębia pocztowego)
                bulkRepository.saveAllSafe(paczka, transakcjaClient);
            } catch (Exception e) {
                // To jest "siatka bezpieczeństwa ostatniej szansy"
                LOG.error("💥 Nieoczekiwany błąd w koordynatorze flush: {}", e.getMessage());
            }
        }
    }

    @Test
    void odpalOdkurzacz() throws InterruptedException {
        LOG.info("🏁 Odkurzacz startuje... Nie zamykaj bramy przez 30 sekund!");

        // Dajemy czas Listenerowi, żeby wciągnął te 18 tysięcy transakcji
        Thread.sleep(30000);

        // Na wszelki wypadek wypychamy to, co mogło zostać w buforze
        periodicFlush();

        LOG.info("✅ Czas minął. Teraz kolejka powinna być pusta!");
    }
}