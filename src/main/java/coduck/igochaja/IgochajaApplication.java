package coduck.igochaja;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing

public class IgochajaApplication {

	public static void main(String[] args) {
		SpringApplication.run(IgochajaApplication.class, args);
	}
}
