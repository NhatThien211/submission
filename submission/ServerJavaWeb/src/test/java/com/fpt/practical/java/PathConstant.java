package com.fpt.practical.java;

import java.io.File;

public class PathConstant {

    public static final String PROJECT_DIR = System.getProperty("user.dir");
    public static final String PATH_JAVA_SERVER = PROJECT_DIR + File.separator + "ServerJavaWeb";
    public static final String PATH_JAVA_FOLDER_TEST = PATH_JAVA_SERVER + File.separator
            + "src" + File.separator
            + "test" + File.separator
            + "java" + File.separator
            + "com" + File.separator
            + "fpt" + File.separator
            + "practical" + File.separator
            + "java";
}
