/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package QLTV.Model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author dinhd
 */
public class DBConnection {
    private static final String HOST = "localhost";
    private static final String PORT = "3306";
    private static final String DB   = "thuvienutt";
    private static final String USER = "root";
    private static final String PASS = "123456";

    private static final String URL ="jdbc:mysql://" + HOST + ":" + PORT + "/" + DB+ "?useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=UTC";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
