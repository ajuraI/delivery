package microarch.delivery.core.application.queries;

import microarch.delivery.core.domain.model.order.Order;
import microarch.delivery.core.ports.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class GetAllIncompleteOrdersQueryHandlerImpl implements GetAllIncompleteOrdersQueryHandler {

    private final OrderRepository orderRepository;

    public GetAllIncompleteOrdersQueryHandlerImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public List<Order> handle(GetAllIncompleteOrdersQuery query) {
        Objects.requireNonNull(query, "query must not be null");

        return orderRepository.getAllIncomplete();
    }
}
