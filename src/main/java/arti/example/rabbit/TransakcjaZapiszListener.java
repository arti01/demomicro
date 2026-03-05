package arti.example.rabbit;

import arti.example.model.Transakcja;
import arti.example.service.TransakcjaService;
import io.micronaut.rabbitmq.annotation.Queue;
import io.micronaut.rabbitmq.annotation.RabbitListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RabbitListener
public class TransakcjaZapiszListener {

    private static final Logger LOG = LoggerFactory.getLogger(TransakcjaZapiszListener.class);
    private final TransakcjaService transakcjaService;

    public TransakcjaZapiszListener(TransakcjaService transakcjaService) {
        this.transakcjaService = transakcjaService;
    }

    @Queue("transakcje-do_wykonania")
    public void wykonajZapis(Transakcja transakcja) {
        LOG.info("📥 Otrzymano zlecenie zapisu transakcji przez RabbitMQ!");

        // Wywołujemy nasz istniejący serwis
        Transakcja zapisana = transakcjaService.zapiszTransakcje(transakcja);

        LOG.info("✅ Transakcja zlecenia zapisana pomyślnie z ID: {}", zapisana.id());
    }
}