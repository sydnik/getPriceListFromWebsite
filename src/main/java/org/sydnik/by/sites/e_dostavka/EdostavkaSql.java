package org.sydnik.by.sites.e_dostavka;

import org.sydnik.by.enums.SqlQueries;
import org.sydnik.by.framework.utils.JsonUtil;
import org.sydnik.by.framework.utils.Logger;
import org.sydnik.by.framework.utils.SQLUtil;
import org.sydnik.by.sites.e_dostavka.data.Product;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ArrayBlockingQueue;

public class EdostavkaSql extends Thread {
    private ArrayBlockingQueue<Product> queue;
    private boolean work = true;

    public EdostavkaSql(ArrayBlockingQueue<Product> queue) {
        this.queue = queue;
    }

    public void run(){
        try {
            while (work || !queue.isEmpty()) {
                if (queue.isEmpty()) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        addProductAndPrice(queue.take());
                    } catch (InterruptedException e) {
                        Logger.error(this.getClass(), e.getMessage());
                    }
                }
            }
        } finally {
            SQLUtil.close();
        }

    }
    public int addProduct(Product product){
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = SQLUtil.getConnection().prepareStatement("INSERT INTO products (itemcode, name, country, subdirectory, directory, food) " +
                    "VALUES (?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setInt( 1, product.getItemCode());
            preparedStatement.setString(2, product.getName());
            preparedStatement.setInt(3, getIdFromSoloTable(SqlQueries.COUNTRY,product.getCountry()));
            preparedStatement.setInt(4, getIdFromSoloTable(SqlQueries.SUBDIRECTORY,product.getSubdirectory()));
            preparedStatement.setInt(5, getIdFromSoloTable(SqlQueries.DIRECTORY,product.getDirectory()));
            preparedStatement.setBoolean(6, product.isFood());
            preparedStatement.executeUpdate();
            resultSet = preparedStatement.getGeneratedKeys();
            resultSet.next();
            return resultSet.getInt(1);
        } catch (SQLException e) {
            Logger.error(this.getClass(), product + " didnt write to the sqlbase\n" +
                    "SQL state:" + e.getSQLState() + e.getMessage() + " code: " + e.getErrorCode());
        } finally {
            SQLUtil.closePreparedStatement(preparedStatement);
            SQLUtil.closeResultSet(resultSet);
        }
        return -1;
    }

    public int addValueToSoloTable(SqlQueries sqlQueries, String value) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = SQLUtil.getConnection().prepareStatement(sqlQueries.getAdd(), Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString( 1, value);
            preparedStatement.executeUpdate();
            resultSet = preparedStatement.getGeneratedKeys();
            resultSet.next();
            return resultSet.getInt(1);
        } catch (SQLException e) {
            Logger.error(this.getClass(), value + " didnt get id from the sql base\n" +
                    "SQL state:" + e.getSQLState() + e.getMessage() + " code: " + e.getErrorCode());
        } finally {
            SQLUtil.closePreparedStatement(preparedStatement);
            SQLUtil.closeResultSet(resultSet);
        }
        return -1;
    }

    public int getIdFromSoloTable(SqlQueries sqlQueries, String value){
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = SQLUtil.getConnection().prepareStatement(sqlQueries.getGet());
            preparedStatement.setString(1, value);
            resultSet = preparedStatement.executeQuery();
        if (resultSet.next()){
            return resultSet.getInt(1);
        }

        } catch (SQLException e) {
            Logger.error(this.getClass(), value + " didnt get id from the sql base\n" +
                    "SQL state:" + e.getSQLState() + e.getMessage() + " code: " + e.getErrorCode());
        } finally {
            SQLUtil.closePreparedStatement(preparedStatement);
            SQLUtil.closeResultSet(resultSet);
        }
        return addValueToSoloTable(sqlQueries, value);
    }

    public int getProductId(Product product){
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = SQLUtil.getConnection().prepareStatement("SELECT id FROM products WHERE name = ? AND itemcode = ?");
            preparedStatement.setString(1, product.getName());
            preparedStatement.setInt(2, product.getItemCode());
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                return resultSet.getInt(1);
            }

        } catch (SQLException e) {
            Logger.error(this.getClass(), product + " didnt get id from the sql base\n" +
                    "SQL state:" + e.getSQLState() + e.getMessage() + " code: " + e.getErrorCode());
        } finally {
            SQLUtil.closePreparedStatement(preparedStatement);
            SQLUtil.closeResultSet(resultSet);
        }
        return -1;
    }

    public void addPrice(int id, Product product){
        String stringDate = product.getDate().format(DateTimeFormatter.ofPattern(JsonUtil.getDataString("dateFormat")));
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = SQLUtil.getConnection().prepareStatement("INSERT prices SET `idProduct`= ?, `PRICE_DATE`= ? ON DUPLICATE KEY UPDATE `PRICE_DATE` = ?;"
                    .replaceAll("PRICE_DATE", stringDate));
            preparedStatement.setInt(1, id);
            preparedStatement.setDouble(2, product.getPrice());
            preparedStatement.setDouble(3, product.getPrice());
            preparedStatement.execute();
        } catch (SQLException e) {
            if(e.getErrorCode()==1054){
                addColumnInPrices(stringDate);
                addProductAndPrice(product);
            }
            Logger.info(this.getClass(), "SQL state:" + e.getSQLState() + e.getMessage() + " code: " + e.getErrorCode());
        } finally {
            SQLUtil.closePreparedStatement(preparedStatement);
        }
    }

    public void addProductAndPrice(Product product){
        int id = getProductId(product);
        if(id==-1){
            id = addProduct(product);
        }
        addPrice(id, product);
    }

    public void addColumnInPrices(String priceDate){
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = SQLUtil.getConnection().prepareStatement("alter table prices add `PRICE_DATE` double null;"
                    .replace("PRICE_DATE", priceDate));
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            Logger.error(this.getClass(),   " didnt get id from the sql base\n" +
                    "SQL state:" + e.getSQLState() + e.getMessage() + " code: " + e.getErrorCode());
        } finally {
            SQLUtil.closePreparedStatement(preparedStatement);
        }
    }

    public void setWork(boolean work) {
        this.work = work;
    }
}
