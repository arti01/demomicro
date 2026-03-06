package arti.example.rabbit;

import arti.example.model.Transakcja;
import arti.example.service.TransakcjaRaport;
import io.micronaut.rabbitmq.annotation.Binding;
import io.micronaut.rabbitmq.annotation.RabbitClient;


@RabbitClient("exchange-transakcje") // Nazwa punktu wymiany w RabbitMQ
public interface TransakcjaClient {

    @Binding("nowa-transakcja")//wrzuca info o wykonanej transakcji na bazie
    void wyslijRaport(TransakcjaRaport raport);

    @Binding("zapisz-transakcje")//wrzuca na koolejkę do zapisu
    void zlecZapis(Transakcja transakcja);

}