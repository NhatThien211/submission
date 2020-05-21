package com.fpt.submission.constants;

import java.io.File;

import static com.fpt.submission.utils.PathUtils.PROJECT_DIR;

public class CommonConstant {

    public static final int SOCKET_SERVER_LISTENING_PORT = 9997;
    public static final int SOCKET_SERVER_LISTENING_PORT_SUBMISSION = 9996;
    public static final String SOCKET_SERVER_LOCAL_HOST = "localhost";
    public static final String CODE_PRACTICAL_JAVA = "Practical_Java";
    public static final String CODE_PRACTICAL_C = "Practical_C";
    public static final String CODE_PRACTICAL_JAVA_WEB = "Practical_JavaWeb";
    public static final String CODE_PRACTICAL_CSharp = "Practical_CSharp";

    public static final String EXTENSION_ZIP = ".zip";
    public static final String EXTENSION_TXT = ".txt";
    public static final String EXTENSION_JAVA = ".java";
    public static final String EXTENSION_CSHARP = ".cs";
    public static final String EXTENSION_C = ".c";
    public static final String EXTENSION_CONFIG = ".properties";
    public static final String ZIP_PATH = PROJECT_DIR + File.separator + "ZipFiles";

    public static final String URL_WEBSERVICE_SERVER = "http://localhost:2021/api/practical-exam/submission";

}
