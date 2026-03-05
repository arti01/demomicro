package arti.example.service;

import arti.example.model.Transakcja;
import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record TransakcjaRaport(
        Transakcja transakcja,
        boolean sukces,
        String wiadomoscBledu
) {}