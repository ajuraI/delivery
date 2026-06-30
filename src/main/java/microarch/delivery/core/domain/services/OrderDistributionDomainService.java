package microarch.delivery.core.domain.services;

import libs.errs.UnitResult;
import libs.errs.Error;
import microarch.delivery.core.domain.model.courier.Courier;
import microarch.delivery.core.domain.model.order.Order;

import java.util.List;

public interface OrderDistributionDomainService {
    UnitResult<Error> distributeOrder(Order order, List<Courier> couriers);
}
