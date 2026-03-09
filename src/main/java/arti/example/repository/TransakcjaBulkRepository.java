package arti.example.repository;

import arti.example.model.Transakcja;
import java.util.List;

public interface TransakcjaBulkRepository {
    void saveAllFast(List<Transakcja> lista);
}