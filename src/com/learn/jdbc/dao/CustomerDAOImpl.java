package com.learn.jdbc.dao;

import com.learn.jdbc.bean.Customer;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;

public class CustomerDAOImpl extends BaseDAO implements CustomerDAO {
    @Override
    public void insert(Connection conn, Customer customer) {
        String sql = "insert into customers(name,email,birth)values(?,?,?)";
        update(conn, sql, customer.getName(), customer.getEmail(), customer.getBirth());
    }

    @Override
    public void deleteById(Connection conn, int id) {
        String sql = "delete from customers where id = ?";
        update(conn, sql, id);
    }

    @Override
    public void update(Connection conn, Customer customer) {
        String sql = "update customers set name = ?,email = ?,birth = ? where id = ?";
        update(conn, sql, customer.getName(), customer.getEmail(), customer.getBirth(), customer.getId());
    }

    @Override
    public Customer getCustomerById(Connection conn, int id) {
        String sql = "select id,name,email,birth from customers where id = ?";
        return getInstance(conn, Customer.class, sql, id);
    }

    @Override
    public List<Customer> getAll(Connection conn) {
        String sql = "select id,name,email,birth from customers";
        return  getForList(conn, Customer.class, sql);
    }

    @Override
    public long getCount(Connection conn) {

        String sql = "select count(*) from customers";
        return getValue(conn, sql);
    }

    @Override
    public Date getMaxBirth(Connection conn) {
        String sql = "select max(birth) from customers";
       return getValue(conn,sql);
    }
}
