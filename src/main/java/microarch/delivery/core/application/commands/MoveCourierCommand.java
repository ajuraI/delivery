package microarch.delivery.core.application.commands;

import java.util.UUID;

public record MoveCourierCommand(UUID courierId, int locationX, int locationY) {
}
