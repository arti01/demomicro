package arti.example.model;

import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.data.annotation.Relation;
import io.micronaut.serde.annotation.Serdeable;

import java.math.BigDecimal;

@Serdeable
@MappedEntity
public record Transakcja(
        @Id
        @GeneratedValue(GeneratedValue.Type.IDENTITY)
        Long id,
        BigDecimal kwota,
        String waluta,
        @Relation(Relation.Kind.MANY_TO_ONE)
        Klient klient
) {}