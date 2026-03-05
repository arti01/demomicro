package arti.example.service;

import arti.example.model.Transakcja;
import arti.example.rabbit.TransakcjaClient;
import arti.example.repository.TransakcjaRepository;
import io.micronaut.transaction.annotation.Transactional;
import jakarta.inject.Singleton;

@Singleton
public class TransakcjaService {

    private final TransakcjaRepository transakcjaRepository;
    private final TransakcjaClient transakcjaClient;

    public TransakcjaService(TransakcjaRepository transakcjaRepository, TransakcjaClient transakcjaClient) {
        this.transakcjaRepository = transakcjaRepository;
        this.transakcjaClient = transakcjaClient;
    }

    @Transactional//zapisuje po prostu do bazy
    public Transakcja zapiszTransakcje(Transakcja transakcja) {
        // 1. Zapis do bazy
        Transakcja zapisana = transakcjaRepository.save(transakcja);

        // 2. WYSYŁKA NA RABBITA
        transakcjaClient.wyslijTransakcje(zapisana)
                .doOnError(e -> System.err.println("Błąd wysyłki na RabbitMQ: " + e.getMessage()))
                .subscribe();
        return zapisana;
    }

    public void zlecZapisPrzezRabbit(Transakcja transakcja) {
        transakcjaClient.zlecZapis(transakcja).subscribe();
    }

    public Iterable<Transakcja> pobierzWszystkie() {
        return transakcjaRepository.findAll();
    }

}