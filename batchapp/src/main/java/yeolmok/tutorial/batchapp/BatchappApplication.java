package yeolmok.tutorial.batchapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*
 * Batch 처리는 웹 앱과 WAR 파일에 내장될 수도 있음.
 * 또는 실행 가능한 JAR 파일에 패키징하여, 독립 실행형 애플리케이션으로 간단하게 만들 수도 있음.
 */
@SpringBootApplication
public class BatchappApplication {

	public static void main(String[] args) {
		// Job을 완료하고 프로세스를 종료
		System.exit(SpringApplication.exit(SpringApplication.run(BatchappApplication.class, args)));
	}
}
