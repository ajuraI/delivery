package microarch.delivery.core.application.queries;

import microarch.delivery.core.application.queries.dto.IncompleteOrderDto;
import microarch.delivery.core.application.queries.dto.LocationDto;
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
    public List<IncompleteOrderDto> handle(GetAllIncompleteOrdersQuery query) {
        Objects.requireNonNull(query, "query must not be null");

        return orderRepository.getAllIncomplete().stream()
                .map(order -> new IncompleteOrderDto(
                        order.getId(),
                        new LocationDto(order.getLocation().getX(), order.getLocation().getY())
                ))
                .toList();
    }
}
