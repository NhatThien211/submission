package com.fpt.submission.utils;

import com.fpt.submission.constants.CommonConstant;

import java.io.*;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipFile {

    private static final int BUFFER_SIZE = 4096;

    //    Zip folder
    public static void zipFolder(String folder, String outPath) throws IOException {
        FileOutputStream fos = new FileOutputStream(outPath + ".zip");
        ZipOutputStream zipOut = new ZipOutputStream(fos);
        File fileToZip = new File(folder);
        zipFile(fileToZip, fileToZip.getName(), zipOut);
        zipOut.close();
        fos.close();
    }

    //    Zip folder
    public static void zipProject(String folder, String outPath, String extension) throws IOException {
        FileOutputStream fos = new FileOutputStream(outPath + ".zip");
        ZipOutputStream zipOut = new ZipOutputStream(fos);
        File fileToZip = new File(folder);
        if (fileToZip.getName().contains("EXAM_")) {
            String scriptFile = fileToZip.getParent() + File.separator + "TestScript" + extension;
            zipFile(fileToZip, scriptFile, zipOut);
        } else {
            zipFile(fileToZip, fileToZip.getName(), zipOut);
        }
        zipOut.close();
        fos.close();
    }

    public static void downloadZip(File fileParam, OutputStream output) {
        try {
            File file = fileParam;
            if (file.exists()) {
                FileInputStream fis = new FileInputStream(file);
                byte[] buffer = new byte[4096];
                int b = -1;
                while ((b = fis.read(buffer)) != -1) {
                    output.write(buffer, 0, b);
                }
                fis.close();
                output.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void zipFile(File fileToZip, String fileName, ZipOutputStream zipOut) throws IOException {
        if (!fileName.contains("DBUtilities") && !fileName.contains(".log") && !fileName.contains(".lock")) {
            if (fileToZip.isHidden()) {
                return;
            }
            if (fileToZip.isDirectory()) {
                if (fileName.endsWith("/")) {
                    zipOut.putNextEntry(new ZipEntry(fileName));
                    zipOut.closeEntry();
                } else {
                    zipOut.putNextEntry(new ZipEntry(fileName + "/"));
                    zipOut.closeEntry();
                }
                File[] children = fileToZip.listFiles();
                for (File childFile : children) {
                    zipFile(childFile, fileName + "/" + childFile.getName(), zipOut);
                }
                return;
            }
            FileInputStream fis = new FileInputStream(fileToZip);
            ZipEntry zipEntry = new ZipEntry(fileName);
            zipOut.putNextEntry(zipEntry);
            byte[] bytes = new byte[1024];
            int length;
            while ((length = fis.read(bytes)) >= 0) {
                zipOut.write(bytes, 0, length);
            }
            fis.close();
        }
    }

    public static void unzip(String zipFilePath, String destDirectory) throws IOException {
        File destDir = new File(destDirectory);
        if (!destDir.exists()) {
            destDir.mkdir();
        }
        File file = new File(destDirectory + File.separator + "student");
        if (!file.exists()) {
            file.mkdir();
        }

        ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath));
        ZipEntry entry = zipIn.getNextEntry();
        while (entry != null) {

//            String pattern = Pattern.quote(S);
            String[] mainArr = null;
            String[] arr = entry.getName().split("/");
            String[] splittedFileName = entry.getName().split("\\\\");
            if(arr.length > splittedFileName.length){
                mainArr = arr;
            }else{
                mainArr = splittedFileName;
            }
            System.out.println(mainArr);
//          String splittedFileName1 =entry.getName().split();
            String filePath = destDirectory;
            for (int i = 0; i < mainArr.length; i++) {
                // if the entry is a directory, make the directory
                filePath += File.separator + mainArr[i];
                File dir = new File(filePath);
                if (!dir.exists() && !dir.isFile() && !filePath.contains(".")) {
                    dir.mkdir();
                }
            }
            String filePathStr = destDirectory + File.separator + entry.getName();
            if (!entry.isDirectory()) {
                // if the entry is a file, extracts it
                extractFile(zipIn, filePathStr);
            } else {
                // if the entry is a directory, make the directory
                File dir = new File(filePathStr);
                if (!dir.exists()) {
                    dir.mkdir();
                }
            }
            zipIn.closeEntry();
            entry = zipIn.getNextEntry();
        }
        zipIn.close();
    }

    public static void unzip2(String zipFilePath, String destDirectory) throws IOException {
        byte[] buffer = new byte[1024];
        ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFilePath));
        ZipEntry zipEntry = zis.getNextEntry();
        File destDir = new File(destDirectory);
        while (zipEntry != null) {
            File newFile = newFile(destDir, zipEntry);
            FileOutputStream fos = new FileOutputStream(newFile);
            int len;
            while ((len = zis.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
            }
            fos.close();
            zipEntry = zis.getNextEntry();
        }
        zis.closeEntry();
        zis.close();
    }

    public static File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
        File destFile = new File(destinationDir, zipEntry.getName());

        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();

        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }

        return destFile;
    }

    private static void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
        File file = new File(filePath);
        if (file.isDirectory() && file.exists()) {
            file.mkdir();
        }
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
        byte[] bytesIn = new byte[BUFFER_SIZE];
        int read = 0;
        while ((read = zipIn.read(bytesIn)) != -1) {
            bos.write(bytesIn, 0, read);
        }
        bos.close();
    }

}

