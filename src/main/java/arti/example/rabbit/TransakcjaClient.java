package arti.example.rabbit;

import arti.example.model.Transakcja;
import io.micronaut.rabbitmq.annotation.Binding;
import io.micronaut.rabbitmq.annotation.RabbitClient;
import reactor.core.publisher.Mono;

@RabbitClient("exchange-transakcje") // Nazwa punktu wymiany w RabbitMQ
public interface TransakcjaClient {

    @Binding("nowa-transakcja")
    Mono<Void> wyslijTransakcje(Transakcja transakcja); // Zmieniamy void na Mono<Void>
}