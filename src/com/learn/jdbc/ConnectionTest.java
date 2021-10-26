package com.learn.jdbc;

import org.junit.Test;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionTest {
    @Test
    public void testConnection1() {
        try {
            //1.显式调用第三方数据库api，获取Driver实现类对象。
            Driver driver = new com.mysql.cj.jdbc.Driver();
            //2.要连接的数据库url
            String url = "jdbc:mysql://localhost:3306/test";
            //3.连接mysql所需用户名和密码(你本机安装mysql时设置的用户名和密码)
            Properties info = new Properties();
            info.setProperty("user", "root");
            info.setProperty("password", "Bingoing");
            //4.调用driver的connect(),建立连接
            Connection conn = driver.connect(url, info);
            System.out.println(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    @Test
    public void testConnection2(){
        try {
            //1.反射方式获取Driver实现类对象。
            Class<?> clazz = Class.forName("com.mysql.cj.jdbc.Driver");
            Driver driver= (Driver) clazz.newInstance();
            //2.要连接的数据库url
            String url = "jdbc:mysql://localhost:3306/test";
            //3.连接mysql所需用户名和密码(你本机安装mysql时设置的用户名和密码)
            Properties info = new Properties();
            info.setProperty("user", "root");
            info.setProperty("password", "Bingoing");
            //4.调用driver的connect(),建立连接
            Connection conn = driver.connect(url,info);
            System.out.println(conn);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public  void testConnection3(){
        try {
            //1.连接数据的4个基本要素
            String url = "jdbc:mysql://localhost:3306/test";
            String user = "root";
            String password = "abc123";
            String driverName = "com.mysql.cj.jdbc.Driver";
            //2.反射方式获取Driver实现类对象。
            Class<?> clazz = Class.forName(driverName);
            Driver driver= (Driver) clazz.newInstance();
            //3.注册驱动
            DriverManager.registerDriver(driver);
            //4.获取连接
            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println(conn);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public  void testConnection4(){
        try {
            //1.连接数据的4个基本要素
            String url = "jdbc:mysql://localhost:3306/test";
            String user = "root";
            String password = "Bingoing";
            String driverName = "com.mysql.cj.jdbc.Driver";
            //2.加载驱动 （①实例化Driver ②注册驱动）
            Class.forName(driverName);
            //Class<?> clazz = Class.forName(driverName);
            //Driver driver= (Driver) clazz.newInstance();
            //3.获取连接
            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println(conn);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public void testConnection5(){
        try {
            //1.读取配置文件中的4个基本信息
            InputStream is = ConnectionTest.class.getClassLoader().getResourceAsStream("jdbc.properties");
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
            System.out.println(conn);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
