package arti.example;

import arti.example.model.Klient;
import arti.example.model.Transakcja;
import arti.example.repository.KlientRepository;
import arti.example.repository.TransakcjaBulkRepository;
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
    TransakcjaBulkRepository transakcjaBulkRepository;

    @Inject
    KlientRepository klientRepository;

    @Test
    public void wielkaKonfrontacjaMetod() {
        // 1. Przygotowanie 500k rekordów w pamięci (wspólne dla obu testów)
        Klient testowyKlient = klientRepository.save(new Klient(null, "test", "test", null));
        int liczba = 100;
        List<Transakcja> lista = new ArrayList<>();
        for (int i = 0; i < liczba; i++) {
            lista.add(new Transakcja(null, new BigDecimal("100.00"), "PLN", null, testowyKlient));
        }

        System.out.println("🏁 Rozpoczynamy wielkie porównanie dla " + liczba + " rekordów...\n");

        // --- PRZEBIEG 1: STARA METODA (JDBC BATCH / MICRONAUT DATA) ---
        System.out.println("🐢 [1/2] Start zapisu metodą saveAll()...");
        long startOld = System.currentTimeMillis();
        transakcjaRepository.saveAll(lista);
        long endOld = System.currentTimeMillis();
        long czasOld = endOld - startOld;
        System.out.println("❌ Zakończono w: " + czasOld + "ms");

        // --- OPCJONALNIE: TRUNCATE/DELETE (jeśli nie masz @Transactional na całym teście) ---
        // transakcjaRepository.deleteAll();

        // --- PRZEBIEG 2: NOWA METODA (POSTGRES COPY + STREAM) ---
        System.out.println("\n🚀 [2/2] Start zapisu metodą saveAllFast()...");
        long startNew = System.currentTimeMillis();
        transakcjaBulkRepository.saveAllFast(lista);
        long endNew = System.currentTimeMillis();
        long czasNew = endNew - startNew;
        System.out.println("✅ Zakończono w: " + czasNew + "ms");

        // --- PODSUMOWANIE ---
        System.out.println("\n========================================");
        System.out.println("📊 RAPORT OPTYMALIZACJI");
        System.out.println("========================================");
        System.out.printf("Standard (saveAll):     %d ms\n", czasOld);
        System.out.printf("Turbo (saveAllFast):    %d ms\n", czasNew);
        System.out.println("----------------------------------------");

        double mnoznik = (double) czasOld / czasNew;
        System.out.printf("🔥 Metoda COPY jest o %.2f razy szybsza!\n", mnoznik);
        System.out.printf("⏱️ Zaoszczędzony czas: %d ms\n", (czasOld - czasNew));
        System.out.println("========================================");
    }
}