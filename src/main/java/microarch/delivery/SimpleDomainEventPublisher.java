package microarch.delivery;

import libs.ddd.Aggregate;
import libs.ddd.DomainEventPublisher;
import microarch.delivery.core.ports.DomainEventProducer;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component
public class SimpleDomainEventPublisher implements DomainEventPublisher {
    private final DomainEventProducer producer;

    public SimpleDomainEventPublisher(DomainEventProducer producer) {
        this.producer = producer;
    }

    @Override
    public void publish(Iterable<? extends Aggregate<?>> aggregates) {
        for (Aggregate<?> aggregate : aggregates) {
            for (var event : aggregate.getDomainEvents()) {
                producer.produce(event);
            }
            aggregate.clearDomainEvents();
        }
    }
}
