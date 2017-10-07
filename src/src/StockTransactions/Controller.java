package src.StockTransactions;

import java.util.ArrayList;

import org.jfree.data.xy.XYDataset;

public class Controller implements IActionListener{
    
    StockTransactions model;
    view view;
    
    public Controller(StockTransactions stocks, view view){
        this.model = stocks;
        this.view = view;
        this.view.setListeners(this);
        this.view.display();
    }

    @Override
    public void action(String description) {
        if (model.getCustomers().contains(description)){
            ArrayList<Integer> custPortfolios = model.getPortfolios(description);
            view.setPortfolioView(custPortfolios, description);
            view.currCustomerId = model.getCustId(description);
        }else if (description.startsWith("Portfolio #")){
            String[] port = description.split(" ");
            int portfolioId = Integer.parseInt(port[2]);
            view.currPortfolio = portfolioId;
            System.out.println(portfolioId);
            XYDataset data = model.getCustData(portfolioId);
            view.setChart(data, view.currCustomer);
            String[][] transactionData = model.getTransactionData(portfolioId);
            view.transactionView(transactionData);
            ArrayList<String> stocks = model.getStocks();
            view.setNewTransactionPane(stocks, model.currentValue);
        } else if (description.equals("back")){
            view.setCustomerView();
            view.south.removeAll();
            view.south.repaint();
            view.east.removeAll();
            view.chartPane.removeAll();
            view.main.pack();
        } else if (description.equals("add new portfolio")){
            model.addPortfolio(view.currCustomerId);
            ArrayList<Integer> custPortfolios = model.getPortfolios(view.currCustomer);
            view.setPortfolioView(custPortfolios, view.currCustomer);
        } else if (description.equals("add transaction")){
            String type = (String) view.transactionType.getSelectedItem();
            String stockTik = (String) view.stocks.getSelectedItem();
            String day = view.dayField.getText();
            String month = view.monthField.getText();
            String year = view.yearField.getText();
            int volume = Integer.parseInt(view.volumeField.getText());
            int portfolio = view.currPortfolio;
            String currDate = year + "-" + month + "-" + day;
            switch(type){
            case "buy":
                model.buyStock(stockTik, portfolio, currDate, volume);
                break;
            case "sell":
                model.sellStock(stockTik, portfolio, currDate, volume);
                break;
            }
            XYDataset data = model.getCustData(portfolio);
            view.setChart(data, view.currCustomer);
            String[][] transactionData = model.getTransactionData(portfolio);
            view.transactionView(transactionData); 
            ArrayList<String> stocks = model.getStocks();
            view.setNewTransactionPane(stocks, model.currentValue);
            }
        else if (description.equals("Pick your sign ;)")){
            int size = view.stocks.getItemCount();
            int selection =(int) (Math.random() * (size));
            view.stocks.setSelectedIndex(selection);
        } else if (description.equals("Remove Portfolio #:")){
            model.deletePortfolio((int) view.ports.getSelectedItem());
            ArrayList<Integer> custPortfolios = model.getPortfolios(view.currCustomer);
            view.setPortfolioView(custPortfolios, view.currCustomer);
        } else if (description.equals("Submit")){
            String day = view.oldDayField.getText();
            String month = view.oldMonthField.getText();
            String year = view.oldYearField.getText();
            int volume = Integer.parseInt(view.oldVolumeField.getText());
            int newVolume = Integer.parseInt(view.newVolumeField.getText());
            int portfolio = view.currPortfolio;
            String currDate = year + "-" + month + "-" + day;
            model.updateTransaction((String) view.editStocks.getSelectedItem(), portfolio, currDate, volume, newVolume);
            XYDataset data = model.getCustData(portfolio);
            view.setChart(data, view.currCustomer);
            String[][] transactionData = model.getTransactionData(portfolio);
            view.transactionView(transactionData); 
            ArrayList<String> stocks = model.getStocks();
            view.setNewTransactionPane(stocks, model.currentValue);
        }
        }
        
    }


