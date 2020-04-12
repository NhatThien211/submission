package server.databasetestutil;

import com.practicalexam.student.connection.DBUtilities;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DatabaseTestUtils implements Serializable {

    private static String PROJECT_DIR = System.getProperty("user.dir");

    private static String PATH_CONTEXT_FILE = PROJECT_DIR +
            File.separator
            + "src"
            + File.separator
            + "main"
            + File.separator
            + "webapp"
            + File.separator
            + "META-INF"
            + File.separator
            + "context.xml";


    private static String PATH_DBUTILITIES_CHECKED = PROJECT_DIR
            + File.separator + "src"
            + File.separator + "main"
            + File.separator + "java"
            + File.separator + "com"
            + File.separator + "practicalexam"
            + File.separator + "student"
            + File.separator + "DBUtilities.java";

    public static boolean checkMakeConnection() {
        boolean check = true;
        System.out.println(PROJECT_DIR);
        String connectionContextStr = "";
        connectionContextStr = readFileAsString(PATH_CONTEXT_FILE).trim();
        if (!connectionContextStr.contains("com.microsoft.sqlserver.jdbc.SQLServerDriver")) {
            check = false;
        }
        if (!connectionContextStr.contains("jdbc:sqlserver://localhost:")) {
            check = false;
        }
        if (!connectionContextStr.contains("type=\"javax.sql.DataSource\"")) {
            check = false;
        }
        if (check) {

        }
        return check;
    }

    private static String readFileAsString(String fileName) {
        String text = "";
        try {
            text = new String(Files.readAllBytes(Paths.get(fileName)));
        } catch (IOException e) {
        }
        return text;
    }

    public static boolean checkExistAfterDelete(String findKey) {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        boolean check = false;
        try {
            //1. Connection
            con = DBUtilities.makeConnection();
            if (con != null) {
                //2. Create Sql String
                String sql = "SELECT amourId "
                        + "FROM tbl_Weapon Where  amourId = ?";
                //3. Create statement
                stm = con.prepareStatement(sql);
                stm.setString(1, findKey);
                //4. Execute Query
                rs = stm.executeQuery();
                //5. Process
                if (rs.next()) {
                    check = true;
                }//end while rs is not null
            }//end if con is not null
        } catch (Exception e) {
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stm != null) {
                    stm.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
            }
        }
        return check;
    }

    public static boolean preparedData() {
        Connection con = null;
        PreparedStatement stm = null;
        boolean check = false;
        try {
            //1. Connection
            con = DBUtilities.makeConnection();
            if (con != null) {
                //2. Create Sql String
                String sql = "Insert into tbl_Weapon(amourId, description, classification, defense, timeOfCreate, status)" +
                        " Values ('AM01','AM01','AM01','2020-03-12','123','true')," +
                        "('AM02','AM02','AM02','2020-03-12','123','true'), " +
                        "('AM03','AM03','AM03','2020-03-12','123','true')";
                //3. Create statement
                stm = con.prepareStatement(sql);
                //4. Execute Query
                int rowEffect = stm.executeUpdate();
                //5. Process
                if (rowEffect > 0) {
                    return true;
                }
                //5. Process
            }//end if con is not null
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stm != null) {
                    stm.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
            }
        }
        return check;
    }

    public static void cleanData() {
        Connection con = null;
        PreparedStatement stm = null;

        try {
            //1. Connection
            con = DBUtilities.makeConnection();
            if (con != null) {
                //2. Create Sql String
                String sql = "Delete From tbl_Weapon";
                //3. Create statement
                stm = con.prepareStatement(sql);
                //4. Execute Query
                stm.executeUpdate();
                //5. Process

            }//end if con is not null
        } catch (Exception e) {
        } finally {
            try {
                if (stm != null) {
                    stm.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
            }
        }
    }
}
