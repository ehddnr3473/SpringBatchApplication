package yeolmok.tutorial.batchapp.batch.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import yeolmok.tutorial.batchapp.batch.record.Customer;

public class CustomerCouponProcessor implements ItemProcessor<Customer, Customer> {

    private static final Logger logger = LoggerFactory.getLogger(CustomerCouponProcessor.class);

    @Override
    public Customer process(Customer customer) {
        final int couponCount = customer.couponCount() + 1;

        final Customer transformedCustomer = new Customer(customer.id(), customer.name(), customer.point(), couponCount);

        logger.info("----Coupon Process information----");
        logger.info("Converting (" + customer + ") into (" + transformedCustomer + ")");
        logger.info("---------------------------");

        return transformedCustomer;
    }
}
