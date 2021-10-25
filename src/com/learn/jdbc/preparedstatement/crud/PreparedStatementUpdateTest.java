package com.learn.jdbc.preparedstatement.crud;

import com.learn.jdbc.ConnectionTest;
import com.learn.jdbc.utils.JDBCUtils;
import org.junit.Test;

import java.io.InputStream;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Properties;

/**
 * 使用PreparedStatement替换Statement，实现对数据的增删改操作
 */

public class PreparedStatementUpdateTest {
    /**
     * 向customers表中插入一条数据
     */
    @Test
    public void testInsert() {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
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
            conn = DriverManager.getConnection(url, user, password);
            //4.预编译sql语句，返回
            String sql = "insert into customers(name,email,birth) values(?,?,?)";
            ps = conn.prepareStatement(sql);
            //5.填充占位符 , 下标索引从1开始 ，数据类型依据数据表中的真实类型。
            ps.setString(1, "哪吒");
            ps.setString(2, "nezha@gmail.com");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date date = sdf.parse("1000-01-01");
            ps.setDate(3, new Date(date.getTime()));
            //6.执行sql操作
            ps.execute();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //7.关闭资源
            try {
                if (ps != null) {
                    ps.close();
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

    /**
     * 修改Customers表中一条数据
     */
    @Test
    public void testUpdate() {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            //1.获取数据库的连接
            conn = JDBCUtils.getConnection();
            //2.预编译sql语句，返回PreparedStatement的实例
            String sql = "UPDATE customers SET name = ? WHERE id = ?";
            ps = conn.prepareStatement(sql);
            //3.填充占位符
            ps.setObject(1, "莫扎特");
            ps.setObject(2, 18);
            //4.执行
            ps.execute();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //5.资源的关闭
            JDBCUtils.close(conn, ps);
        }


    }
    @Test
    public void testCommonUpdate(){
        String sql = "delete from customers where id = ?";
        update(sql,3);
    }
    // 通用的增删改操作
    public void update(String sql, Object... args) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            //1.获取数据库的连接
            conn = JDBCUtils.getConnection();
            //2.预编译sql语句，返回PrepareStatement实例
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i+1, args[i]);
            }
            //4.执行
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //5.资源关闭
            JDBCUtils.close(conn, ps);
        }

    }
}
