package arti.example.service;

import arti.example.model.Transakcja;
import arti.example.rabbit.TransakcjaClient;
import arti.example.repository.TransakcjaRepository;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import io.micronaut.transaction.annotation.Transactional;

@Singleton
public class TransakcjaService {

    private final TransakcjaRepository transakcjaRepository;
    private final TransakcjaClient transakcjaClient;

    public TransakcjaService(TransakcjaRepository transakcjaRepository, TransakcjaClient transakcjaClient) {
        this.transakcjaRepository = transakcjaRepository;
        this.transakcjaClient = transakcjaClient;
    }

    @Transactional
    public Transakcja zapiszTransakcje(Transakcja transakcja) {
        // 1. Zapis do bazy
        Transakcja zapisana = transakcjaRepository.save(transakcja);

        // 2. WYSYŁKA NA RABBITA
        transakcjaClient.wyslijTransakcje(zapisana)
                .doOnError(e -> System.err.println("Błąd wysyłki na RabbitMQ: " + e.getMessage()))
                .subscribe();
        return zapisana;
    }

    public Iterable<Transakcja> pobierzWszystkie() {
        return transakcjaRepository.findAll();
    }
}