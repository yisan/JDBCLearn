package com.learn.jdbc.junit;

import com.learn.jdbc.bean.Customer;
import com.learn.jdbc.dao.CustomerDAOImpl;
import com.learn.jdbc.utils.JDBCUtils;
import org.junit.Test;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;

import static org.junit.Assert.*;

public class CustomerDAOImplTest {
    private final CustomerDAOImpl dao = new CustomerDAOImpl();
    @Test
    public void insert() {
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();
            Customer customer = new Customer(1, "孙尚香", "sunshangxaing@qq.com", new Date(43323234432L));
            dao.insert(conn,customer);
            System.out.println("添加成功");
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            JDBCUtils.close(conn,null);
        }
    }

    @Test
    public void deleteById() {
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();
            dao.deleteById(conn,12);
            System.out.println("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            JDBCUtils.close(conn,null);
        }
    }

    @Test
    public void update() {
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();
            Customer customer = new Customer(18, "郎朗", "langlang@qq.com", new Date(302029303L));
            dao.update(conn,customer);
            System.out.println("更新成功");
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            JDBCUtils.close(conn,null);
        }
    }

    @Test
    public void getCustomerById() {
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();
            Customer customer = dao.getCustomerById(conn, 19);
            System.out.println(customer);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            JDBCUtils.close(conn,null);
        }
    }

    @Test
    public void getAll() {
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();
            List<Customer> all = dao.getAll(conn);
            all.forEach(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            JDBCUtils.close(conn,null);
        }
    }

    @Test
    public void getCount() {
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();
            long count = dao.getCount(conn);
            System.out.println("count: "+count);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            JDBCUtils.close(conn,null);
        }
    }

    @Test
    public void getMaxBirth() {
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();
            Date maxBirth = dao.getMaxBirth(conn);
            System.out.println(maxBirth.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            JDBCUtils.close(conn,null);
        }
    }
}