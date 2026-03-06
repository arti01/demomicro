package arti.example.scheduling;

import io.micronaut.context.annotation.Factory;
import jakarta.inject.Singleton;
import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.provider.jdbc.JdbcLockProvider;

import javax.sql.DataSource;

@Factory
public class ShedLockConfig {

    @Singleton
    public LockProvider lockProvider(DataSource dataSource) {
        return new JdbcLockProvider(dataSource);
    }
}