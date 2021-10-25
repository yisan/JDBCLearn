package com.learn.jdbc.preparedstatement.crud;

import com.learn.jdbc.bean.Customer;
import com.learn.jdbc.utils.JDBCUtils;
import org.junit.Test;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * 针对Customer表的查询操作
 */
public class CustomerForQuery {

    @Test
    public void testQuery1() {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        try {
            conn = JDBCUtils.getConnection();
            String sql = "SELECT id,name,email,birth FROM customers WHERE id = ?";
            ps = conn.prepareStatement(sql);
            ps.setObject(1,1);
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
