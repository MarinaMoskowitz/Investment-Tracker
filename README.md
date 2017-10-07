# Investment-Tracker
Utilizing MySQL database to create Java app for investors to track their financial portfolios 

Created my own client data that I used to insert into the database for testing purposes.
Used Quandl API to get top 50 S&P companies names and daily stock prices.


* Database is written in MySQL 
* Front-end code in Java 8 and jfreecharts, which is a Java library for graphing charts
* The controller, model, and view of application is all written in Java
* Database connectivity is JDBC 4.2


Step by Step User Interaction: 
CRUD methods the user is going to interact with the application
1) C- The user will can create a portfolio
2) R- The user will can read the data in database to see if they making a profit or
a lose on their portfolios by visually beautiful graphs displaying each of the companies in
their portfolios current stock price.
3) U- The user can update their portfolios by making transactions of either buying or selling stocks
4) D- the user can delete their portfolio if they want to


***BONOUS FEATURE*** 
User can have stocks suggested to them based on their horscope...functionality is all randomly generated, of course ;)


Future Work: 
* In the future I would like to add the functionality for users to be able to compare themselves to other users 
to see how their portfolios are doing compared to other users portfolios. 
* I would also like to implement the ability to account for stock splits, so user results will be more accurate.
* In terms of the database, I would like to add the ability for a companies stock price to update automatically 
each day without having to reload the data.  

