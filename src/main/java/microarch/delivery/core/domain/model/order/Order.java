package microarch.delivery.core.domain.model.order;

import libs.ddd.Aggregate;
import libs.errs.Error;
import libs.errs.GeneralErrors;
import libs.errs.Result;
import libs.errs.UnitResult;
import microarch.delivery.core.domain.model.courier.Assignment;
import microarch.delivery.core.domain.model.courier.AssignmentStatus;
import microarch.delivery.core.domain.model.kernel.Location;
import microarch.delivery.core.domain.model.kernel.Volume;

import java.util.UUID;

public class Order extends Aggregate<UUID> {
    private UUID id;
    private Volume volume;
    private Location location;
    private OrderStatus status;

    protected Order() {
    }

    private Order(UUID id, Volume volume, Location location) {
        super(id);
        this.volume = volume;
        this.location = location;
        this.status = OrderStatus.CREATED;
    }

    public static Result<Order, Error> create(UUID id, Volume volume, Location location) {
        return Result.success(new Order(id, volume, location));
    }

    public UnitResult<Error> assign(UUID courierId) {
        if (this.status != OrderStatus.CREATED) {
            return UnitResult.failure(GeneralErrors.valueIsInvalid("status", status));
        }

        this.status = OrderStatus.ASSIGNED;
        return UnitResult.success();
    }

    public UnitResult<Error> complete(UUID courierId) {
        if (this.status != OrderStatus.ASSIGNED) {
            return UnitResult.failure(GeneralErrors.valueIsInvalid("status", status));
        }

        this.status = OrderStatus.COMPLETED;
        return UnitResult.success();
    }
}
