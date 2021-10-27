package com.learn.jdbc.utils;

import com.learn.jdbc.ConnectionTest;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.sql.*;
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
    public static void close(Connection conn, Statement s, ResultSet rs) {
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

        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    // 通用的增删改操作 -- v1.0 -- 未考虑事务的操作
    public  static int update(String sql, Object... args) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            //1.获取数据库的连接
            conn = JDBCUtils.getConnection();
            //2.预编译sql语句，返回PrepareStatement实例
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            //4.执行
            /*
             * ps.execute()
             * 如果执行的是查询操作，有返回结果，则此方法返回true
             * 如果执行的是增删改操作，没有返回结果，则此方法返回false
             * ps.executeUpdate()
             * 返回值表示影响的行数，如果大于0 表明操作成功，返回值为0 表明操作失败
             */
            //方式一
//            ps.execute();
            //方式二
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //5.资源关闭
            JDBCUtils.close(conn, ps);
        }
        return 0;
    }

    /**
     * 通用的增删改操作 -- v2.0 -- 考虑事务操作
     * @param sql
     * @param args
     * @return
     */
    public  static int update(Connection conn,String sql, Object... args) {
        PreparedStatement ps = null;
        try {
            //1.预编译sql语句，返回PrepareStatement实例
            //2.填充占位符
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            //3.执行
            /*
             * ps.execute()
             * 如果执行的是查询操作，有返回结果，则此方法返回true
             * 如果执行的是增删改操作，没有返回结果，则此方法返回false
             * ps.executeUpdate()
             * 返回值表示影响的行数，如果大于0 表明操作成功，返回值为0 表明操作失败
             */
            //方式一
//            ps.execute();
            //方式二
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //4.资源关闭
            JDBCUtils.close(null, ps);
        }
        return 0;
    }
    /**
     * 针对不同的表的通用的查询操作，返回表中的一条记录  -- v1.0 -- 考虑事务
     *
     * @param clazz
     * @param sql
     * @param args
     * @param <T>
     * @return
     */
    public static  <T> T getInstance(Connection conn,Class<T> clazz, String sql, Object... args) {
        ResultSet resultSet = null;
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            resultSet = ps.executeQuery();
            //获取ResultSet的元数据
            ResultSetMetaData metaData = resultSet.getMetaData();
            //通过ResultSetMetaData获取结果集中的列数
            int columnCount = metaData.getColumnCount();
            if (resultSet.next()) {
                T t = clazz.newInstance();
                //处理结果集一行数据中的每一列
                for (int i = 0; i < columnCount; i++) {
                    //获取列值
                    Object columnValue = resultSet.getObject(i + 1);
                    //获取每个列的别名
                    String columnLabel = metaData.getColumnLabel(i + 1);
                    //通过反射将columnValue 赋值给对象指定的columnName属性
                    Field field = clazz.getDeclaredField(columnLabel);
                    //属性修饰符可能为private，这里设置可以访问
                    field.setAccessible(true);
                    field.set(t, columnValue);
                }
                return t;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(null, ps, resultSet);
        }
        return null;
    }
    /**
     * 针对不同的表的通用的查询操作，返回表中的一条记录  -- v1.0 -- 未考虑事务
     *
     * @param clazz
     * @param sql
     * @param args
     * @param <T>
     * @return
     */
    public static  <T> T getInstance(Class<T> clazz, String sql, Object... args) {
        ResultSet resultSet = null;
        PreparedStatement ps = null;
        Connection conn = null;
        try {
            conn = getConnection();
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            resultSet = ps.executeQuery();
            //获取ResultSet的元数据
            ResultSetMetaData metaData = resultSet.getMetaData();
            //通过ResultSetMetaData获取结果集中的列数
            int columnCount = metaData.getColumnCount();
            if (resultSet.next()) {
                T t = clazz.newInstance();
                //处理结果集一行数据中的每一列
                for (int i = 0; i < columnCount; i++) {
                    //获取列值
                    Object columnValue = resultSet.getObject(i + 1);
                    //获取每个列的别名
                    String columnLabel = metaData.getColumnLabel(i + 1);
                    //通过反射将columnValue 赋值给对象指定的columnName属性
                    Field field = clazz.getDeclaredField(columnLabel);
                    //属性修饰符可能为private，这里设置可以访问
                    field.setAccessible(true);
                    field.set(t, columnValue);
                }
                return t;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(conn, ps, resultSet);
        }
        return null;
    }
}
