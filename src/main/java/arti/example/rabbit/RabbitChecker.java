package arti.example.rabbit;


import io.micronaut.context.annotation.Context;
import io.micronaut.rabbitmq.annotation.Queue;
import io.micronaut.rabbitmq.annotation.RabbitListener;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Context
@RabbitListener // To mówi Micronautowi: "Hej, szukaj połączenia z Rabbitem!"
public class RabbitChecker {
    private static final Logger LOG = LoggerFactory.getLogger(RabbitChecker.class);

    @PostConstruct
    public void check() {
        LOG.info("🏛️ STRAŻNIK: Melduję gotowość. Szukam połączenia...");
    }

    // Ten fragment zmusi Micronauta do faktycznego połączenia z serwerem:
    @Queue("test-queue")
    public void receive(String message) {
        // Tu będziemy odbierać wiadomości
    }
}