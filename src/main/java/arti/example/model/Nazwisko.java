package arti.example.model;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.annotation.*;
import io.micronaut.serde.annotation.Serdeable;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Serdeable
@MappedEntity("nazwiska") // Mapuje klasę na tabelę public.nazwiska
public record Nazwisko(
        @Id
        @GeneratedValue(GeneratedValue.Type.AUTO)
        @Nullable
        Long id,

        String imie,
        String nazwisko,

        @Nullable
        @DateCreated // Magia: Micronaut sam wstawi teraz aktualny czas przed INSERT-em
        OffsetDateTime data,

        @Nullable
        @DateUpdated // Magia: Micronaut sam wstawi czas przed każdym UPDATE-em
        LocalDateTime biezaca
) {
        public Nazwisko withNazwisko(String noweNazwisko) {
                return new Nazwisko(id, imie, noweNazwisko, data, biezaca);
        }
}