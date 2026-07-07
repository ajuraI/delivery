package microarch.delivery.core.application.commands;

import libs.errs.Error;
import libs.errs.GeneralErrors;
import libs.errs.Guard;
import libs.errs.Result;
import libs.errs.UnitResult;
import microarch.delivery.core.domain.model.kernel.Location;
import microarch.delivery.core.domain.model.kernel.Volume;
import microarch.delivery.core.domain.model.order.Order;
import microarch.delivery.core.ports.GeoClient;
import microarch.delivery.core.ports.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.ThreadLocalRandom;

@Service
public class CreateOrderCommandHandlerImpl implements CreateOrderCommandHandler {

    private final OrderRepository orderRepository;
    private final GeoClient geoClient;

    public CreateOrderCommandHandlerImpl(OrderRepository orderRepository, GeoClient geoClient) {
        this.orderRepository = orderRepository;
        this.geoClient = geoClient;
    }

    @Override
    @Transactional
    public UnitResult<Error> handle(CreateOrderCommand command) {
        Error validationError = validate(command);

        if (validationError != null) {
            return UnitResult.failure(validationError);
        }

        Result<Volume, Error> volumeResult = Volume.create(command.volume());

        if (volumeResult.isFailure()) {
            return UnitResult.failure(volumeResult.getError());
        }

        var location = geoClient.getGeolocation(command.street());

        if (location.isFailure()) {
            return UnitResult.failure(location.getError());
        }

        Result<Order, Error> orderResult = Order.create(
                command.orderId(),
                location.getValue(),
                volumeResult.getValue()
        );

        if (orderResult.isFailure()) {
            return UnitResult.failure(orderResult.getError());
        }

        orderRepository.add(orderResult.getValue());
        return UnitResult.success();
    }

    private static Error validate(CreateOrderCommand command) {
        if (command == null) {
            return GeneralErrors.valueIsRequired("command");
        }

        return Guard.combine(
                Guard.againstNullOrEmpty(command.orderId(), "orderId"),
                Guard.againstNullOrEmpty(command.country(), "country"),
                Guard.againstNullOrEmpty(command.city(), "city"),
                Guard.againstNullOrEmpty(command.street(), "street"),
                Guard.againstNullOrEmpty(command.house(), "house"),
                Guard.againstNullOrEmpty(command.apartment(), "apartment")
        );
    }

    private static Location randomLocation() {
        return new Location(randomCoordinate(), randomCoordinate());
    }

    private static int randomCoordinate() {
        return ThreadLocalRandom.current().nextInt(
                Location.MIN_COORDINATE,
                Location.MAX_COORDINATE + 1
        );
    }
}
