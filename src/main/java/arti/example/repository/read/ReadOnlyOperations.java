package arti.example.repository.read;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.model.Pageable;
import io.micronaut.data.repository.GenericRepository;

import java.util.List;
import java.util.Optional;

public interface ReadOnlyOperations<T, ID> extends GenericRepository<T, ID> {
    Optional<T> findById(ID id);
    Iterable<T> findAll();
    long count();
    boolean existsById(ID id);
    List<T> findAll(Pageable pageable);
}