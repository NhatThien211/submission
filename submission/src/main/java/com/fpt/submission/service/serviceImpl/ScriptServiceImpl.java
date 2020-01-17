package com.fpt.submission.service.serviceImpl;

import com.fpt.submission.exception.CustomException;
import com.fpt.submission.service.ScriptService;
import com.fpt.submission.utils.ZipFile;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

@Service
public class ScriptServiceImpl implements ScriptService {

    private static final String PREFIX_START = "//start";
    private static final String PREFIX_END = "//end";
    private static final String TEMPLATE_SCRIPT_JAVA = "static/ScripTestJava.java";

    private static final String PROJECT_DIR = System.getProperty("user.dir");

    @Override
    public void downloadFile(HttpServletResponse response) {
        String pracPath = null;
        try {
            pracPath = PROJECT_DIR + File.separator + "Practical";
            ZipFile.zipping(pracPath + File.separator + "JavaWebSubmit", pracPath + File.separator + "JavaWebSubmit");
            String filePath = pracPath + File.separator + "JavaWebSubmit.zip";

            File file = new File(filePath);
            String mimeType = "application/octet-stream";
            response.setContentType(mimeType);
            response.addHeader("Content-Disposition", "attachment; filename=" + file.getName());
            response.setContentLength((int) file.length());
            OutputStream os = null;
            os = response.getOutputStream();

            ZipFile.downloadZip(file, os);
        } catch (FileNotFoundException e) {
            throw new CustomException(HttpStatus.CONFLICT, e.getMessage());
        } catch (IOException e) {
            throw new CustomException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

}
