package microarch.delivery.core.domain.services;

import libs.errs.Error;
import libs.errs.UnitResult;
import microarch.delivery.core.domain.model.courier.Courier;
import microarch.delivery.core.domain.model.order.Order;

import java.util.List;

public class OrderDistributionDomainServiceImpl implements OrderDistributionDomainService {
    @Override
    public UnitResult<Error> distributeOrder(Order order, List<Courier> couriers) {
        
    }
}
