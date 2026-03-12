package arti.example.rabbit;

import arti.example.model.Transakcja;
import arti.example.service.BatchTransakcjaService;
import io.micronaut.context.annotation.Requires;
import io.micronaut.rabbitmq.annotation.RabbitListener;
import io.micronaut.rabbitmq.annotation.Queue;
import jakarta.annotation.PreDestroy;
import jakarta.inject.Singleton;


@Requires(notEnv = "ttt")
@Singleton
@RabbitListener()
public class BatchTransakcjaListener {

    private final BatchTransakcjaService batchService;

    public BatchTransakcjaListener(BatchTransakcjaService batchService) {
        this.batchService = batchService;
    }

    @Queue(value = "transakcje-do_wykonania", prefetch = 100)
    public void onMessage(Transakcja transakcja) {
        batchService.addToBuffer(transakcja);
    }

    @PreDestroy
    void onShutdown() {
        batchService.flush("ZAMKNIĘCIE SYSTEMU");
    }
}