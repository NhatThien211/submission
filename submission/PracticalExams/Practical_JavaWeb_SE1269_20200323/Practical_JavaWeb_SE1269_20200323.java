/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.practicalexam.student.connection;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;

/**
 * @author Kieu Trong Khanh
 */
public class DBUtilities implements Serializable {

    public static Connection makeConnection() {
        Connection con = null;
        // Add code get connection here
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String url = "jdbc:sqlserver://localhost:1433;databaseName=PracticalTest;instanceName=SQL2017";
            con = DriverManager.getConnection(url, "sa", "12");
        } catch (Exception e) {
            e.printStackTrace();
        }
        //
        return con;
    }

}
