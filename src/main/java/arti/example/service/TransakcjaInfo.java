package arti.example.service;

import io.micronaut.serde.annotation.Serdeable;

import java.math.BigDecimal;
import java.time.Instant;

@Serdeable
public record TransakcjaInfo(
        Long id,
        BigDecimal kwota,
        String waluta,
        Instant czas
) {}