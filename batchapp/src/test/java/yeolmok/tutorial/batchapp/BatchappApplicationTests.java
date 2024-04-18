package yeolmok.tutorial.batchapp;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

/*
 * Batch job을 테스트하는 것은 굉장히 중요함.
 * 예를 들어, 웹 애플리케이션의 경우, QA팀에서 직접 화면을 보며, 또는 더 나아가 소스 코드를 까보며 테스트할 수 있지만,
 * Batch 작업의 경우 직접 DB와 로그를 까보기는 쉽지 않기 때문에, 테스트를 통해 사전에 버그를 예방하는 것이 중요하다.
 *
 * End To End Test
 * - Batch job의 시작부터 끝까지 전체 과정을 테스트
 * - 테스트 조건을 설정하고, job을 실행하고, 결과를 확인할 수 있음.
 */
@SpringBatchTest
@SpringBootTest
@SpringJUnitConfig(BatchappApplication.class)
public class BatchappApplicationTests {

	private static final Logger logger = LoggerFactory.getLogger(BatchappApplicationTests.class);

	@Autowired
	private JobLauncherTestUtils jobLauncherTestUtils;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Test
	public void testJob(@Autowired Job job) throws Exception {
		this.jobLauncherTestUtils.setJob(job);

		// Given
		for(int i = 0; i < 6; i++) {
			String name = "Batch Test " + String.valueOf(i);
			this.jdbcTemplate.update("INSERT INTO Customer (Name, point, couponCount) VALUES (?, 0, 0)", name);
		}

		// When
		JobExecution jobExecution = jobLauncherTestUtils.launchJob();
		logger.info("Batch Exit Status: " + jobExecution.getExitStatus().getExitCode());

		// Then
		Assertions.assertEquals("COMPLETED", jobExecution.getExitStatus().getExitCode());
	}
}
