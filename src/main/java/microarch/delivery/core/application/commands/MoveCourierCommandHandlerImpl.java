package microarch.delivery.core.application.commands;

import libs.errs.Error;
import libs.errs.GeneralErrors;
import libs.errs.Guard;
import libs.errs.UnitResult;
import microarch.delivery.core.domain.model.courier.Courier;
import microarch.delivery.core.ports.CourierRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MoveCourierCommandHandlerImpl implements MoveCourierCommandHandler {

    private final CourierRepository courierRepository;

    public MoveCourierCommandHandlerImpl(CourierRepository courierRepository) {
        this.courierRepository = courierRepository;
    }

    @Override
    @Transactional
    public UnitResult<Error> handle(MoveCourierCommand command) {
        Error validationError = validate(command);

        if (validationError != null) {
            return UnitResult.failure(validationError);
        }

        Courier courier = courierRepository.getById(command.courierId()).orElse(null);

        if (courier == null) {
            return UnitResult.failure(GeneralErrors.notFound("courier", command.courierId()));
        }

        UnitResult<Error> changeLocationResult = courier.changeLocation(command.location());

        if (changeLocationResult.isFailure()) {
            return changeLocationResult;
        }

        courierRepository.update(courier);
        return UnitResult.success();
    }

    private static Error validate(MoveCourierCommand command) {
        if (command == null) {
            return GeneralErrors.valueIsRequired("command");
        }

        return Guard.combine(Guard.againstNullOrEmpty(command.courierId(), "courierId"),
                command.location() == null ? GeneralErrors.valueIsRequired("location") : null);
    }
}
