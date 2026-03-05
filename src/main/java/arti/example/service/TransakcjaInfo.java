package arti.example.service;

import io.micronaut.serde.annotation.Serdeable;

import java.math.BigDecimal;

@Serdeable
public record TransakcjaInfo(
        Long id,
        BigDecimal kwota,
        String waluta
) {}