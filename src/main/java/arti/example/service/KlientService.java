package arti.example.service;

import arti.example.model.Klient;
import arti.example.model.Transakcja;
import arti.example.repository.KlientReadOnlyRepository;
import arti.example.repository.KlientRepository;
import arti.example.repository.TransakcjaReadOnlyRepository;
import arti.example.repository.TransakcjaRepository;
import io.micronaut.data.annotation.Repository;
import io.micronaut.transaction.annotation.Transactional;
import jakarta.inject.Singleton;

import java.util.List;
import java.util.Optional;

@Singleton // Micronaut stworzy jedną instancję tego serwisu
public class KlientService {

    private final KlientRepository klientRepository;
    private final KlientReadOnlyRepository klientRepositoryRO;
    private final TransakcjaRepository transakcjaRepository;
    private final TransakcjaReadOnlyRepository transakcjaRepositoryRO;

    public KlientService(KlientRepository klientRepository, KlientReadOnlyRepository klientRepositoryRO, TransakcjaRepository transakcjaRepository, TransakcjaReadOnlyRepository transakcjaRepositoryRO) {
        this.klientRepository = klientRepository;
        this.klientRepositoryRO = klientRepositoryRO;
        this.transakcjaRepository = transakcjaRepository;
        this.transakcjaRepositoryRO = transakcjaRepositoryRO;
    }

    public Klient zapiszKlienta(Klient klient) {
        return klientRepository.save(klient);
    }

    @Transactional(readOnly = true)
    public Iterable<Klient> pobierzWszystkich() {
        return klientRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Klient> pobierzPoId(Long id) {
        return klientRepository.findById(id);
    }

    @Transactional(readOnly = true) // Ważne: zapewnia spójność przy pobieraniu powiązanych danych
    public Optional<KlientZTransakcjami> pobierzKlientaZTransakcjami(Long id) {
        return klientRepositoryRO.findById(id).map(klient -> {
            // Pobieramy transakcje z bazy
            List<Transakcja> listaZazyczna = transakcjaRepositoryRO.findByKlientId(id);

            // Mapujemy "ciężkie" transakcje na "lekkie" rekordy
            List<TransakcjaInfo> transakcjeInfo = listaZazyczna.stream()
                    .map(t -> new TransakcjaInfo(t.id(), t.kwota(), t.waluta(), t.czas()))
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