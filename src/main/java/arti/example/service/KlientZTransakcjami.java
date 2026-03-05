package arti.example.service;

import arti.example.model.Klient;
import arti.example.model.Transakcja;

import java.util.List;

// Prosty rekord pomocniczy (DTO), żeby ładnie zwrócić JSON-a
@io.micronaut.serde.annotation.Serdeable
public record KlientZTransakcjami(Klient klient, List<Transakcja> transakcje) {}
