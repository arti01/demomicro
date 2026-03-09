package arti.example;

import arti.example.model.Klient;
import arti.example.model.Transakcja;
import arti.example.rabbit.TransakcjaClient;
import arti.example.repository.KlientRepository;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

@MicronautTest(environments = "dev")
public class RabbitProducerTransakcjeTest {

    @Inject
    TransakcjaClient transakcjaClient;

    @Inject
    KlientRepository klientRepository;

    @Test
    void wyslijPaczkeDoRabbita() {
        int liczbaWiadomosci = 10000;
        System.out.println("🚀 Przygotowanie do wysyłki " + liczbaWiadomosci + " wiadomości...");

        for (int i = 0; i < liczbaWiadomosci; i++) {
            Transakcja t = new Transakcja(
                    null,
                    new BigDecimal("97.99"),
                    "PLN",
                    null,
                    null
            );

            // Wysyłamy na kolejkę "transakcje-do_wykonania"
            transakcjaClient.zlecZapis(t);
        }

        System.out.println("✅ Wszystkie wiadomości zostały przekazane do brokera RabbitMQ.");
    }
}