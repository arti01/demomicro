package arti.example.health;

import io.micronaut.data.jdbc.runtime.JdbcOperations;
import io.micronaut.health.HealthStatus; // <--- TEN JEST KLUCZOWY
import io.micronaut.management.health.indicator.HealthIndicator;
import io.micronaut.management.health.indicator.HealthResult;
import io.micronaut.management.health.indicator.annotation.Readiness;
import jakarta.inject.Singleton;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Collections;

@Singleton
@Readiness
public class DatabaseWriteHealthIndicator implements HealthIndicator {

    private final JdbcOperations jdbcOperations; // Zmieniamy DataSource na JdbcOperations

    public DatabaseWriteHealthIndicator(JdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }

    @Override
    public Publisher<HealthResult> getResult() {
        return Mono.fromCallable(() -> {
            try {
                // executeSync sam otwiera połączenie i je zamyka
                Boolean isReadOnly = jdbcOperations.execute(connection -> {
                    var statement = connection.createStatement();
                    var resultSet = statement.executeQuery("SELECT pg_is_in_recovery()");
                    return resultSet.next() && resultSet.getBoolean(1);
                });

                if (Boolean.TRUE.equals(isReadOnly)) {
                    return HealthResult.builder("database-write")
                            .status(HealthStatus.DOWN)
                            .details(Collections.singletonMap("error", "Baza w trybie READ-ONLY (Slave)"))
                            .build();
                }
                return HealthResult.builder("database-write").status(HealthStatus.UP).build();
            } catch (Exception e) {
                return HealthResult.builder("database-write").status(HealthStatus.DOWN).exception(e).build();
            }
        }).subscribeOn(Schedulers.boundedElastic());
    }
}