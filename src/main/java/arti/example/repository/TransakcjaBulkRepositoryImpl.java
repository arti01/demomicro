package arti.example.repository;

import arti.example.model.Transakcja;
import io.micronaut.transaction.annotation.ReadOnly;
import jakarta.inject.Singleton;
import org.postgresql.PGConnection;
import org.postgresql.copy.CopyManager;
import org.postgresql.core.BaseConnection;

import javax.sql.DataSource;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.sql.Connection;
import java.util.List;
import java.util.stream.Collectors;

@Singleton // Mówimy Micronautowi, że to pojedynczy byt (komponent)
public class TransakcjaBulkRepositoryImpl implements TransakcjaBulkRepository {

    private final DataSource dataSource;

    // Micronaut sam dostarczy nam DataSource
    public TransakcjaBulkRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void saveAllFast(List<Transakcja> lista) {
        try (Connection conn = dataSource.getConnection()) {
            BaseConnection baseConn = conn.unwrap(BaseConnection.class);
            CopyManager copyManager = new CopyManager(baseConn);

            // Tworzymy "rurę" (Pipe)
            PipedInputStream in = new PipedInputStream();
            try (PipedOutputStream out = new PipedOutputStream(in)) {

                // Odpalamy zapis do rury w osobnym wątku,
                // żeby Postgres mógł czytać z drugiego końca w tym samym czasie
                Thread writerThread = new Thread(() -> {
                    try (PrintWriter pw = new PrintWriter(out)) {
                        for (Transakcja t : lista) {
                            pw.printf("%s|%s|%d\n",
                                    t.kwota(),
                                    t.waluta(),
                                    t.klient().id());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                writerThread.start();
                // Postgres pije z rury (InputStream)
                String sql = "COPY transakcja (kwota, waluta, klient_id) FROM STDIN WITH (FORMAT csv, DELIMITER '|')";
                copyManager.copyIn(sql, in);
                writerThread.join(); // Czekamy, aż wątek piszący skończy pracę
            }
        } catch (Exception e) {
            throw new RuntimeException("Błąd strumieniowego importu: " + e.getMessage(), e);
        }
    }
}