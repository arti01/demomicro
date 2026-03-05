package arti.example.repository;

import arti.example.model.Transakcja;
import io.micronaut.data.annotation.Join;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;

import java.util.List;

@JdbcRepository(dialect = Dialect.POSTGRES)
public interface TransakcjaRepository extends CrudRepository<Transakcja, Long> {

    @Join(value = "klient", type = Join.Type.FETCH) // To magiczna linia!
    List<Transakcja> findByKlientId(Long klientId);
}
