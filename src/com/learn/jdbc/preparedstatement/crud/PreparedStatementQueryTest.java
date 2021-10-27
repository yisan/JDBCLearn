package com.learn.jdbc.preparedstatement.crud;

import com.learn.jdbc.bean.Customer;
import com.learn.jdbc.bean.Order;
import com.learn.jdbc.statement.curd.User;
import com.learn.jdbc.utils.JDBCUtils;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PreparedStatementQueryTest {
    @Test
    public void getForList(){
        String sql = "select order_id orderId,order_name orderName,order_date orderDate from `order` where order_id < ?";
        List<Order> orderList = getForList(Order.class, sql, 5);
        orderList.forEach(System.out::println);
        String sql1 = "select id,name,email from customers where id < ?";
        List<Customer> customerList = getForList(Customer.class, sql1, 12);
        customerList.forEach(System.out::println);
    }
    // PreparedStatement方式
    @Test
    public void testLogin() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入用户名:");
        String user = scanner.next();
        System.out.println("请输入密码:");
        String password = scanner.next();
        String sql = "SELECT user,password FROM user_table WHERE user= ? AND password = ?";
        User user1 = JDBCUtils.getInstance(User.class,sql,user,password);
        if (user1!=null){
            System.out.println("登录成功");
        }else{
            System.out.println("用户名不存在或密码错误");
        }
    }
    public <T> List<T> getForList(Class<T> clazz, String sql, Object... args) {
        ResultSet resultSet = null;
        PreparedStatement ps = null;
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();
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
            JDBCUtils.close(conn, ps, resultSet);
        }
        return null;
    }

    @Test
    public void testGetInstance() {
        String sql = "select id,name,email from customers where id = ?";
        Customer customer = JDBCUtils.getInstance(Customer.class, sql, 2);
        System.out.println(customer);
        String sql1 = "select order_id orderId,order_name orderName,order_date orderDate from `order` where order_id = ?";
        Order order = JDBCUtils.getInstance(Order.class, sql1, 2);
        System.out.println(order);
    }


}
