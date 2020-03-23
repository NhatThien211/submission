package server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

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
