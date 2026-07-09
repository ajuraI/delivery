package microarch.delivery.adapters.in.kafka;

import com.google.protobuf.InvalidProtocolBufferException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import queues.basket.events.BasketEventsProto;

@Component
public class BasketConfirmedIntegrationEventConsumer {

    @KafkaListener(
            topics = "${app.kafka.basket-events-topic}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void listen(byte[] payload) throws InvalidProtocolBufferException {
        BasketEventsProto.BasketConfirmedIntegrationEvent event =
                BasketEventsProto.BasketConfirmedIntegrationEvent.parseFrom(payload);
    }
}
