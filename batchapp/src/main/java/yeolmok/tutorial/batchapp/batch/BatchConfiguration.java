package yeolmok.tutorial.batchapp.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import yeolmok.tutorial.batchapp.batch.listener.JobCompletionNotificationListener;
import yeolmok.tutorial.batchapp.batch.processor.CustomerCouponProcessor;
import yeolmok.tutorial.batchapp.batch.processor.CustomerPointProcessor;
import yeolmok.tutorial.batchapp.batch.record.Customer;

import javax.sql.DataSource;

@Configuration
public class BatchConfiguration {

    @Bean
    public DataSourceTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    /*
     * ItemReader
     * DB가 많은 행을 반환하는 경우, 메모리에 유지할 수 없음. 따라서 다음과 같은 기법이 존재.
     * - Cursor 기반: 1라인씩 읽어서 처리
     * - Paging 기반: 지정한 Page size 만큼 읽어서 처리
     */
    @Bean
    public JdbcCursorItemReader<Customer> reader(DataSource dataSource) {
        return new JdbcCursorItemReaderBuilder<Customer>()
                .name("jdbcCursorItemReader")
                .dataSource(dataSource)
                .sql("SELECT ID, Name, Point, CouponCount FROM CUSTOMER")
                .rowMapper((rs, rowNum) -> new Customer(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getInt(4)))
                .build();
    }

    @Bean
    public CustomerPointProcessor customerPointProcessor() { return new CustomerPointProcessor(); }

    @Bean
    public CustomerCouponProcessor customerCouponProcessor() { return new CustomerCouponProcessor(); }

    @Bean
    @Qualifier("pointWriter")
    public JdbcBatchItemWriter<Customer> pointWriter(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Customer>()
                .sql("UPDATE Customer SET Point = (:point) WHERE ID = (:id)")
                .dataSource(dataSource)
                .beanMapped()
                .build();
    }

    @Bean
    @Qualifier("couponWriter")
    public JdbcBatchItemWriter<Customer> couponWriter(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Customer>()
                .sql("UPDATE Customer SET CouponCount = (:couponCount) WHERE ID = (:id)")
                .dataSource(dataSource)
                .beanMapped()
                .build();
    }

    @Bean
    public Job customerEventJob(JobRepository jobRepository,
                                @Qualifier("pointStep") Step pointStep,
                                @Qualifier("couponStep") Step couponStep,
                                JobCompletionNotificationListener jobCompletionNotificationListener) {
        return new JobBuilder("customerEventJob", jobRepository)
                .listener(jobCompletionNotificationListener)
                .start(pointStep)
                .next(couponStep)
                .build();
    }

    /*
     * Step
     * name과 함께 step 처리, 중복된 이름으로 step을 수행하려고 한다면 아래와 같은 로그 출력
     * Step already complete or not restartable, so no action to execute: StepExecution: id=1, version=3, name=step1, status=COMPLETED, exitStatus=COMPLETED, ...
     */
    @Bean
    @Qualifier("pointStep")
    public Step pointStep(JobRepository jobRepository,
                          DataSourceTransactionManager transactionManager,
                          JdbcCursorItemReader<Customer> reader,
                          CustomerPointProcessor customerPointProcessor,
                          @Qualifier("pointWriter") JdbcBatchItemWriter<Customer> pointWriter) {
        return new StepBuilder("pointStep", jobRepository)
                .<Customer, Customer> chunk(3, transactionManager)
                .reader(reader)
                .processor(customerPointProcessor)
                .writer(pointWriter)
                .build();
    }

    @Bean
    @Qualifier("couponStep")
    public Step couponStep(JobRepository jobRepository,
                           DataSourceTransactionManager transactionManager,
                           JdbcCursorItemReader<Customer> reader,
                           CustomerCouponProcessor customerCouponProcessor,
                           @Qualifier("couponWriter") JdbcBatchItemWriter<Customer> couponWriter) {
        return new StepBuilder("couponStep", jobRepository)
                .<Customer, Customer> chunk(3, transactionManager)
                .reader(reader)
                .processor(customerCouponProcessor)
                .writer(couponWriter)
                .build();
    }
}
