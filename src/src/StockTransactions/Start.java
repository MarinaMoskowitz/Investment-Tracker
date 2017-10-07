package src.StockTransactions;


public class Start {
    public static void main(String[] args){
        StockTransactions s = new StockTransactions();
        System.out.println(s.getCustomers());
        view view = new view(s.getCustomers());
        Controller c = new Controller(s, view);
    }
}
