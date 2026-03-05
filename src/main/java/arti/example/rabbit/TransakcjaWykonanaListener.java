package arti.example.rabbit;

import arti.example.model.Transakcja;
import io.micronaut.rabbitmq.annotation.Queue;
import io.micronaut.rabbitmq.annotation.RabbitListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RabbitListener // To mówi Micronautowi: "Ta klasa pilnuje Rabbita"
public class TransakcjaWykonanaListener {

    private static final Logger LOG = LoggerFactory.getLogger(TransakcjaWykonanaListener.class);

    @Queue("transakcje-wykonane") // Tu wpisz nazwę kolejki, którą stworzyliśmy w RabbitConfig
    public void odbierzTransakcje(Transakcja transakcja) {
        // To się wykona automatycznie, gdy tylko wiadomość pojawi się w kolejce
        LOG.info("📢 ODEBRANO WIADOMOŚĆ Z KOLEJKI O ZAPISANIU TRANSAKCJI!");
        LOG.info("Kwota: {} {}", transakcja.kwota(), transakcja.waluta());
        LOG.info("ID Transakcji w bazie: {}", transakcja.id());
        LOG.info("ID Klienta w bazie: {}", transakcja.klient().id());
    }
}