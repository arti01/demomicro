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
            // Pobieramy transakcje z bazy
            List<Transakcja> listaZazyczna = transakcjaRepository.findByKlientId(id);

            // Mapujemy "ciężkie" transakcje na "lekkie" rekordy
            List<TransakcjaInfo> transakcjeInfo = listaZazyczna.stream()
                    .map(t -> new TransakcjaInfo(t.id(), t.kwota(), t.waluta()))
                    .toList();

            // Składamy to w jeden, płaski obiekt
            return new KlientZTransakcjami(
                    klient.id(),
                    klient.nazwa(),
                    klient.email(),
                    transakcjeInfo
            );
        });
    }
}

