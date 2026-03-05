package arti.example.rabbit;

import arti.example.model.Transakcja;
import arti.example.service.TransakcjaRaport;
import io.micronaut.rabbitmq.annotation.Binding;
import io.micronaut.rabbitmq.annotation.RabbitClient;
import reactor.core.publisher.Mono;

@RabbitClient("exchange-transakcje") // Nazwa punktu wymiany w RabbitMQ
public interface TransakcjaClient {

    @Binding("nowa-transakcja")//wrzuca info o wykonanej transakcji na bazie
    Mono<Void> wyslijRaport(TransakcjaRaport raport);

    @Binding("zapisz-transakcje")//wrzuca na koolejkę do zapisu
    Mono<Void> zlecZapis(Transakcja transakcja);

}