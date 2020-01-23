package com.fpt.submission.constants;

import java.io.File;

public class PathConstants {
    private static final String PROJECT_DIR = System.getProperty("user.dir");

    public static final String PATH_PRACTICAL = PROJECT_DIR + File.separator + "Practical";

    public static final String PATH_JAVA_WEB_SUBMIT = PATH_PRACTICAL + File.separator + "JavaWebSubmit";

    public static final String PATH_JAVA_WEB_SUBMIT_FILE = PATH_JAVA_WEB_SUBMIT + ".zip";

}
