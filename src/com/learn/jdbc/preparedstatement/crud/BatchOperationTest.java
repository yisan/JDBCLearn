package com.learn.jdbc.preparedstatement.crud;

import com.learn.jdbc.utils.JDBCUtils;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * PreparedStatement批量操作
 * <p>
 * update , delete 本身就具有批量操作的效果
 * 此时的批量操作，主要指的是批量插入。
 */
public class BatchOperationTest {
    @Test
    public void testInsert1() {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = JDBCUtils.getConnection();
            String sql = "insert into goods(name)values(?)";
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < 20000; i++) {
                ps.setObject(1, "name_" + i);
                ps.execute();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.close(conn, ps);
        }
    }

    /**批量插入的方式三
     * 1.addBatch() , executeBatch(),clearBatch()
     * 2.mysql服务器默认是关闭批处理的，需要通过一个参数开启
     * ？rewriteBatchedStatements=true 写在配置文件的url后面
     */
    @Test
    public void testInsert2() {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = JDBCUtils.getConnection();
            String sql = "insert into goods(name)values(?)";
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < 1000000; i++) {
                ps.setObject(1, "name_" + i);
                //1."攒"sql
                ps.addBatch();
                if (i % 500 == 0) {
                    //2.执行batch
                    ps.executeBatch();
                    //3.清空batch
                    ps.clearBatch();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.close(conn, ps);
        }
    }

    /**
     * 方式四
     * 设置关闭自动提交，最后统一提交
     *
     */
    @Test
    public void testInsert3() {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = JDBCUtils.getConnection();
            conn.setAutoCommit(false);
            String sql = "insert into goods(name)values(?)";
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < 1000000; i++) {
                ps.setObject(1, "name_" + i);
                //1."攒"sql
                ps.addBatch();
                if (i % 500 == 0) {
                    //2.执行batch
                    ps.executeBatch();
                    //3.清空batch
                    ps.clearBatch();
                }
            }
            //提交数据
            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.close(conn, ps);
        }
    }
}
