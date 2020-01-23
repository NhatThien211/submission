package com.fpt.submission.service.serviceImpl;

import com.fpt.submission.constants.PathConstants;
import com.fpt.submission.dto.request.UploadFileDto;
import com.fpt.submission.service.SubmissionService;
import com.fpt.submission.utils.CmdExcution;
import com.fpt.submission.utils.FileUtils;
import com.fpt.submission.utils.ZipFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class SubmissionServiceImpl implements SubmissionService {

    private final FileUtils fileUtils;

    @Autowired
    public SubmissionServiceImpl(FileUtils fileUtils) {
        this.fileUtils = fileUtils;
    }

    @Override
    public String submit(UploadFileDto dto) {
//        fileUtils.uploadFile(dto);
        try {
            File folder = new File(PathConstants.PATH_JAVA_TEST);
            for (final File fileEntry : folder.listFiles()) {
                if (!fileEntry.isDirectory()) {
                    if (dto.getStudentCode().equals("SE62847")) {
                        Path sourceDirectory = Paths.get(PathConstants.PATH_JAVA_TEST + File.separator + "De2.java");
                        Path targetDirectory = Paths.get(PathConstants.PATH_JAVA_FOLDER_TEST + "De2.java");

                        //copy source to target using Files Class
                        Files.copy(sourceDirectory, targetDirectory);
                        break;
                    }
                }
            }

            ZipFile.unzip(PathConstants.PATH_JAVA_WEB_SUBMIT + File.separator + "SE62847.zip", PathConstants.PATH_JAVA_FOLDER);
            CmdExcution.execute(PathConstants.EXECUTE_MAVEN_CMD);
            File file = new File(PathConstants.PATH_JAVA_FOLDER_COM);
            if (FileUtils.deleteFolder(file)) {
                System.out.println("xoa bai lam roi nha!");
            }
            File scriptFile = new File(PathConstants.PATH_JAVA_FOLDER_TEST + "De2.java");
            if (scriptFile.delete()) {
                System.out.println("xoa script roi nha!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "Success";
    }

    public void extractFolder(String path, String destDirectory) throws IOException, InterruptedException {
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();

        for (File file : listOfFiles) {
            if (file.isFile()) {
                ZipFile.unzip(file.getAbsolutePath(), destDirectory);
                CmdExcution.execute(PathConstants.EXECUTE_MAVEN_CMD);
            }
        }
    }
}
