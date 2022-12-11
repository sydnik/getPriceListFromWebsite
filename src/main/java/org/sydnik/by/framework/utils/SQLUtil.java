package org.sydnik.by.framework.utils;

import com.fasterxml.jackson.databind.ext.SqlBlobSerializer;

import java.sql.*;
import java.util.logging.Logger;

public class SQLUtil {
    private static SQLUtil sqlUtil;
    private final String URL = "jdbc:mysql://localhost:3310/Mydb";
    private final String USERNAME = "root";
    private final String PASSWORD = "root";
    private Connection connection;

    private SQLUtil(){
        try {
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            Logger.getLogger("SQLBase").warning("Didn't connect " + "data.getValue(baseUrl).toString()" + "\n" + e.getSQLState());
            e.printStackTrace();
        }
    }

    public static SQLUtil getInstance(){
        if(sqlUtil == null){
            return new SQLUtil();
        }
        return sqlUtil;
    }

    public static void close(){
        SQLUtil sqlUtil = getInstance();
        try {
            if (sqlUtil.connection != null && !sqlUtil.connection.isClosed()) {
                sqlUtil.connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        SQLUtil sqlUtil = getInstance();
        try {
            if(sqlUtil.connection==null || sqlUtil.connection.isClosed()){

                sqlUtil.connection = DriverManager.getConnection(sqlUtil.URL, sqlUtil.USERNAME, sqlUtil.PASSWORD);
                return sqlUtil.connection;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();

        }
        return sqlUtil.connection;
    }

    public static void closePreparedStatement(PreparedStatement pr){
        try {
            pr.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void closeResultSet(ResultSet resultSet){
        try {
            resultSet.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

}
