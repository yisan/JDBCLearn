package com.learn.jdbc.connection;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.junit.Test;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;

public class C3P0Test {
    @Test
    public void testGetConnection() throws Exception {
        //获取c3p0连接池
        ComboPooledDataSource cpds = new ComboPooledDataSource();
        cpds.setDriverClass("com.mysql.cj.jdbc.Driver"); //loads the jdbc driver
        cpds.setJdbcUrl("jdbc:mysql://localhost:3306/test?rewriteBatchedStatements=true");
        cpds.setUser("root");
        cpds.setPassword("Bingoing");
        //通过设置相关的参数，对数据库连接池进行管理。
        // 设置初始时数据库连接池中的连接数
        cpds.setInitialPoolSize(10);
        Connection conn = cpds.getConnection();
        System.out.println(conn);
    }

    /**
     * 使用配置文件
     */
    @Test
    public void  testGetConnection1() throws SQLException {

        ComboPooledDataSource cpds = new ComboPooledDataSource("helloc3p0");
        Connection conn = cpds.getConnection();
        System.out.println(conn);
    }
}
