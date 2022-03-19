1. Added SQLite library via module settings in IntelliJ
2. Included library as a dependency in the POM.xml file (added Maven framework support for this)
3. Created database connection class (dbConnection) to connect to db file that is located inside database package
4. The dbOperations class has common search functions for checking the database. currently the database only has one table
   called 'users' and each user has a username and a password 



* A useful way to manipulate the database (i.e. create new tables etc..) is called 'DB browser for SQLite' : https://sqlitebrowser.org/
* Good tutorial for using SQLite https://www.sqlitetutorial.net/sqlite-java/