# BuyIt
BuyIt is a Java application that allows people to perform online commerce around the world.
Authors:
Evan Law
Kenneth Carroll
Tsun (Jaeryn) Chu

### Version
1.0 BETA

### Changelog:
* July 20, 2015:
    Basic user interface created for BETA testing. Users can register for a username and password and no other information will be asked. Users can login to the application and access additional functionality of the application such as sell, buy, and exit. 
* July 29, 2015:
    Additional user interface functionality has been added. Asks for first name, last name, etc... A random number generator funtion was created to give new items an identification for searching ability.

### Completed:
* Password checking if entered passwords match for varification and if password meets the following requirements of at least one uppercase, one lowercase, and one special character and a minimum length of 12. 
* User registration interface (asks user for full information indicated in report)
* User login interface
    *  Additional interface after sucessful logon such as sell, buy, exit program
        *  Able to "Sell" and write to database
* Queries to insert data into database tables
* Connection to database (mysql)
* All new items are marked "published", when searching items with the status "published" will only display.

### Incomplete:
* Way to search database and display "published" entries only
* Hashing function to salt passwords
* Marking entries as "unpublished" when user changes the items status
* Modifying items fields and writing changes back to database

### Member Contributions:
July 20, 2015: Everyone worked on the entire project together as well as planning what columns should be created in each table and common variable names in the program and table columns. All query code was implemented my Kenneth and the basic skeleton structure was created by Evan and Tsun. 

July 28, 2015: Evan added additional functionality to the user interfacer of the user registration option. A random number generator of 5 digits was added in as a function to give each new item entry an ID. A status identification to new items have "published" in them so searching for them is filtered out from "unpublished" items. Kenneth rewrote most of the code to use Prepared Statements, making code format cleaner and nicer to work with. Code to write to the database has been modified to only write to it after all user input has been made. Connection closes after writing to the database. 
