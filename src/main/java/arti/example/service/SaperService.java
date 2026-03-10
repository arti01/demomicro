package arti.example.service;

import arti.example.model.Transakcja;
import arti.example.repository.TransakcjaRepository;
import jakarta.inject.Singleton;
import io.micronaut.transaction.annotation.Transactional;
import io.micronaut.transaction.TransactionDefinition;

@Singleton
public class SaperService {
    private final TransakcjaRepository standardRepo;

    public SaperService(TransakcjaRepository standardRepo) {
        this.standardRepo = standardRepo;
    }

    // KLUCZ: REQUIRES_NEW wymusza nowe połączenie, ignorując błąd Tarana
    @Transactional(propagation = TransactionDefinition.Propagation.REQUIRES_NEW)
    public void uratujRekord(Transakcja t) {
        standardRepo.save(t);
    }
}