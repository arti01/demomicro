package arti.example.repository;

import arti.example.model.Transakcja;
import arti.example.rabbit.TransakcjaClient;

import java.util.List;

public interface TransakcjaBulkRepository {
    // Szybki Taran (tylko COPY)
    void saveAllFast(List<Transakcja> lista);

    // Bezpieczna Hybryda (Taran + Saper)
    void saveAllSafe(List<Transakcja> lista, TransakcjaClient client);
}