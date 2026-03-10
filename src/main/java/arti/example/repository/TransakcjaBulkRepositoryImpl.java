package arti.example.repository;

import arti.example.model.Transakcja;
import arti.example.rabbit.TransakcjaClient;
import arti.example.scheduling.StatusReporter;
import arti.example.service.SaperService;
import arti.example.service.TransakcjaRaport;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.postgresql.PGConnection;
import org.postgresql.copy.CopyManager;
import org.postgresql.core.BaseConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private final TransakcjaRepository standardRepo; // Wstrzykujemy sapera
    private static final Logger LOG = LoggerFactory.getLogger(TransakcjaBulkRepositoryImpl.class);

    public TransakcjaBulkRepositoryImpl(DataSource dataSource, TransakcjaRepository standardRepo) {
        this.dataSource = dataSource;
        this.standardRepo = standardRepo;
    }

    @Inject
    SaperService saper; // Wstrzyknij go!

    @Override
    public void saveAllSafe(List<Transakcja> lista, TransakcjaClient client) {
        try {
            saveAllFast(lista); // 1. Taran próbuje...

            // Jeśli przeszło, meldujemy sukces dla wszystkich
            lista.forEach(t -> client.wyslijRaportOK(new TransakcjaRaport(t, "OK")));

        } catch (Exception e) {
            LOG.error("🚨 Taran zablokowany! Saper rozbraja bombę...");

            for (Transakcja t : lista) {
                try {
                    saper.uratujRekord(t); // 2. Saper ratuje...
                    // Jeśli uratował - meldujemy sukces dla tego jednego
                    client.wyslijRaportOK(new TransakcjaRaport(t, "OK"));
                } catch (Exception ex) {
                    // 3. SABOTAŻYSTA!
                    LOG.error("❌ Prawdziwy sabotażysta ID={}: {}", t.id(), ex.getMessage());
                    client.wyslijRaportBlad(new TransakcjaRaport(t, ex.getMessage()));
                }
            }
        }
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