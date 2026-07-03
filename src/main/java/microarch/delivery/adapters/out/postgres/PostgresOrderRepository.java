package microarch.delivery.adapters.out.postgres;

import microarch.delivery.core.domain.model.order.Order;
import microarch.delivery.core.domain.model.order.OrderStatus;
import microarch.delivery.core.ports.OrderRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Repository
public class PostgresOrderRepository implements OrderRepository {

    private final OrderJpaRepository repository;

    public PostgresOrderRepository(OrderJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public void add(Order order) {
        save(order);
    }

    @Override
    @Transactional
    public void update(Order order) {
        save(order);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Order> getById(UUID id) {
        Objects.requireNonNull(id, "id must not be null");

        return repository.findById(id).map(OrderJpaEntity::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Order> getAnyCreated() {
        return repository.findFirstByStatus(OrderStatus.CREATED).map(OrderJpaEntity::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> getAllAssigned() {
        return repository.findAllByStatus(OrderStatus.ASSIGNED).stream()
                .map(OrderJpaEntity::toDomain)
                .toList();
    }

    private void save(Order order) {
        Objects.requireNonNull(order, "order must not be null");

        repository.save(OrderJpaEntity.fromDomain(order));
    }
}
