package com.fpt.submission.service.serviceImpl;

import com.fpt.submission.constants.PathConstants;
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

    @Override
    public void downloadFile(HttpServletResponse response) {
        try {
            ZipFile.zipping(PathConstants.PATH_JAVA_WEB_SUBMIT, PathConstants.PATH_JAVA_WEB_SUBMIT_FILE);
            String filePath = PathConstants.PATH_JAVA_WEB_SUBMIT_FILE;
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
