package server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringbootWithWebxmlApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootWithWebxmlApplication.class, args);
	}

	@Bean
	public WebXmlBridge webXmlBridge() {
		return new WebXmlBridge();
	}
}
