package yeolmok.tutorial.batchapp.batch.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import yeolmok.tutorial.batchapp.batch.record.Customer;

public class CustomerPointProcessor implements ItemProcessor<Customer, Customer> {

    private static final Logger logger = LoggerFactory.getLogger(CustomerPointProcessor.class);

    @Override
    public Customer process(final Customer customer) {
        final int point = customer.point() + 500;

        final Customer transformedCustomer = new Customer(customer.id(), customer.name(), point, customer.couponCount());

        logger.info("----Point Process information----");
        logger.info("Converting (" + customer + ") into (" + transformedCustomer + ")");
        logger.info("---------------------------");

        return transformedCustomer;
    }
}
