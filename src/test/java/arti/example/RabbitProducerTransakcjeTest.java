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
        int liczbaWiadomosci = 20270;
        System.out.println("🚀 Przygotowanie do wysyłki " + liczbaWiadomosci + " wiadomości z sabotażystami...");

        // Prawidłowy klient
        @NonNull Optional<Klient> klientOk = klientRepository.findById(2L);

        // Tworzymy obiekt-ducha (klient o ID 99, który nie istnieje w bazie)
        // To wywoła błąd naruszenia klucza obcego (Foreign Key Violation)
        Klient klientFalszywy = new Klient(99L, "Nieistniejący", "Sabotażysta", null);
        int iloscSabot = 0;

        for (int i = 1; i <= liczbaWiadomosci; i++) {
            Klient wybranyKlient;

            // Co 3000 rekord wrzucamy sabotażystę
            if (i % 3000 == 0) {
                wybranyKlient = klientFalszywy;
                System.out.println("🧨 Dodano sabotażystę do paczki (rekord nr: " + i + ")");
                iloscSabot++;
            } else {
                wybranyKlient = klientOk.orElse(null);
            }

            Transakcja t = new Transakcja(
                    null,
                    new BigDecimal("97.99"),
                    "PLN",
                    Instant.now(),
                    wybranyKlient
            );
            transakcjaClient.zlecZapis(t);
        }
        System.out.println("✅ Wszystkie wiadomości (w tym " + iloscSabot + " sabotażystów) są w RabbitMQ.");
    }
}
