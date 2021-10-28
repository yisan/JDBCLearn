package com.learn.jdbc.dao;

import com.learn.jdbc.bean.Customer;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;

/**
 * 定义Customers表的常用操作的接口
 */
public interface CustomerDAO {
    /**
     * 将Customer对象添加到数据库中
     * @param conn
     * @param customer
     */
    void insert(Connection conn, Customer customer );

    /**
     * 根据指定的id删除表中的一条记录
     * @param conn
     * @param id
     */
    void deleteById(Connection conn,int id);

    /**
     * 根据内存中的Customer对象，修改数据表中的记录
     * @param conn
     */
    void update(Connection conn,Customer customer);

    /**
     * 根据指定的id查询得到对应的Customer对象
     * @param conn
     * @param id
     */
    Customer getCustomerById(Connection conn,int id);

    /**
     * 查询表中的所有记录
     * @param conn
     * @return
     */
    List<Customer> getAll(Connection conn);

    /**
     * 返回数据表中的条目数
     * @param conn
     * @return
     */
    long getCount(Connection conn);

    /**
     * 获取表中的最大生日日期
     * @param conn
     * @return
     */
    Date getMaxBirth(Connection conn);

}
