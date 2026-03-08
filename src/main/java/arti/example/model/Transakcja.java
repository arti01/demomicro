package arti.example.model;

import io.micronaut.data.annotation.*;
import io.micronaut.serde.annotation.Serdeable;
import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import jakarta.persistence.Column;

import java.math.BigDecimal;
import java.time.Instant;

// ZOBACZ TEŻ: arti.example.service.TransakcjaInfo (Pamiętaj o synchronizacji pól!)
@Serdeable
@MappedEntity
public record Transakcja(
        @Id
        @GeneratedValue(value = GeneratedValue.Type.IDENTITY)
        Long id,
        BigDecimal kwota,
        String waluta,
        @DateCreated
        Instant czas,
        @Relation(Relation.Kind.MANY_TO_ONE)
        Klient klient
) {}