package arti.example.rabbit;

import com.rabbitmq.client.Channel;
import io.micronaut.rabbitmq.connect.ChannelInitializer;
import jakarta.inject.Singleton;

import java.io.IOException;

@Singleton
public class RabbitInitializer extends ChannelInitializer {
    @Override
    public void initialize(Channel channel, String name) throws IOException {

        channel.exchangeDeclare("exchange-transakcje", "topic", true);
        // To polecenie fizycznie tworzy kolejkę w RabbitMQ:
        channel.queueDeclare("transakcje-wykonane_poprawnie", true, false, false, null);
        channel.queueDeclare("transakcje-wykonane_blednie", true, false, false, null);
        channel.queueDeclare("transakcje-do_wykonania", true, false, false, null);

        channel.queueBind("transakcje-wykonane_poprawnie", "exchange-transakcje", "transakcja-ok");
        channel.queueBind("transakcje-wykonane_blednie", "exchange-transakcje", "transakcja-blad");
        channel.queueBind("transakcje-do_wykonania", "exchange-transakcje", "zapisz-transakcje");
    }
}