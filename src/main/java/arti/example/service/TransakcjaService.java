package arti.example.service;

import arti.example.model.Transakcja;
import arti.example.rabbit.TransakcjaClient;
import arti.example.repository.TransakcjaRepository;
import io.micronaut.transaction.annotation.Transactional;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Singleton
public class TransakcjaService {

    private static final Logger LOG = LoggerFactory.getLogger(TransakcjaService.class);
    private final TransakcjaRepository transakcjaRepository;
    private final TransakcjaClient transakcjaClient;

    public TransakcjaService(TransakcjaRepository transakcjaRepository, TransakcjaClient transakcjaClient) {
        this.transakcjaRepository = transakcjaRepository;
        this.transakcjaClient = transakcjaClient;
    }

    @Transactional
    public Transakcja zapiszTransakcje(Transakcja transakcja) {
        try {
            Transakcja zapisana = transakcjaRepository.save(transakcja);

            // Sukces - wysyłamy raport
            transakcjaClient.wyslijRaport(new TransakcjaRaport(zapisana, true, "OK")).subscribe();
            return zapisana;

        } catch (Exception e) {
            // Błąd - wysyłamy raport o porażce
            LOG.error("🚨 Przechwycono błąd bazy: {}", e.getMessage());
            transakcjaClient.wyslijRaport(new TransakcjaRaport(transakcja, false, e.getMessage())).subscribe();

            // ZAMIAST throw e; -> Zwróć null lub rzuć własny, kontrolowany wyjątek,
            // ale pamiętaj, że Listener i tak to zaloguje jako błąd, jeśli go tam nie złapiesz.
            return null;
        }
    }

    public void zlecZapisPrzezRabbit(Transakcja transakcja) {
        transakcjaClient.zlecZapis(transakcja).subscribe();
    }

    @Transactional(readOnly = true)
    public Iterable<Transakcja> pobierzWszystkie() {
        return transakcjaRepository.findAll();
    }

    @Transactional(readOnly = true) // readOnly przyspiesza operacje tylko do odczytu i może kierowac na wezel slave
    public long policzWszystkieTransakcje() {
        return transakcjaRepository.count();
    }
}