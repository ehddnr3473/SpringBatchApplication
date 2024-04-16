package yeolmok.tutorial.batchapp.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class CustomerItemProcessor implements ItemProcessor<Customer, Customer> {

    private static final Logger logger = LoggerFactory.getLogger(CustomerItemProcessor.class);

    @Override
    public Customer process(final Customer customer) {
        final int point = customer.point() + 500;

        final Customer transformedCustomer = new Customer(customer.id(), customer.name(), point);

        logger.info("Converting (" + customer + ") into (" + transformedCustomer + ")");

        return transformedCustomer;
    }
}
