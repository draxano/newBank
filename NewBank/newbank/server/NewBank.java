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
    public boolean userPermission = false;

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
    private boolean retrieveAccounts(String customer) {
        // otherwise, create new accounts list and populate it from database
        // only if the account doesn't already exist
        ArrayList<Account> accounts = dbReadOperations.getAccounts(customer);
        if (accounts.isEmpty()) return false;
        ArrayList<Account> accountsLoaded = customers.get(customer).getAccounts();
        if (accountsLoaded.isEmpty()) {
            accounts.forEach(a -> customers.get(customer).addAccount(a));
        } else {
            accounts.stream().filter(a -> !accountsLoaded.contains(a)).forEach(a -> customers.get(customer).addAccount(a));
        }
        return true;
    }

    // commands from the NewBank customer are processed in this method
    public synchronized String processRequest(String customer, String request) {
        String[] tokens = request.split(" ");
        String cmd;

        if (tokens.length == 0) {
            return "Invalid Input.";
        }

        cmd = tokens[0];

        // current checks if the HashTable has the username (key) inside
        if (customers.containsKey(customer)) {
            if (cmd.toLowerCase().contains("showmyaccounts") || cmd.equals("1")) {
                return retrieveAccounts(customer) ? showMyAccounts(customer) : "No accounts have been opened for this user. " +
                        "Select option 2 to open an account.";
            } else if (cmd.toLowerCase().contains("newaccount") || cmd.equalsIgnoreCase("2")) {
                return "Open a new bank account:";
            } else if (cmd.toLowerCase().contains("withdraw") || cmd.equalsIgnoreCase("3")) {
                return "Withdraw Money:";
            } else if (cmd.toLowerCase().contains("deposit") || cmd.equalsIgnoreCase("4")) {
                return "Deposit Money:";
            } else if (cmd.toLowerCase().contains("transfer") || cmd.equalsIgnoreCase("5")) {
                return "Transfer Money:";
            } else if (cmd.toLowerCase().contains("exit") || cmd.equalsIgnoreCase("x")) {
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


    // adds money to an account (if account exists), returns confirmation message.
    public String depositMoney(String userName, String accountName, String deposit) {
        Account account = dbReadOperations.getAccount(userName, accountName); // obtain an account object
        if (account == null) return "Account does not exist. Deposit request failed.";
        if (!customers.get(userName).getAccounts().contains(account)) { // adding the account before we manipulate balance
            customers.get(userName).addAccount(account);
        }
        // check valid input, parse to double if true
        Double depositNum = getaDouble(deposit);
        if (depositNum == null) return "Request denied, Deposit amount not a valid number.";

        int accountId = dbReadOperations.getAccountId(userName, accountName);

        double currentBalance = account.getBalance(); // use account object to obtain balance
        double newBalance = currentBalance + depositNum; // calculate new balance
        if (dbUpdateOperations.update(accountId, newBalance)) {
            customers.get(userName).getAccount(accountName).setBalance(newBalance); // update customer object in hashMap
            return accountName + " has been credited with " + deposit + "." + " The new balance is " + newBalance;
        }
        return "Deposit request has failed.";
    }

    // withdraws money from account (if enough money present && account exists), returns confirmation message.
    public String withdrawMoney(String userName, String accountName, String withdraw) {
        Account account = dbReadOperations.getAccount(userName, accountName); // obtain account object
        if (account == null) return "Account does not exist. Withdraw request failed.";
        // check valid input, parse to double if true
        Double withdrawNum = getaDouble(withdraw);
        if (withdrawNum == null) return "Request denied, Withdraw amount not a valid number.";

        // then check if the user is withdrawing a valid amount
        if (withdrawNum >= 10_000) return "Withdraw request failed. Withdrawals of above 10k are not allowed";
        if (withdrawNum >= 1000 && !userPermission) return "Withdrawal request above 1000, would you like to proceed?";

        if (!customers.get(userName).getAccounts().contains(account)) { // adding the account before we manipulate balance
            customers.get(userName).addAccount(account);
        }

        int accountId = dbReadOperations.getAccountId(userName, accountName);

        double currentBalance = account.getBalance(); // use account object to obtain balance
        double newBalance = currentBalance - withdrawNum; // calculate new balance
        if (dbUpdateOperations.update(accountId, newBalance)) {
            customers.get(userName).getAccount(accountName).setBalance(newBalance); // update customer object in hashMap
            return userName + " has withdrawn " + withdraw + " from " + accountName + "." + " The new balance is " + newBalance;
        }
        return "Withdraw request has failed. Double check your balance.";
    }

    // transfers amount of money from account1 to account2 (if enough money present && account exists),
    // returns confirmation message.
    public String transferMoney(String userName, String firstAccountName, String secondAccountName, String transferAmount){
        Account account1 = dbReadOperations.getAccount(userName, firstAccountName);
        Account account2 = dbReadOperations.getAccount(userName, secondAccountName);
        if (account1 == null || account2 == null) {
            return "One or both of the entered accounts does not exist. Transfer request failed.";
        }
        if (!customers.get(userName).getAccounts().contains(account1)) { // adding the account before we manipulate balance
            customers.get(userName).addAccount(account1);
        }
        if (!customers.get(userName).getAccounts().contains(account2)) {
            customers.get(userName).addAccount(account2);
        }
        int account1ID = dbReadOperations.getAccountId(userName, firstAccountName);
        int account2ID = dbReadOperations.getAccountId(userName, secondAccountName);

        // check valid input, parse to double if true
        Double transferAmountNum = getaDouble(transferAmount);
        if (transferAmountNum == null) return "Request denied, Transfer amount is not a valid number.";

        double account1Balance = account1.getBalance(); // accessing the balance from account1
        double account2Balance = account2.getBalance(); // and account2
        double newAccount1Balance = account1Balance - transferAmountNum; // moving amount from account1 to account2
        double newAccount2Balance = account2Balance + transferAmountNum;

        // only if balance values in the database are updated, they will be set in the HashMap
        if (dbUpdateOperations.update(account1ID, newAccount1Balance) && dbUpdateOperations.update(account2ID, newAccount2Balance)){
            customers.get(userName).getAccount(firstAccountName).setBalance(newAccount1Balance);
            customers.get(userName).getAccount(secondAccountName).setBalance(newAccount2Balance);
            return userName + " has transferred £" + transferAmount + " from " + firstAccountName.toUpperCase()
                    + " to " + secondAccountName.toUpperCase() + ".\nHere is your current balance:\n"
                    + firstAccountName.toUpperCase() + ": £" + newAccount1Balance + "\n" + secondAccountName.toUpperCase()
                    + ": £" + newAccount2Balance;
        }
        return "Transfer request has failed. Double check your balance.";
    }

    // takes user's input of account type and starting balance and opens a new account
    // using the database
    public String processAccountRequest(String userName, String accountType, String startingBalance) {
        // check first what accounts already exist if any. make sure users do not add accounts of the same name
        ArrayList<Account> accounts = dbReadOperations.getAccounts(userName);
        if (!accounts.isEmpty()) {
            for (Account account : accounts) {
                if (account.getName().equalsIgnoreCase(accountType)) {
                    return "Account request denied. Account with this name already exists.";
                }
            }
        }
        Double startBalanceNum = getaDouble(startingBalance);
        if (startBalanceNum == null) return "Request denied, Starting balance not a valid number.";
        // if an account was created successfully then add the account to the associated customer object in the hashMap
        if (dbCreateOperations.addAccount(userName, accountType, startBalanceNum)) {
            customers.get(userName).addAccount(new Account(accountType, startBalanceNum));
            return "Account for " + userName + " has been created.";
        } else {
            return "Account request denied.";
        }
    }

    private Double getaDouble(String parsableString) {
        double number = 0;
        if (inputCheckNum(parsableString)) {
            number = Double.parseDouble(parsableString);
        } else {
            return null;
        }
        return number;
    }

    // checks if the given input is parsable
    private boolean inputCheckNum(String input) {
        if (input == null) return false;
        try {
            double d = Double.parseDouble(input);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}
