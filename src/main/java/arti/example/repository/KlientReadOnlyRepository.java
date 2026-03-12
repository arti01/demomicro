package arti.example.repository;

import arti.example.model.Klient;
import arti.example.repository.read.ReadOnlyOperations;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;

import java.util.List;

@JdbcRepository(dataSource = "slave", dialect = Dialect.POSTGRES)
public interface KlientReadOnlyRepository extends ReadOnlyOperations <Klient, Long>{
    // Jeśli potrzebujesz tylko standardowych metod, zostawiasz PUSTE.
    // Micronaut już wie, że to 'slave' i że to Postgres!
    List<Klient> findByNazwa(String nazwa);
}