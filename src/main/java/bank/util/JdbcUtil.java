package bank.util;

import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JdbcUtil {
    private Connection connection;
    private PreparedStatement pps;
    private ResultSet resultSet;
    private final static String USERNAME = "root";
    private final static String PASSWORD = "12345678";
    private final static String JDBC_URL = "jdbc:mysql://127.0.0.1:3306/bank?serverTimezone=UTC";

    // 只加载一次驱动
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取 jdbc 链接
     *
     * @return 当前对象的 Connection 对象
     * @throws SQLException sql异常
     */
    public Connection getConnection() throws SQLException {
        this.connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
        return this.connection;
    }


    /**
     * 对 sql 语句进行赋值处理
     *
     * @param sql  要执行的 sql
     * @param list 要填充的字符按
     * @throws SQLException sql 异常
     */
    private PreparedStatement getExecutePps(String sql, List<?> list) throws SQLException {
        this.pps = getConnection().prepareStatement(sql);
        if (!list.isEmpty()) {
            for (int item = 0; item < list.size(); item++) {
                this.pps.setObject(item + 1, list.get(item));
            }
        }
        return this.pps;
    }
    public Boolean executeSql(String sql, List<?> list) {
        int row = 0;
        try {
            row = getExecutePps(sql, list).executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return row > 0;
    }

    private PreparedStatement getInsertExecutePps(String sql, List<?> list) throws SQLException {
        this.pps = getConnection().prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
        if (!list.isEmpty()) {
            for (int item = 0; item < list.size(); item++) {
                this.pps.setObject(item + 1, list.get(item));
            }
        }
        return this.pps;
    }

    /**
     * 增删改使用
     *
     * @param sql  sql语句
     * @param list 填充字段
     * @return 是否成功
     */
    public Long insertExecuteSql(String sql, List<?> list) {
        Long row = (long)-1;
        try {
            PreparedStatement executePps = getInsertExecutePps(sql, list);
            executePps.executeUpdate();
            ResultSet generatedKeys = executePps.getGeneratedKeys();
            while (generatedKeys.next()) {
                row=generatedKeys.getLong(1);//返回指定列的值
                System.out.println("自增id="+row);
            }
//            row = getExecutePps(sql, list).executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return row;
    }


    /**
     *  条件查询使用
     * @param sql sql语句
     * @param list 填充的字段
     * @param map resultSet
     * @param tClass 返回类型
     * @param <T> 泛型
     * @return 查询结果
     */
    public <T> List<T> query(String sql, List<?> list, Map<String,List<?>> map, Class<T> tClass) {
        try {
            this.resultSet = getExecutePps(sql, list).executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return getList(this.resultSet,map,tClass);
    }

    /**
     * 无条件时使用
     * @param sql sql语句
     * @param map resultSet
     * @param tClass 返回类型
     * @param <T> 泛型
     * @return 查询结果
     */
    public <T> List<T> query(String sql,Map<String,List<?>> map, Class<T> tClass) {
        try {
            this.pps = getConnection().prepareStatement(sql);
            this.resultSet = this.pps.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return getList(this.resultSet,map,tClass);
    }

    /**
     * 在 resultSet 中获取相应参数
     *
     * @param resultSet resultSet
     * @param map       取出参数 {"sqlColName": ["setMethodName", Class]}  示例 {数据库字段名 :  [对应的set方法名, java属性类型 ] }
     * @param tClass    要返回对象的 class
     * @param <T>       泛型
     * @return 结果集取出后以 List 形式返回
     */
    public static <T> List<T> getList(ResultSet resultSet, Map<String,List<?>> map, Class<T> tClass) {
        List<T> tList = new ArrayList<>();
        while (true) {
            try {
                if (!resultSet.next()) {
                    break;
                }
                T t = tClass.cast(tClass.getConstructor().newInstance());
                for (String col : map.keySet()) {
                    List<?> objects = map.get(col);
                    tClass.getMethod((String) objects.get(0), (Class<?>) objects.get(1)).invoke(t,resultSet.getObject(col));
                }
                tList.add(t);
            } catch (SQLException | InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return tList;
    }


    public void closeAll() {
        try {
            if (this.resultSet != null) {
                this.resultSet.close();
            }
            if (this.pps != null) {
                this.pps.close();
            }
            if (this.connection != null) {
                this.connection.close();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}