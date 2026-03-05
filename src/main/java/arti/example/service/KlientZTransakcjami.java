package arti.example.service;

import io.micronaut.serde.annotation.Serdeable;

import java.util.List;

@Serdeable
public record KlientZTransakcjami(
        Long id,
        String nazwa,
        String email,
        List<TransakcjaInfo> transakcje
) {}
