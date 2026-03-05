package arti.example.model;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.data.annotation.Relation;
import io.micronaut.serde.annotation.Serdeable;

import java.util.Set;

@Serdeable
@MappedEntity
public record Klient(
        @Id
        @GeneratedValue(GeneratedValue.Type.IDENTITY)
        Long id,
        String nazwa,
        String email,
        @Relation(value = Relation.Kind.ONE_TO_MANY, mappedBy = "klient")
        @Nullable
        Set<Transakcja> transakcje
) {}