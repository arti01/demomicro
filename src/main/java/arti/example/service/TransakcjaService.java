package arti.example.service;

import arti.example.model.Transakcja;
import arti.example.rabbit.TransakcjaClient;
import arti.example.repository.TransakcjaBulkRepository;
import arti.example.repository.TransakcjaRepository;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.micronaut.transaction.annotation.Transactional;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


@Singleton
public class TransakcjaService {

    private static final Logger LOG = LoggerFactory.getLogger(TransakcjaService.class);
    private final TransakcjaRepository transakcjaRepository;
    private final TransakcjaClient transakcjaClient;
    private TransakcjaBulkRepository bulkRepo;

    public TransakcjaService(TransakcjaRepository transakcjaRepository, TransakcjaClient transakcjaClient, TransakcjaBulkRepository bulkRepo) {
        this.transakcjaRepository = transakcjaRepository;
        this.transakcjaClient = transakcjaClient;
        this.bulkRepo = bulkRepo;
    }

    @Transactional
    public Transakcja zapiszTransakcje(Transakcja transakcja) {
        try {
            Transakcja zapisana = transakcjaRepository.save(transakcja);

            // Sukces - wysyłamy raport
            transakcjaClient.wyslijRaportOK(new TransakcjaRaport(zapisana, "OK"));
            return zapisana;

        } catch (Exception e) {
            // Błąd - wysyłamy raport o porażce
            LOG.error("🚨 Przechwycono błąd bazy: {}", e.getMessage());
            transakcjaClient.wyslijRaportBlad(new TransakcjaRaport(transakcja, e.getMessage()));

            // ZAMIAST throw e; -> Zwróć null lub rzuć własny, kontrolowany wyjątek,
            // ale pamiętaj, że Listener i tak to zaloguje jako błąd, jeśli go tam nie złapiesz.
            return null;
        }
    }

    public void zlecZapisPrzezRabbit(Transakcja transakcja) {
        transakcjaClient.zlecZapis(transakcja);
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