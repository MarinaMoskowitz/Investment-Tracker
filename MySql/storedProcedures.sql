USE `stocktransactions`;


DELIMITER $$
DROP PROCEDURE IF EXISTS buy$$
CREATE PROCEDURE buy(stockTik varchar(10), portfolioId int(20), ddate date,volume int(10))
	BEGIN
		set @buyPrice = (select current_price from daily_stock_price where ccurrent_date = ddate && stock_symbol = stockTik LIMIT 1);
		INSERT INTO stock_transaction(portfolio_id, transaction_date, stock_symbol, open_price_at_transaction, transaction_type, transaction_volume)
				VALUES (portfolioId, ddate, stockTik, @buyPrice, 'buy', volume);
	END$$



DROP PROCEDURE IF EXISTS sell$$
CREATE PROCEDURE sell(stockTik varchar(10), portfolioId int(20), ddate date,volume int(10))
	BEGIN
		set @sellprice = (select current_price from daily_stock_price where ccurrent_date = ddate && stock_symbol = stockTik LIMIT 1);
		INSERT INTO stock_transaction (portfolio_id, transaction_date, stock_symbol, open_price_at_transaction, transaction_type, transaction_volume)
				VALUES (portfolioId, ddate, stockTik, @sellprice, 'sell', volume);
	END$$

DROP PROCEDURE IF EXISTS getCustPortfolios$$
CREATE PROCEDurE getCustPortfolios(custName varchar(20))
BEGIN
	SET @id = (SELECT customer_id from customer where customer_name = custName LIMIT 1);
    SELECT portfolio_id FROM portfolio where customer_id = @id;
END$$


DROP PROCEDURE IF EXISTS getCustId$$
CREATE PROCEDurE getCustId(custName varchar(20))
BEGIN
	SELECT customer_id from customer where customer_name = custName LIMIT 1;
END$$


DELIMITER ;
          
         

DROP PROCEDURE IF EXISTS addCustomer;
CREATE PROCEDURE addCustomer(cname varchar(20))
		INSERT INTO customer(customer_name)
				VALUES (cname);
           
           
DROP PROCEDURE IF EXISTS removeCustomer;
CREATE PROCEDURE removeCustomer(cname varchar(20))
		DELETE FROM customer where customer_name = cname;
        
        
        
DROP PROCEDURE IF EXISTS addPortfolio;
CREATE PROCEDURE addPortfolio(customerId int(20))
	INSERT INTO portfolio(customer_id)
			values(customerId);
            
DROP PROCEDURE IF EXISTS getCustData;
CREATE PROCEDURE getCustData(customerId int(20))
	SELECT transaction_date, open_price_at_transaction, transaction_type, transaction_volume FROM stock_transaction
		WHERE portfolio_id = customerId ORDER BY transaction_date desc;
        
        

        
CALL getCustData(20);

CALL getCustId('Brian');
        
CALL addPortfolio(1);
CALL addCustomer('Marina');

CALL addPortfolio(1);

CALL buy('AAPL', 1, '2017-03-22', 300);

CALL sell('AAPL', 1, '2017-04-05', 15);


        

                

        
		