package microarch.delivery.core.application.queries;

import microarch.delivery.core.domain.model.order.Order;

import java.util.List;

public interface GetAllIncompleteOrdersQueryHandler {

    List<Order> handle(GetAllIncompleteOrdersQuery query);
}
