package microarch.delivery.core.application.commands;

import java.util.UUID;

public record CompleteOrderCommand(UUID courierId, UUID orderId) {
}
