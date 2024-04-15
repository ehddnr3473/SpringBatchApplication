package yeolmok.tutorial.batchapp;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableBatchProcessing
@SpringBootApplication
public class BatchappApplication {

	public static void main(String[] args) {
		SpringApplication.run(BatchappApplication.class, args);
	}

}
