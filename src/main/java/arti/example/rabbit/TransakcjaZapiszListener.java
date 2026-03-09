package arti.example.rabbit;

import arti.example.model.Transakcja;
import arti.example.service.TransakcjaService;
import io.micronaut.rabbitmq.annotation.Queue;
import io.micronaut.rabbitmq.annotation.RabbitListener;
import io.micronaut.rabbitmq.annotation.Binding;
import io.micronaut.messaging.MessageHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RabbitListener()
public class TransakcjaZapiszListener {

    private static final Logger LOG = LoggerFactory.getLogger(TransakcjaZapiszListener.class);
    private final TransakcjaService transakcjaService;

    public TransakcjaZapiszListener(TransakcjaService transakcjaService) {
        this.transakcjaService = transakcjaService;
    }

    @Queue(value = "transakcje-do_wykonania", prefetch = 100)
    public void wykonajZapis(Transakcja transakcja) {
        LOG.info("📥 Otrzymano zlecenie zapisu transakcji przez RabbitMQ!");

        // Wywołujemy nasz istniejący serwis
        Transakcja zapisana = transakcjaService.zapiszTransakcje(transakcja);

        if (zapisana == null) {
            LOG.warn("⚠️ Nie udało się zapisać transakcji (szczegóły w logach serwisu)");
            return; // Kończymy metodę bezpiecznie, bez rzucania błędu wyżej
        }

        LOG.info("✅ Transakcja zlecenia zapisana pomyślnie z ID: {}", zapisana.id());
    }
}