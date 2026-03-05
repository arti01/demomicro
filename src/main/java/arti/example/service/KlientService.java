package arti.example.service;

import arti.example.model.Klient;
import arti.example.model.Transakcja;
import arti.example.repository.KlientRepository;
import arti.example.repository.TransakcjaRepository;
import io.micronaut.transaction.annotation.Transactional;
import jakarta.inject.Singleton;
import java.util.List;
import java.util.Optional;

@Singleton // Micronaut stworzy jedną instancję tego serwisu
public class KlientService {

    private final KlientRepository klientRepository;
    private final TransakcjaRepository transakcjaRepository;

    public KlientService(KlientRepository klientRepository, TransakcjaRepository transakcjaRepository) {
        this.klientRepository = klientRepository;
        this.transakcjaRepository = transakcjaRepository;
    }

    public Klient zapiszKlienta(Klient klient) {
        return klientRepository.save(klient);
    }

    public Iterable<Klient> pobierzWszystkich() {
        return klientRepository.findAll();
    }

    public Optional<Klient> pobierzPoId(Long id) {
        return klientRepository.findById(id);
    }

    @Transactional // Ważne: zapewnia spójność przy pobieraniu powiązanych danych
    public Optional<KlientZTransakcjami> pobierzKlientaZTransakcjami(Long id) {
        return klientRepository.findById(id).map(klient -> {
            List<Transakcja> transakcje = transakcjaRepository.findByKlientId(id);
            return new KlientZTransakcjami(klient, transakcje);
        });
    }
}

