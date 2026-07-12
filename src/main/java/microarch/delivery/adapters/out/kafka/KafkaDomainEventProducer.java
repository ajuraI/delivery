package microarch.delivery.adapters.out.kafka;

import libs.ddd.DomainEvent;
import microarch.delivery.core.domain.model.order.events.OrderAssignedDomainEvent;
import microarch.delivery.core.domain.model.order.events.OrderCompletedDomainEvent;
import microarch.delivery.core.ports.DomainEventProducer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import queues.order.events.OrderEventsProto;

import java.util.concurrent.ExecutionException;

@Component
public class KafkaDomainEventProducer implements DomainEventProducer {
    private final KafkaTemplate<String, byte[]> kafkaTemplate;
    private final String orderEventsTopic;

    public KafkaDomainEventProducer(
            KafkaTemplate<String, byte[]> kafkaTemplate,
            @Value("${app.kafka.order-events-topic}") String orderEventsTopic
    ) {
        this.kafkaTemplate = kafkaTemplate;
        this.orderEventsTopic = orderEventsTopic;
    }

    @Override
    public void produce(DomainEvent event) {
        try {
            switch (event) {
                case OrderAssignedDomainEvent orderAssigned -> {
                    var integrationEvent = OrderEventsProto.OrderAssignedIntegrationEvent.newBuilder()
                            .setOrderId(orderAssigned.getOrderId().toString())
                            .build();

                    kafkaTemplate.send(
                            orderEventsTopic,
                            orderAssigned.getOrderId().toString(),
                            integrationEvent.toByteArray()
                    ).get();
                }
                case OrderCompletedDomainEvent orderCompleted -> {
                    var integrationEvent = OrderEventsProto.OrderCompletedIntegrationEvent.newBuilder()
                            .setOrderId(orderCompleted.getOrderId().toString())
                            .build();

                    kafkaTemplate.send(
                            orderEventsTopic,
                            orderCompleted.getOrderId().toString(),
                            integrationEvent.toByteArray()
                    ).get();
                }
                default -> throw new IllegalArgumentException(
                        "Unknown domain event type: " + event.getClass().getName()
                );
            }
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Kafka publish interrupted", exception);
        } catch (ExecutionException exception) {
            throw new IllegalStateException("Kafka publish failed", exception.getCause());
        }
    }
}
