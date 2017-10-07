package src.StockTransactions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;
import java.util.Scanner;
import java.util.TreeMap;

import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.date.SerialDate;

public class StockTransactions {

    float currentValue = 0;

    Connection conn;
    /* The port of the MySQL server (default is 3306) */
    private final int portNumber = 3306;
    /*
     * The name of the database we are testing with (this default is installed
     * with MySQL)
     */
    private final String dbName = "stocktransactions";

    /* The name of the computer running MySQL */
    private final String serverName = "localhost";

    StockTransactions() {
        try {
            this.conn = this.getConnection();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public Connection getConnection() throws SQLException {
        Connection conn = null;
        Properties connectionProps = new Properties();
        connectionProps.put("user", "root");
        connectionProps.put("password", "root");
        conn = DriverManager.getConnection(
                "jdbc:mysql://" + this.serverName + ":" + this.portNumber + "/" + this.dbName, connectionProps);
        return conn;
    }

    public void addPortfolio(int customerId) {
        try {
            CallableStatement call = conn.prepareCall("CALL addPortfolio('" + customerId + "');");
            call.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> getCustomers() {
        ArrayList<String> customerNames = new ArrayList();
        try {
            CallableStatement call = conn.prepareCall("CALL getCustomers();");
            ResultSet result = call.executeQuery();
            // Adds the results to an ArrayList
            while (result.next()) {
                String charName = result.getString(1);
                customerNames.add(charName);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return customerNames;
    }

    public String[][] getTransactionData(int portfolio) {
        String[][] out = null;
        try {
            CallableStatement call = conn.prepareCall("CALL getNumTransactinos(" + portfolio + ");");
            CallableStatement call2 = conn.prepareCall("CALL getTransactions(" + portfolio + ");");
            Statement stmt = conn.createStatement();
            ResultSet result = call2.executeQuery();
            Statement numstmt = conn.createStatement();
            ResultSet num = call.executeQuery();
            int i = 0;
            // Adds the results to an ArrayList
            while (num.next()) {
                i = num.getInt("num");
            }
            out = new String[i][];
            int curr = 0;
            while (result.next()) {
                String[] currElement = new String[5];
                currElement[0] = result.getString("transaction_date");
                currElement[1] = result.getString("stock_symbol");
                currElement[2] = Float.toString(result.getFloat("open_price_at_transaction"));
                currElement[3] = result.getString("transaction_type");
                currElement[4] = Integer.toString(result.getInt("transaction_volume"));
                out[curr] = currElement;
                curr++;
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return out;
    }

    public class Transaction {
        String date;
        int volume;
        String type;
        String stockTik;

        Transaction(String date, int volume, String type, String stockTik) {
            this.date = date;
            this.volume = volume;
            this.type = type;
            this.stockTik = stockTik;
        }

    }

    public XYDataset getCustData(int portfolio) {

        HashMap<String, ArrayList<Transaction>> transactions = new HashMap<String, ArrayList<Transaction>>();

        HashMap<String, HashMap<String, Float>> dateToPrice = new HashMap<String, HashMap<String, Float>>();

        HashMap<String, Integer> holdings = new HashMap<String, Integer>();

        TreeMap<String, ArrayList<String>> datesToStocks = new TreeMap<String, ArrayList<String>>();
        
        ArrayList<String> dates = new ArrayList<String>();

        String firstDate = "";

        try {
            CallableStatement call2 = conn.prepareCall("CALL getTransactions(" + portfolio + ");");
            ResultSet result = call2.executeQuery();
            if (result.next()) {
                firstDate = result.getString("transaction_date");
            }
            result.beforeFirst();
            // Adds the results to an ArrayList
            while (result.next()) {
                String date = result.getString("transaction_date");
                int volume = result.getInt("transaction_volume");
                String type = result.getString("transaction_type");
                String stockTik = result.getString("stock_symbol");
                Transaction t = new Transaction(date, volume, type, stockTik);
                ArrayList<Transaction> currDateTrans = transactions.getOrDefault(date, new ArrayList<Transaction>());
                currDateTrans.add(t);
                transactions.put(date, currDateTrans);
                dateToPrice.putIfAbsent(stockTik, new HashMap<String, Float>());
                holdings.putIfAbsent(stockTik, 0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (String stock : dateToPrice.keySet()) {
            HashMap<String, Float> currPrices = new HashMap<String, Float>();

            try {

                CallableStatement call = conn.prepareCall("CALL getStockDat('" + firstDate + "' , '" + stock + "');");
                ResultSet result = call.executeQuery();
                while (result.next()) {
                    String date = result.getString("ccurrent_date");
                    Float price = result.getFloat("current_price");
                    currPrices.put(date, price);
                    ArrayList<String> daysStocks = datesToStocks.getOrDefault(date, new ArrayList<String>());
                    if (!daysStocks.contains(stock)) {
                        daysStocks.add(stock);
                    }
                    datesToStocks.put(date, daysStocks);
                }
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            dateToPrice.put(stock, currPrices);
        }

        TimeSeries total = new TimeSeries("Total Holdings");
        SimpleDateFormat standardDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Float dayValue = (float) 0;
        for (String day : datesToStocks.keySet()) {
            Day myDay = null;
            dayValue = (float) 0;
            for (String stock : holdings.keySet()) {
                if (datesToStocks.get(day).contains(stock)) {
                    if (transactions.containsKey(day)) {
                        ArrayList<Transaction> thisDayTransaction = transactions.get(day);
                        for (Transaction t : thisDayTransaction) {
                            int currhold = holdings.get(t.stockTik);
                            switch (t.type) {
                            case "buy":
                                currhold = currhold + t.volume;
                                break;
                            case "sell":
                                currhold = currhold - t.volume;
                                break;
                            }
                            holdings.put(t.stockTik, currhold);
                        }
                    }
                    Date myDate = null;

                    try {
                        myDate = standardDateFormat.parse(day);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    myDay = new Day(myDate);
                    int volume = holdings.get(stock);
                    if (volume > 0) {
                        float price = dateToPrice.get(stock).get(day);
                        float stockValue = (price * volume);
                        dayValue = dayValue + stockValue;
                    }
                }
            }
            if (dayValue == 0){
                System.out.println(myDay + " " + dayValue);
            }
            total.add(myDay, dayValue);

        }
        
        this.currentValue = dayValue;
        TimeSeriesCollection out = new TimeSeriesCollection(total);

        return out;
    }
    
    public void deletePortfolio(int id){
        try {
            CallableStatement call = conn.prepareCall("CALL deletePortfolio('" + id + "');");
            call.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    
    public void updateTransaction(String stockTik,int  portfolioId,String  date,int volume, int newVolume){
        try {
            CallableStatement call = conn.prepareCall("CALL editTransaction('"+ stockTik + "', " + portfolioId +", '" + date + "'," + volume + ","+ newVolume + ");");
            call.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addCustomer(String cname) {
        try {
            CallableStatement call = conn.prepareCall("CALL addCustomer('" + cname + "');");
            call.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteCustomer(String cname) {
        try {
            CallableStatement call = conn.prepareCall("CALL removeCustomer('" + cname + "');");
            call.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> getStocks() {
        ArrayList<String> out = new ArrayList<String>();
        try {
            CallableStatement call = conn.prepareCall("CALL getStocks();");
            ResultSet result = call.executeQuery();
            while (result.next()) {
                out.add(result.getString("stock_symbol"));
                System.out.println(result.getString("stock_symbol"));
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }

        return out;
    }

    public void sellStock(String stockTik, int portfolioId, String date, int volume) {
        try {
            CallableStatement call = conn.prepareCall("CALL sell('" + stockTik + "'," + Integer.toString(portfolioId)
                    + ",'" + date + "'," + volume + ");");
            call.executeQuery();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public int getCustId(String name) {
        int i = 0;
        try {
            CallableStatement stmt = conn.prepareCall("CALL getCustId('" + name + "');");
            ResultSet result = stmt.executeQuery();
            // Adds the results to an ArrayList
            while (result.next()) {
                i = result.getInt("customer_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return i;
    }

    public ArrayList<Integer> getPortfolios(String customerName) {
        ArrayList<Integer> portfolios = new ArrayList<Integer>();
        try {
            CallableStatement stmt = conn.prepareCall("CALL getCustPortfolios('" + customerName + "');");
            ResultSet result = stmt.executeQuery();
            // Adds the results to an ArrayList
            while (result.next()) {
                portfolios.add(result.getInt("portfolio_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return portfolios;
    }

    public void buyStock(String stockTik, int portfolioId, String date, int volume) {
        try {
            CallableStatement call = conn.prepareCall("CALL buy('" + stockTik + "'," + Integer.toString(portfolioId)
                    + ",'" + date + "'," + volume + ");");
            call.executeQuery();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
