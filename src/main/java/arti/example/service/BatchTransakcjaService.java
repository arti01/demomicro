package arti.example.service;

import arti.example.model.Transakcja;
import arti.example.rabbit.TransakcjaClient;
import arti.example.repository.TransakcjaBulkRepository;
import io.micronaut.context.annotation.Value;
import io.micronaut.scheduling.annotation.Scheduled;
import io.micronaut.transaction.annotation.Transactional;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Singleton
public class BatchTransakcjaService {
    private static final Logger LOG = LoggerFactory.getLogger(BatchTransakcjaService.class);

    private final TransakcjaBulkRepository bulkRepository;
    private final List<Transakcja> buffer = Collections.synchronizedList(new ArrayList<>());
    private final TransakcjaClient transakcjaClient; // Twój klient RabbitMQ do raportów

    @Value("${rabbitmq.listener.transakcje.batch-size:100}")
    protected int batchSize;

    public BatchTransakcjaService(TransakcjaBulkRepository bulkRepository, TransakcjaClient transakcjaClient) {
        this.bulkRepository = bulkRepository;
        this.transakcjaClient = transakcjaClient;
    }

    public void addToBuffer(Transakcja transakcja) {
        buffer.add(transakcja);
        if (buffer.size() >= batchSize) {
            flush("LIMIT BUFORA");
        }
    }

    @Scheduled(fixedDelay = "10s")
    void periodicFlush() {
        flush("ZEGARMISTRZ (CZAS)");
    }

    @Transactional
    public void flush(String powod) {
        synchronized(buffer) {
            if (buffer.isEmpty()) return;
            List<Transakcja> paczka = new ArrayList<>(buffer);
            buffer.clear();

            int rozmiar = paczka.size();
            LOG.info("🚀 Akcja [{}]: Przekazuję {} transakcji do bazy...", powod, rozmiar);
            bulkRepository.saveAllSafe(paczka, transakcjaClient);
        }
    }
}