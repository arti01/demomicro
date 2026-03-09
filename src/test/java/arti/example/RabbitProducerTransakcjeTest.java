package arti.example;

import arti.example.model.Klient;
import arti.example.model.Transakcja;
import arti.example.rabbit.TransakcjaClient;
import arti.example.repository.KlientRepository;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

@MicronautTest(environments = "dev")
public class RabbitProducerTransakcjeTest {

    @Inject
    TransakcjaClient transakcjaClient;

    @Inject
    KlientRepository klientRepository;

    @Test
    void wyslijPaczkeDoRabbita() {
        int liczbaWiadomosci = 30000;
        System.out.println("🚀 Przygotowanie do wysyłki " + liczbaWiadomosci + " wiadomości...");

        @NonNull Optional<Klient> klient=klientRepository.findById(Long.valueOf(2));

        for (int i = 0; i < liczbaWiadomosci; i++) {
            Transakcja t = new Transakcja(
                    null,
                    new BigDecimal("97.99"),
                    "PLN",
                    Instant.now(),
                    klient.orElse(null)
            );

            // Wysyłamy na kolejkę "transakcje-do_wykonania"
            transakcjaClient.zlecZapis(t);
        }

        System.out.println("✅ Wszystkie wiadomości zostały przekazane do brokera RabbitMQ.");
    }
}