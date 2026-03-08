package arti.example;

import arti.example.model.Klient;
import arti.example.model.Transakcja;
import arti.example.repository.KlientRepository;
import arti.example.repository.TransakcjaRepository;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@MicronautTest(environments = "dev")
public class WydajnoscTest {

    @Inject
    TransakcjaRepository transakcjaRepository;

    @Inject
    KlientRepository klientRepository;

    @Test
    public void testujBatch() {
        // 1. Musimy mieć klienta w bazie, żeby relacja działała
        Klient testowyKlient = klientRepository.save(new Klient(null,"test","test", null));

        List<Transakcja> lista = new ArrayList<>();
        for (int i = 0; i < 500000; i++) {
            lista.add(new Transakcja(
                    //(long) i + 500, // Nadajemy ID ręcznie (np. od 500 w górę)
                    null,
                    new BigDecimal("100.00"),
                    "PLN",
                    null,
                    testowyKlient
            ));
        }

        System.out.println("🚀 Start zapisu 500k rekordów...");
        long start = System.currentTimeMillis();

        // KLUCZ: saveAll() w Micronaut wyzwala mechanizm Batchingowy sterownika
        transakcjaRepository.saveAll(lista);

        long end = System.currentTimeMillis();
        System.out.println("✅ Zakończono w: " + (end - start) + "ms");
        long count = transakcjaRepository.count();
        System.out.println("📊 Liczba rekordów w bazie: " + count);
    }
}