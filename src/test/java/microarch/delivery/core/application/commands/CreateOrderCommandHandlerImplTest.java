package microarch.delivery.core.application.commands;

import libs.errs.Error;
import libs.errs.UnitResult;
import microarch.delivery.core.domain.model.kernel.Location;
import microarch.delivery.core.domain.model.kernel.Volume;
import microarch.delivery.core.domain.model.order.Order;
import microarch.delivery.core.domain.model.order.OrderStatus;
import microarch.delivery.core.ports.OrderRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class CreateOrderCommandHandlerImplTest {

    @Test
    void createsOrderAndSavesIt() {
        OrderRepository orderRepository = mock(OrderRepository.class);
        CreateOrderCommandHandler handler = new CreateOrderCommandHandlerImpl(orderRepository);
        UUID orderId = UUID.randomUUID();

        UnitResult<Error> result = handler.handle(new CreateOrderCommand(
                orderId,
                "Russia",
                "Moscow",
                "Tverskaya",
                "1",
                "10",
                3
        ));

        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository).add(orderCaptor.capture());
        Order order = orderCaptor.getValue();

        assertThat(result.isSuccess()).isTrue();
        assertThat(order.getId()).isEqualTo(orderId);
        assertThat(order.getStatus()).isEqualTo(OrderStatus.CREATED);
        assertThat(order.getVolume()).isEqualTo(Volume.create(3).getValue());
        assertThat(order.getLocation().getX()).isBetween(Location.MIN_COORDINATE, Location.MAX_COORDINATE);
        assertThat(order.getLocation().getY()).isBetween(Location.MIN_COORDINATE, Location.MAX_COORDINATE);
    }
}
