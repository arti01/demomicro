package arti.example.repository;

import arti.example.model.Transakcja;
import arti.example.repository.read.ReadOnlyOperations;
import io.micronaut.data.annotation.Join;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;

import java.util.List;

@JdbcRepository(dataSource = "slave", dialect = Dialect.POSTGRES)
public interface TransakcjaReadOnlyRepository extends ReadOnlyOperations<Transakcja, Long> {

    @Join(value = "klient", type = Join.Type.FETCH) // To magiczna linia!
    List<Transakcja> findByKlientId(Long klientId);
}
