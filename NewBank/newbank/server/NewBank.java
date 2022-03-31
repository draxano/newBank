package newbank.server;

import newbank.database.dbCreateOperations;
import newbank.database.dbOperations;
import newbank.database.dbReadOperations;
import newbank.database.dbUpdateOperations;

import java.util.HashMap;
import java.util.ArrayList;

public class NewBank {
    // all bank instances will have a HashTable containing all customers
    private static final NewBank bank = new NewBank();
    private HashMap<String, Customer> customers;

    private NewBank() {
        customers = dbOperations.loadMap();
    }

    public static NewBank getBank() {
        return bank;
    }

    // now this method checks the database for the username and the password
    public synchronized boolean checkLogInDetails(String userName, String password) {
        return dbReadOperations.checkLogin(userName, password);
    }

    // creates new user and puts into database
    public boolean createNewUser(String userName, String password) {
        if (dbCreateOperations.insert(userName, password)) {
            customers.put(userName, new Customer());
            return true;
        }
        return false;
    }

    // gets account from database and adds to customer object inside the hashmap
    public boolean retrieveAccounts(String customer) {
        // if the map already contains accounts then no need to duplicate results
        if (!customers.get(customer).isEmpty()) return true;
        // otherwise, create new accounts list and populate it from database
        ArrayList<Account> accounts = dbReadOperations.getAccounts(customer);
        if (!accounts.isEmpty()) {
            for (Account account : accounts) {
                customers.get(customer).addAccount(account);
            }
            return true;
        } else {
            return false;
        }
    }

    // commands from the NewBank customer are processed in this method
    public synchronized String processRequest(String customer, String request) {
        String[] tokens = request.split(" ");
        String cmd;

        if (tokens.length == 0) {
            return "FAIL";
        }

        cmd = tokens[0];

        // current checks if the HashTable has the username (key) inside
        if (customers.containsKey(customer)) {
            if (cmd.toLowerCase().contains("showmyaccounts") || cmd.equals("1")) {
                return retrieveAccounts(customer) ? showMyAccounts(customer) : "No accounts have been opened for this user. " +
                        "Select option 2 to open an account.";
            } else if (cmd.toLowerCase().contains("newaccount") || cmd.toLowerCase().equals("2")) {
                return "Open a new bank account:";
            } else if (cmd.toLowerCase().contains("withdraw") || cmd.toLowerCase().equals("3")) {
                return "Withdraw Money:";
            } else if (cmd.toLowerCase().contains("deposit") || cmd.toLowerCase().equals("4")) {
                return "Deposit Money:";
            } else if (cmd.toLowerCase().contains("exit") || cmd.toLowerCase().equals("5")) {
                return "exit";
            } else {
                return "FAIL";
            }
        }
        // otherwise, nothing is shown
        return "FAIL";
    }

    // go through the HashTable to get the customer object so we can use the AccountsToString method to see the account details
    // e.g. customers.get("bob") will return bob object. then bob.accountsToString() gets his details
    private String showMyAccounts(String customer) {
        return (customers.get(customer)).accountsToString();
    }

//    // Create a new account given an existing cutomer Id
//    private String newAccount(CustomerID customerid, String name) {
//        if (!customers.containsKey(customerid.getKey())) {
//            return "FAIL";
//        } else if (name.length() < 1) {
//            return "FAIL";
//        }
//        Customer customer = customers.get(customerid.getKey());
//        if (customer.hasAccount(name)) {
//            return "FAIL - Account already exists";
//        }
//        Account account = new Account(name, 0.0);
//        customer.addAccount(account);
//        return account.toString();
//    }

//    private String move(CustomerID customer, String amount, String from, String to) {
//        return "Move money - TBD";
//    }

//    private String pay(CustomerID customer, String person, String amount) {
//        return "Pay someone - TBD";
//    }

    // adds money to an account (if account exists), returns confirmation message.
    public String depositMoney(String userName, String accountName, double deposit) {
        Account account = dbReadOperations.getAccount(userName, accountName); // obtain an account object
        if (account == null) return "Account does not exist. Deposit request failed.";
        int accountId = dbReadOperations.getAccountId(userName, accountName);

        double currentBalance = account.getBalance(); // use account object to obtain balance
        double newBalance = currentBalance + deposit; // calculate new balance
        if (dbUpdateOperations.update(accountId, newBalance)) {
            return accountName + " has been credited with " + deposit + "." + " The new balance is " + newBalance;
        }
        return "Deposit request has failed.";
    }

    // withdraws money from account (if enough money present && account exists), returns confirmation message.
    public String withdrawMoney(String userName, String accountName, double withdraw) {
        Account account = dbReadOperations.getAccount(userName, accountName); // obtain account object
        if (account == null) return "Account does not exist. Deposit request failed.";
        int accountId = dbReadOperations.getAccountId(userName, accountName);

        double currentBalance = account.getBalance(); // use account object to obtain balance
        double newBalance = currentBalance - withdraw; // calculate new balance
        if (dbUpdateOperations.update(accountId, newBalance)) {
            return userName + " has withdrawn " + withdraw + " from " + accountName + "." + " The new balance is " + newBalance;
        }
        return "Withdraw request has failed. Double check your balance.";
    }

    // takes user's input of account type and starting balance and opens a new account
    // using the database
    public String processAccountRequest(String userName, String accountType, double startingBalance) {
        // check first what accounts already exist if any. make sure users do not add accounts of the same name
        ArrayList<Account> accounts = dbReadOperations.getAccounts(userName);
        if (!accounts.isEmpty()) {
            for (Account account : accounts) {
                if (account.getName().equalsIgnoreCase(accountType)) {
                    return "Account request denied. Account with this name already exists.";
                }
            }
        }

        if (dbCreateOperations.addAccount(userName, accountType, startingBalance)) {
            return "Account for " + userName + " has been created.";
        } else {
            return "Account request denied.";
        }
    }
}
