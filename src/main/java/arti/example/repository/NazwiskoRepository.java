package arti.example.repository;

import arti.example.model.Nazwisko;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;

@JdbcRepository(dialect = Dialect.POSTGRES) // Adnotacja mówi: "To jest komponent bazy"
public interface NazwiskoRepository extends CrudRepository<Nazwisko, Long> {
}
