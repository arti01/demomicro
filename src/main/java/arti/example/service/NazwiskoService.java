package arti.example.service;

import arti.example.model.Nazwisko;
import arti.example.repository.NazwiskoRepository;
import jakarta.inject.Singleton;

import java.util.List;


@Singleton // To sprawia, że klasa jest singletonem zarządzanym przez Micronaut
public class NazwiskoService {

    private final NazwiskoRepository nazwiskoRepository;

    // Wstrzykiwanie przez konstruktor - najlepsza praktyka!
    public NazwiskoService(NazwiskoRepository nazwiskoRepository) {
        this.nazwiskoRepository = nazwiskoRepository;
    }

    public void zapisz(Nazwisko osoba, boolean czyDuzymi) {
        if (czyDuzymi) {
            nazwiskoRepository.save(osoba.withNazwisko(osoba.nazwisko().toUpperCase()));
        } else {
            nazwiskoRepository.save(osoba);
        }
    }

    public List<Nazwisko> pobierzWszystkich() {
        return nazwiskoRepository.findAll();
    }
}