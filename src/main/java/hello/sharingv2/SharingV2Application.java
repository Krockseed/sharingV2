package hello.sharingv2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SharingV2Application {

	public static void main(String[] args) {
		SpringApplication.run(SharingV2Application.class, args);
	}

}
