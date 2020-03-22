package server.databasetestutil;

import com.practicalexam.student.connection.DBUtilities;
import com.practicalexam.student.khanhkt.registration.RegistrationDTO;

import javax.naming.NamingException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseTestUtils implements Serializable {


    public static boolean checkByUsername(String usernameTest)
            throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        boolean check = false;
        try {
            //1. Connection
            con = DBUtilities.makeConnection();
            if (con != null) {
                //2. Create Sql String
                String sql = "Select username "
                        + "From Registration "
                        + "Where username = ?";
                //3. Create statement
                stm = con.prepareStatement(sql);
                stm.setString(1, usernameTest);
                //4. Execute Query
                rs = stm.executeQuery();
                //5. Process
                if (rs.next()) {
                    check = true;
                }//end while rs is not null
            }//end if con is not null
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stm != null) {
                stm.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return check;
    }

    public static boolean checkUpdate(String usernameTest, String passwordTest)
            throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        boolean check = false;
        try {
            //1. Connection
            con = DBUtilities.makeConnection();
            if (con != null) {
                //2. Create Sql String
                String sql = "Select username, password "
                        + "From Registration "
                        + "Where username = ? AND password = ?";
                //3. Create statement
                stm = con.prepareStatement(sql);
                stm.setString(1, usernameTest);
                stm.setString(2, passwordTest);
                //4. Execute Query
                rs = stm.executeQuery();
                //5. Process
                if (rs.next()) {
                    check = true;
                }//end while rs is not null
            }//end if con is not null
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stm != null) {
                stm.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return check;
    }
}
