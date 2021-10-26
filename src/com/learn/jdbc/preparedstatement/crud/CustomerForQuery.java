package com.learn.jdbc.preparedstatement.crud;

import com.learn.jdbc.bean.Customer;
import com.learn.jdbc.utils.JDBCUtils;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.*;

/**
 * 针对Customer表的查询操作
 */
public class CustomerForQuery {
    @Test
    public void testQueryForCustomer(){
        String sql = "select id,name,birth,email from customers where id = ?";
        Customer customer = queryForCustomer(sql, 13);
        System.out.println(customer);
        String sql1 = "select name,email from customers where id = ?";
        Customer customer1 = queryForCustomer(sql1, 12);
        System.out.println(customer1);
    }

    public Customer queryForCustomer(String sql,Object ...args) {
        ResultSet resultSet = null;
        PreparedStatement ps = null;
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();
            ps = conn.prepareStatement(sql);
            for (int i=0;i<args.length;i++){
                ps.setObject(i+1,args[i]);
            }
            resultSet = ps.executeQuery();
            //获取ResultSet的元数据
            ResultSetMetaData metaData = resultSet.getMetaData();
            //通过ResultSetMetaData获取结果集中的列数
            int columnCount = metaData.getColumnCount();
            System.out.println("columnCount: "+columnCount);
            if (resultSet.next()) {
                Customer customer = new Customer();
                //处理结果集一行数据中的每一列
                for (int i = 0; i < columnCount; i++) {
                    //获取列值
                    Object columnValue = resultSet.getObject(i + 1);
                    //获取每个列的别名
                    String columnLabel = metaData.getColumnLabel(i + 1);
                    //通过反射将columnValue 赋值给对象指定的columnName属性
                    Field field = Customer.class.getDeclaredField(columnLabel);
                    //属性修饰符可能为private，这里设置可以访问
                    field.setAccessible(true);
                    field.set(customer, columnValue);
                }
                return customer;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.close(conn, ps, resultSet);
        }
        return null;

    }

    @Test
    public void testQuery1() {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        try {
            conn = JDBCUtils.getConnection();
            String sql = "SELECT id,name,email,birth FROM customers WHERE id = ?";
            ps = conn.prepareStatement(sql);
            ps.setObject(1, 1);
            //执行，获取结果集
            resultSet = ps.executeQuery();
            //处理结果集  next()：判断结果集的下一条是否有数据，如果有数据返回true,并指针下移。注意：ResultSet游标最初位于第一行之前。
            if (resultSet.next()) {
                //获取当前这条数据的各个字段值
                int id = resultSet.getInt(1);
                String name = resultSet.getString(2);
                String email = resultSet.getString(3);
                Date birth = resultSet.getDate(4);
                //将数据封装为一个对象
                Customer customer = new Customer(id, name, email, birth);
                System.out.println(customer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.close(conn, ps, resultSet);
        }

    }
}
