package microarch.delivery.core.domain.model.courier;

import libs.ddd.Aggregate;
import libs.errs.*;
import libs.errs.Error;
import microarch.delivery.core.domain.model.kernel.Location;
import microarch.delivery.core.domain.model.kernel.Volume;
import microarch.delivery.core.domain.model.order.Order;
import microarch.delivery.core.domain.model.order.OrderStatus;

import java.util.Arrays;
import java.util.UUID;

public class Courier extends Aggregate<UUID> {
    public static final int MAX_VOLUME = 20;

    private UUID id;
    private String name;
    private Location location;
    private Volume maxVolume;
    private Assignment[] assignments;

    protected Courier() {
    }

    private Courier(UUID id, String name, Location location) {
        super(id);
        this.name = name;
        this.maxVolume = Volume.create(MAX_VOLUME).getValue();
        this.location = location;
    }

    public static Result<Courier, Error> create(UUID id, String name, Location location) {
        return Result.success(new Courier(id, name, location));
    }

    public void changeLocation(Location newLocation) {
        this.location = newLocation;
    }

    public Result<Order, Error> takeOrder(UUID orderId) {
        // get order from repository

        if (!this.canTakeOneMoreOrder(orderId)) {
            // Тут ошибка что не можем дать больше заказов тк макс volume
            return Result.failure()
        }

        Assignment assignment = Assignment.create(orderId, order.volume, order.location).getValue();

        //Проверить правильность этого мува
        Arrays.stream(this.assignments).toList().add(assignment);

        order.assign(this.id);
    }

    public UnitResult<Error> completeOrder(UUID orderId) {

    }

    private boolean canTakeOneMoreOrder(UUID orderId) {
        // get order from repository
        int countedVolume = 0;

        // Тут надо сложить с имеющимся волюмом
        if (Arrays.stream(this.assignments).toList().forEach())
    }
}
