package com.learn.jdbc.utils;

import com.learn.jdbc.ConnectionTest;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class JDBCUtils {
    /**
     * 获取数据库的连接
     *
     * @return
     * @throws Exception
     */
    public static Connection getConnection() throws Exception {
        //1.读取配置文件中的4个基本信息
        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("jdbc.properties");
        Properties pros = new Properties();
        pros.load(is);
        if (is != null) {
            is.close();
        }
        String user = pros.getProperty("user");
        String password = pros.getProperty("password");
        String url = pros.getProperty("url");
        String driverClass = pros.getProperty("driverClass");
        //2.加载驱动
        Class.forName(driverClass);
        //3.获取连接
        Connection conn = DriverManager.getConnection(url, user, password);
        return conn;
    }

    public static void close(Connection conn, Statement s) {
        try {
            if (s != null) {
                s.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
