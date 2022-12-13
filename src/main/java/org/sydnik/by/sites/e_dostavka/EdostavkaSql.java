package org.sydnik.by.sites.e_dostavka;

import org.sydnik.by.IStopThread;
import org.sydnik.by.sites.e_dostavka.enums.OperationMode;
import org.sydnik.by.sites.e_dostavka.enums.SqlQueries;
import org.sydnik.by.framework.utils.JsonUtil;
import org.sydnik.by.framework.utils.Logger;
import org.sydnik.by.framework.utils.SQLUtil;
import org.sydnik.by.sites.e_dostavka.data.Product;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;


public class EdostavkaSql extends Thread implements IStopThread {
    private boolean work = true;
    private OperationMode operationMode;


    public EdostavkaSql(OperationMode operationMode) {
        this.operationMode = operationMode;
    }

    public void run() {
        switch (operationMode) {
            case SITE_TO_SQL: {
                savePricesFromSiteToSql();
                break;
            }
            case SQL_TO_EXCEL: {
                getPricesFromSqlToExcel();
                break;
            }
            default: {
                Logger.error(this.getClass(), "Dont work with operation mode:" + operationMode.name());
                break;
            }
        }

    }

    public void getPricesFromSqlToExcel(){
        LocalDate localDate = LocalDate.of(2022, 12, 10);
        String stringDate = localDate.format(DateTimeFormatter.ofPattern(JsonUtil.getDataString("dateFormat")));
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = SQLUtil.getConnection().prepareStatement(SqlQueries.PRODUCTS.getGet().replaceAll("PRICE_DATE", stringDate));
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                int id = resultSet.getInt(1);
                int itemCode = resultSet.getInt(2);
                String name = resultSet.getString(3);
                String country = resultSet.getString(4);
                String directory = resultSet.getString(5);
                String subdirectory = resultSet.getString(6);
                boolean food = resultSet.getBoolean(7);
                double price = resultSet.getDouble(8);
                QueueUtil.add(new Product(id, itemCode, name, price , country, subdirectory, directory, localDate, food));
            }
        } catch (SQLException e) {
            Logger.info(this.getClass(), "SQL state:" + e.getSQLState() + e.getMessage() + " code: " + e.getErrorCode());
        } finally {
            SQLUtil.closePreparedStatement(preparedStatement);
            SQLUtil.close();
        }
    }

    private void savePricesFromSiteToSql(){
        try {
            while (work || !QueueUtil.isEmpty()) {
                if (QueueUtil.isEmpty()) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    addProductAndPrice(QueueUtil.take());
                }
            }
        } finally {
            SQLUtil.close();
        }
    }

    public int addProduct(Product product) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = SQLUtil.getConnection().prepareStatement("INSERT INTO products (itemcode, name, country, subdirectory, directory, food) " +
                    "VALUES (?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            int idCountry = getIdFrom(SqlQueries.COUNTRY, product.getCountry());
            int idSub = getIdFrom(SqlQueries.SUBDIRECTORY, product.getSubdirectory());
            int idDir = getIdFrom(SqlQueries.DIRECTORY, product.getDirectory());
            if (idCountry == -1 || idSub == -1 || idDir == -1) {
                Logger.error(this.getClass(), " idCountry: " + idCountry + " idSub: " + idSub + " idDir: " + idDir);
                throw new SQLException();
            }
            preparedStatement.setInt(1, product.getItemCode());
            preparedStatement.setString(2, product.getName());
            preparedStatement.setInt(3, idCountry);
            preparedStatement.setInt(4, idSub);
            preparedStatement.setInt(5, idDir);
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

    public int addValueTo(SqlQueries sqlQueries, String value) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = SQLUtil.getConnection().prepareStatement(sqlQueries.getAdd(), Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, value);
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

    public int getIdFrom(SqlQueries sqlQueries, String value) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = SQLUtil.getConnection().prepareStatement(sqlQueries.getGet());
            preparedStatement.setString(1, value);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }

        } catch (SQLException e) {
            Logger.error(this.getClass(), value + " didnt get id from the sql base\n" +
                    "SQL state:" + e.getSQLState() + e.getMessage() + " code: " + e.getErrorCode());
        } finally {
            SQLUtil.closePreparedStatement(preparedStatement);
            SQLUtil.closeResultSet(resultSet);
        }
        return addValueTo(sqlQueries, value);
    }

    public int getProductId(Product product) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = SQLUtil.getConnection().prepareStatement("SELECT id FROM products WHERE name = ? AND itemcode = ?");
            preparedStatement.setString(1, product.getName());
            preparedStatement.setInt(2, product.getItemCode());
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
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

    public void addPrice(int id, Product product) {
        String stringDate = product.getDate().format(DateTimeFormatter.ofPattern(JsonUtil.getDataString("dateFormat")));
        PreparedStatement preparedStatement = null;
        try {
            if (id == -1) {
                throw new SQLException();
            }
            preparedStatement = SQLUtil.getConnection().prepareStatement("INSERT prices SET `idProduct`= ?, `PRICE_DATE`= ? ON DUPLICATE KEY UPDATE `PRICE_DATE` = ?;"
                    .replaceAll("PRICE_DATE", stringDate));
            preparedStatement.setInt(1, id);
            preparedStatement.setDouble(2, product.getPrice());
            preparedStatement.setDouble(3, product.getPrice());
            preparedStatement.execute();
        } catch (SQLException e) {
            if (e.getErrorCode() == 1054) {
                addColumnInPrices(stringDate);
                addProductAndPrice(product);
            }
            Logger.info(this.getClass(), "SQL state:" + e.getSQLState() + e.getMessage() + " code: " + e.getErrorCode());
        } finally {
            SQLUtil.closePreparedStatement(preparedStatement);
        }
    }

    public void addProductAndPrice(Product product) {
        int id = getProductId(product);
        if (id == -1) {
            id = addProduct(product);
        }
        addPrice(id, product);
    }

    public void addColumnInPrices(String priceDate) {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = SQLUtil.getConnection().prepareStatement("alter table prices add `PRICE_DATE` double null;"
                    .replace("PRICE_DATE", priceDate));
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            Logger.error(this.getClass(), " didnt get id from the sql base\n" +
                    "SQL state:" + e.getSQLState() + e.getMessage() + " code: " + e.getErrorCode());
        } finally {
            SQLUtil.closePreparedStatement(preparedStatement);
        }
    }

    @Override
    public void stopThead() {
        work = false;
    }
}
