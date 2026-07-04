package microarch.delivery.core.application.commands;

import microarch.delivery.core.domain.model.kernel.Location;

import java.util.UUID;

public record MoveCourierCommand(UUID courierId, Location location) {
}
