package arti.example.repository;

import arti.example.model.Klient;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;

@JdbcRepository(dialect = Dialect.POSTGRES)
public interface KlientRepository extends CrudRepository<Klient, Long> {
    // Micronaut sam wygeneruje implementację!
}