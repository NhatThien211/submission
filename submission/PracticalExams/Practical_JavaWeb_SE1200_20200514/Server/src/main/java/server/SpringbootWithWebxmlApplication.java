package server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@ServletComponentScan(basePackages = "com.practicalexam.student")
public class SpringbootWithWebxmlApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootWithWebxmlApplication.class, args);
//        changeExtensionHtmlToJspInCode();
//        convertHtmlFileToJspFile();
//        System.out.println(DatabaseTestUtils.checkMakeConnection());
    }

    @Bean
    public WebXmlBridge webXmlBridge() {
        return new WebXmlBridge();
    }

    private static void convertHtmlFileToJspFile() {
        String path = "C:\\Users\\HP\\Desktop\\Submit Project\\JavaWeb\\Server\\Server\\src\\main\\webapp";
        List<File> webAppFiles = new ArrayList<>();
        getAllFiles(path, webAppFiles, ".html");
        for (File file : webAppFiles) {
            Path source = Paths.get(file.getAbsolutePath());
            Path destination = Paths.get(path + File.separator + file.getName().replace(".html", "") + ".jsp");
            try {
                Files.copy(source, destination);
            } catch (IOException e) {
                System.out.println("Not replace");
            }
        }
    }

    private static void changeExtensionHtmlToJspInCode() {
        String studentPath = "C:\\Users\\HP\\Desktop\\Submit Project\\JavaWeb\\Server\\Server\\src\\main\\java\\com\\practicalexam\\student";
        List<File> studentCodeFiles = new ArrayList<>();
        getAllFiles(studentPath, studentCodeFiles, ".java");
        if (studentCodeFiles.size() > 0) {
            for (File file : studentCodeFiles) {

                try {
                    Path path = Paths.get(file.getAbsolutePath());
                    Charset charset = StandardCharsets.UTF_8;
                    String content = new String(Files.readAllBytes(path), charset);
                    content = content.replaceAll("\\.html\";", ".jsp\";");
                    Files.write(path, content.getBytes(charset));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void getAllFiles(String directoryName, List<File> files, String extension) {
        // Get all files from a directory.
        File directory = new File(directoryName);
        File[] fList = directory.listFiles();
        if (fList != null)
            for (File file : fList) {
                if (file.isFile()) {
                    if (file.getName().endsWith(extension)) {
                        files.add(file);
                    }
                } else if (file.isDirectory()) {
                    getAllFiles(file.getAbsolutePath(), files, extension);
                }
            }
    }

}
