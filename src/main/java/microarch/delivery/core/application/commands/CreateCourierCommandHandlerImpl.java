package microarch.delivery.core.application.commands;

import libs.errs.Error;
import libs.errs.GeneralErrors;
import libs.errs.Guard;
import libs.errs.Result;
import libs.errs.UnitResult;
import microarch.delivery.core.domain.model.courier.Courier;
import microarch.delivery.core.domain.model.kernel.Location;
import microarch.delivery.core.ports.CourierRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateCourierCommandHandlerImpl implements CreateCourierCommandHandler {

    private static final Location INITIAL_LOCATION = new Location(
            Location.MIN_COORDINATE,
            Location.MIN_COORDINATE
    );

    private final CourierRepository courierRepository;

    public CreateCourierCommandHandlerImpl(CourierRepository courierRepository) {
        this.courierRepository = courierRepository;
    }

    @Override
    @Transactional
    public UnitResult<Error> handle(CreateCourierCommand command) {
        Error validationError = validate(command);

        if (validationError != null) {
            return UnitResult.failure(validationError);
        }

        Result<Courier, Error> courierResult = Courier.create(command.name(), INITIAL_LOCATION);

        if (courierResult.isFailure()) {
            return UnitResult.failure(courierResult.getError());
        }

        courierRepository.add(courierResult.getValue());
        return UnitResult.success();
    }

    private static Error validate(CreateCourierCommand command) {
        if (command == null) {
            return GeneralErrors.valueIsRequired("command");
        }

        return Guard.againstNullOrEmpty(command.name(), "name");
    }
}
