package com.fpt.submission.constants;

import java.io.File;

public class PathConstants {
    public static final String PROJECT_DIR = System.getProperty("user.dir");

    public static final String PATH_PRACTICAL = PROJECT_DIR + File.separator + "Practical";

    public static final String PATH_JAVA_WEB_SUBMIT = PATH_PRACTICAL + File.separator + "JavaWebSubmit";

    public static final String PATH_JAVA_WEB_SUBMIT_FILE = PATH_JAVA_WEB_SUBMIT + ".zip";

    public static final String PATH_JAVA_SERVER = PROJECT_DIR + File.separator + "ServerJavaWeb";

    public static final String PATH_JAVA_FOLDER = PATH_JAVA_SERVER + File.separator
                                                                        + "src" + File.separator
                                                                        + "main" + File.separator
                                                                        + "java" + File.separator;

    public static final String PATH_JAVA_FOLDER_TEST = PATH_JAVA_SERVER + File.separator
            + "src" + File.separator
            + "test" + File.separator
            + "java" + File.separator
            + "com" + File.separator
            + "thucnh" + File.separator
            + "azuredevops" + File.separator;

    public static final String PATH_JAVA_FOLDER_COM = PATH_JAVA_FOLDER + File.separator + "com";

    public static final String PATH_JAVA_TEST = PROJECT_DIR + File.separator + "ScriptFiles" + File.separator + "Java";

    public static final String EXECUTE_MAVEN_CMD = "cd " + PATH_JAVA_SERVER + "&mvn clean package";

}
