package src.StockTransactions;

import java.awt.EventQueue;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.LegendItemSource;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;



import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class view {

    ArrayList<String> customers;
    ArrayList<JButton> customerButtons;
    private JFrame frame;
    JPanel chartPane;
    JPanel cust;
    JFrame main;
    ActionListener listener;
    JPanel scrollPaneView;
    JPanel south;
    String currCustomer;
    int currCustomerId;
    JPanel east;
    JComboBox transactionType;
    JComboBox stocks;
    JTextField dayField;
    JTextField yearField;
    JTextField monthField;
    JTextField volumeField;
    JButton horoscope;
    int currPortfolio;
    JComboBox ports;
    JPanel currCust;
    JComboBox editStocks;
   JComboBox editTransactionType;
    JTextField oldVolumeField;
    JTextField oldMonthField;
   JTextField oldYearField;
   JTextField oldDayField;
    JTextField newVolumeField;
    
    

    /**
     * Create the application.
     */
    public view(ArrayList<String> customers) {
        this.customers = customers;
        customerButtons = new ArrayList();
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new JFrame();
        main = frame;
        frame.setBounds(100, 100, 1400, 900);
        frame.setPreferredSize(new Dimension(1400,800));
        //frame.setMaximumSize(new Dimension(1280,720));
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JScrollPane scrollPane = new JScrollPane();
        
        frame.getContentPane().setLayout(new BorderLayout(0, 0));
        frame.getContentPane().add(scrollPane, BorderLayout.WEST);
        
        
        
        this.cust = new JPanel();
        this.scrollPaneView = new JPanel();
        scrollPaneView.setLayout(new BorderLayout());
        scrollPaneView.add(cust, BorderLayout.CENTER);
        cust.setLayout(new BoxLayout(cust, BoxLayout.PAGE_AXIS));
        scrollPane.setViewportView(scrollPaneView);
        
        this.chartPane = new JPanel();
        
        frame.getContentPane().add(chartPane, BorderLayout.CENTER);
        for (String s:customers){
            System.out.println(s);
            JButton btnBrian = new JButton(s);
            customerButtons.add(btnBrian);
            cust.add(btnBrian);
        }
        
        XYDataset empty = new TimeSeriesCollection();
        this.setChart(empty, "");
        this.east = new JPanel();
        this.south = new JPanel();
        currCust = new JPanel();
    }
    
    public void transactionView(String[][] data){
        south.removeAll();
        String[] columns = new String[] {
          "Transaction Date","Stock Ticker", "Price at Transaction", "Transaction Type", "Transaction Volume"      
        };
        
        JTable table = new JTable(data, columns);
        south.setPreferredSize(new Dimension(1400, 150));
        table.setGridColor(Color.BLACK);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(south.getPreferredSize());
        south.add(scrollPane);
        main.add(south, BorderLayout.SOUTH);
        main.pack();
    }

    public void display(){
        this.frame.setVisible(true);
    }
    
    public void setListeners(final IActionListener listener){
        ActionListener listen = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                listener.action(e.getActionCommand());
            }
          };
         for (JButton j: customerButtons){
             j.addActionListener(listen);
         }
         this.listener = listen;
    }

    
    public void setNewTransactionPane(ArrayList<String> Stocks, float value){
        east.removeAll();
        
        JLabel currentValue = new JLabel(currCustomer + "'s portfolio value =");
        JLabel val = new JLabel(Float.toString(value));
        east.setLayout(new BoxLayout(east, BoxLayout.Y_AXIS));
        east.add(currentValue);
        east.add(val);
        JPanel newTransaction = new JPanel();
        newTransaction.setLayout(new BoxLayout(newTransaction, BoxLayout.Y_AXIS));
        transactionType = new JComboBox();
        transactionType.addItem("buy");
        transactionType.addItem("sell");
        newTransaction.setBorder(BorderFactory.createLineBorder(Color.black));
        JLabel newTransactions = new JLabel("New Transaction:");
        volumeField = new JTextField("Transaction Volume");
        monthField = new JTextField("Month of transaction");
        yearField = new JTextField("Year of  transaction");
        dayField = new JTextField("Day of transaction");
        currentValue.setAlignmentX(Component.LEFT_ALIGNMENT);


        JButton makeTransaction = new JButton("add transaction");
        makeTransaction.addActionListener(listener);
        stocks = new JComboBox(Stocks.toArray());
        newTransaction.add(newTransactions);
        newTransaction.add(stocks);
        newTransaction.add(transactionType);
        newTransaction.add(volumeField);
        newTransaction.add(monthField);
        newTransaction.add(yearField);
        newTransaction.add(dayField);
        newTransaction.add(makeTransaction);
        newTransaction.setMaximumSize(new Dimension(300, 200));
        east.setPreferredSize(new Dimension(200, 700));
        east.add(newTransaction);
        newTransaction.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JPanel horoscopeStuff = new JPanel();
        horoscopeStuff.setMaximumSize(new Dimension(200,200));
        JLabel horoscopeLabel1 = new JLabel("Generate a Stock based");
        JLabel horoscopeLabel2 = new JLabel("on your horoscope:");
        ArrayList<String> symbols = new ArrayList<String>();
        symbols.add("Aries");
        symbols.add("Taurus");
        symbols.add("Gemeni");
        symbols.add("Cancer");
        symbols.add("Leo");
        symbols.add("Virgo");
        symbols.add("Libra");
        symbols.add("Scorpio");
        symbols.add("Sagittarius");
        symbols.add("Capricorn");
        symbols.add("Aquarius");
        symbols.add("Pisces");
        JComboBox horoscopes = new JComboBox(symbols.toArray());
        horoscope = new JButton("Pick your sign ;)");
        horoscope.addActionListener(listener);

        
        horoscopeStuff.add(horoscopeLabel1);
        horoscopeStuff.add(horoscopeLabel2);
        horoscopeStuff.add(horoscopes);
        horoscopeStuff.add(horoscope);
        
        JPanel editPanel = new JPanel();
        editPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel editTransaction = new JLabel("Edit an");
        JLabel editTransaction2 = new JLabel("existing transaction:");
        editStocks = new JComboBox(Stocks.toArray());
        editTransactionType = new JComboBox();
        editTransactionType.addItem("buy");
        editTransactionType.addItem("sell");
        oldVolumeField = new JTextField("Transaction Volume");
        oldMonthField = new JTextField("Month of transaction");
        oldYearField = new JTextField("Year of  transaction");
        oldDayField = new JTextField("Day of transaction");
        newVolumeField = new JTextField("New transaction Volume");
        JButton editButton = new JButton("Submit");
        editButton.addActionListener(listener);
        
        editPanel.add(editTransaction);
        editPanel.add(editTransaction2);
        editPanel.add(editStocks);
        editPanel.add(editTransactionType);
        editPanel.add(oldVolumeField);
        editPanel.add(oldMonthField);
        editPanel.add(oldYearField);
        editPanel.add(oldDayField);
        editPanel.add(newVolumeField);
        editPanel.add(editButton);
        
        editPanel.setLayout(new BoxLayout(editPanel, BoxLayout.Y_AXIS));

        east.add(horoscopeStuff);
        east.add(editPanel);
        editPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        editPanel.setMaximumSize(new Dimension(200,250));
        horoscopeStuff.setAlignmentX(Component.LEFT_ALIGNMENT);
        main.add(east,BorderLayout.EAST);
        main.pack(); 
    }
    
    
    
    public void setPortfolioView(ArrayList<Integer> portfolios, String customerName){
        currCustomer = customerName;
        scrollPaneView.removeAll();
        cust.removeAll();
        JLabel label1 = new JLabel(customerName);
        cust.add(label1);
        for (int i:portfolios){
            JButton btnBrian = new JButton("Portfolio # " + Integer.toString(i));
            btnBrian.addActionListener(listener);
            cust.add(btnBrian);
        }
        currCust.removeAll();
        
        currCust.setLayout(new BoxLayout(currCust, BoxLayout.PAGE_AXIS));
        JButton back = new JButton("back");
        JButton addPortfolio = new JButton("add new portfolio");
        addPortfolio.addActionListener(this.listener);
        JButton removePortfolio = new JButton("Remove Portfolio #:");
        removePortfolio.addActionListener(this.listener);
        ports = new JComboBox(portfolios.toArray());
        ports.setToolTipText("select a portfolio to remove");
        back.addActionListener(this.listener);
        currCust.add(addPortfolio);
        currCust.add(removePortfolio);
        currCust.add(ports);
        currCust.add(back);
        scrollPaneView.add(currCust, BorderLayout.SOUTH);
        scrollPaneView.add(cust, BorderLayout.CENTER);
        scrollPaneView.repaint();
        main.pack();
        main.repaint();
    }
    
    public void setCustomerView(){
       south.removeAll();
       cust.removeAll();
       scrollPaneView.removeAll();
       scrollPaneView.add(cust, BorderLayout.CENTER);
       for (JButton j : customerButtons){
           cust.add(j);
       }
       cust.repaint();
       main.pack();
    }
    
    
    
    
    public void setChart(XYDataset data, String custName){
        chartPane.removeAll();
        chartPane.setPreferredSize(new Dimension(1000,600));
        JFreeChart dataChart = ChartFactory.createTimeSeriesChart(             
                custName, 
                "Day",              
                "Value",              
                data,             
                true,              
                true,              
                false);
        ChartPanel cp = new ChartPanel(dataChart);
        cp.setPreferredSize(chartPane.getPreferredSize());
        chartPane.add(cp);
        chartPane.repaint();
        main.pack();
    }

}
