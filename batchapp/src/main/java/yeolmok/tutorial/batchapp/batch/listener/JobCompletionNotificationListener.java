package yeolmok.tutorial.batchapp.batch.listener;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import yeolmok.tutorial.batchapp.batch.record.Customer;

@Component
@RequiredArgsConstructor
public class JobCompletionNotificationListener implements JobExecutionListener {

    private static final Logger logger = LoggerFactory.getLogger(JobCompletionNotificationListener.class);

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            logger.info("----JOB FINISHED.----");

            jdbcTemplate
                    .query("SELECT ID, Name, Point, CouponCount FROM Customer", new DataClassRowMapper<>(Customer.class))
                    .forEach(customer -> logger.info("Found <{{}}> in the database.", customer));
        }
    }
}
