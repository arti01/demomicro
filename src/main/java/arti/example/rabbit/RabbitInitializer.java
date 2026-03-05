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
        channel.queueDeclare("transakcje-wykonane", true, false, false, null);
        channel.queueDeclare("transakcje-do_wykonania", true, false, false, null);

        channel.queueBind("transakcje-wykonane", "exchange-transakcje", "nowa-transakcja");
        channel.queueBind("transakcje-do_wykonania", "exchange-transakcje", "zapisz-transakcje");
    }
}