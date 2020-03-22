package server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@ServletComponentScan(basePackages = "com.practicalexam.student")
public class SpringbootWithWebxmlApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootWithWebxmlApplication.class, args);
	}

	@Bean
	public WebXmlBridge webXmlBridge() {
		return new WebXmlBridge();
	}
}
