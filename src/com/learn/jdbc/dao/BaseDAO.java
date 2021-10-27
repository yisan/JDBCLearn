package com.learn.jdbc.dao;

import com.learn.jdbc.utils.JDBCUtils;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 封装了基本的数据表的通用操作
 */
public class BaseDAO {

    /**
     * 针对不同的表的通用的查询操作，返回表中的一条记录  -- v1.0 -- 考虑事务
     *
     * @param clazz
     * @param sql
     * @param args
     * @param <T>
     * @return
     */
    public static  <T> T getInstance(Connection conn, Class<T> clazz, String sql, Object... args) {
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
            JDBCUtils.close(null, ps, resultSet);
        }
        return null;
    }

    /**
     * 用于查询特殊值的通用方法
     */
    public <E> E getValue(Connection conn,String sql,Object ...args){
        PreparedStatement ps = null;
        ResultSet rs  = null;
        try {
            ps = conn.prepareStatement(sql);
            for (int i=0;i<args.length;i++){
                ps.setObject(i+1,args[i]);
            }
            rs = ps.executeQuery();
            if (rs.next()){
                return  (E) rs.getObject(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            JDBCUtils.close(null,ps,rs);
        }
        return null;
    }

    public <T> List<T> getForList(Connection conn,Class<T> clazz, String sql, Object... args) {
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
            ArrayList<T> list = new ArrayList<>();
            while (resultSet.next()) {
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
                list.add(t);
            }
            return list;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.close(null, ps, resultSet);
        }
        return null;
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
}
