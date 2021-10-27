package com.learn.jdbc.transaction;

import com.learn.jdbc.utils.JDBCUtils;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;


public class TransactionTest {
    /**
     * 针对于用户表user_table来说
     * 用户AA向BB转账100
     *
     * update user_table set balance = balance - 100 where user = 'AA';
     * update user_table set balance = balance + 100 where user = 'BB';
     *
     */
    @Test
    public void testUpdate(){
        String sql1 = "update user_table set balance = balance - 100 where user = ?";
        String sql2 = "update user_table set balance = balance + 100 where user = ?";
        JDBCUtils.update(sql1,"AA");
        JDBCUtils.update(sql2,"BB");
        System.out.println("转账成功");
    }
    @Test
    public void testUpdateWithTransaction(){
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();
            // 取消数据的自动提交
            conn.setAutoCommit(false);
            String sql1 = "update user_table set balance = balance - 100 where user = ?";
            JDBCUtils.update(conn,sql1,"AA");
            // 模拟网络异常
            System.out.println(10/0);
            String sql2 = "update user_table set balance = balance + 100 where user = ?";
            JDBCUtils.update(conn,sql2,"BB");

            System.out.println("转账成功");
            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
            // 回滚数据
            try {
                if (conn!=null){
                    conn.rollback();
                }
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }finally {
            // 恢复默认设置
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                }
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            JDBCUtils.close(conn,null);
        }

    }
}
