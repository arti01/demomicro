package arti.example.rabbit;

import arti.example.model.Transakcja;
import arti.example.service.TransakcjaRaport;
import io.micronaut.rabbitmq.annotation.Queue;
import io.micronaut.rabbitmq.annotation.RabbitListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RabbitListener // To mówi Micronautowi: "Ta klasa pilnuje Rabbita"
public class TransakcjaWykonanaListener {

    private static final Logger LOG = LoggerFactory.getLogger(TransakcjaWykonanaListener.class);

    @Queue("transakcje-wykonane")
    public void odbierzTransakcje(TransakcjaRaport raport) {
        // 1. Wyciągamy dane z raportu
        Transakcja t = raport.transakcja();

        if (raport.sukces()) {
            LOG.info("✅ SUKCES: Odebrano raport o udanej transakcji!");
            LOG.info("Kwota: {} {}", t.kwota(), t.waluta());
            LOG.info("ID Klienta: {}", t.klient().id());
            LOG.info("ID Transakcji: {}", t.id());
            LOG.info("Status: {}", raport.wiadomoscBledu()); // Tu będzie nasze "OK"
        } else {
            LOG.warn("❌ ALARM: Odebrano raport o BŁĘDZIE zapisu!");
            LOG.error("Powód błędu: {}", raport.wiadomoscBledu());
            LOG.error("Dane niedoszłej transakcji: {} {} ID Klienta: {}", t.kwota(), t.waluta(), t.klient().id());
        }
    }
}