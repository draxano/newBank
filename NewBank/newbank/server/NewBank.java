package newbank.server;

import java.util.HashMap;
import java.util.ArrayList;

public class NewBank {
    // all bank instances will have a HashTable containing all customers
    private static final NewBank bank = new NewBank();
    private HashMap<String, Customer> customers;

    private NewBank() {
        customers = new HashMap<>();
        addTestData();
    }

    // just a test function to add some fake customers. you can add an account to their list,
    // then put the customer into the HashMap (the bank)
    private void addTestData() {
        Customer bhagy = new Customer();
        bhagy.addAccount(new Account("Main", 1000.0));
        customers.put("Bhagy", bhagy);

        Customer christina = new Customer();
        christina.addAccount(new Account("Savings", 1500.0));
        customers.put("Christina", christina);

        Customer john = new Customer();
        john.addAccount(new Account("Checking", 250.0));
        customers.put("John", john);
    }

    public static NewBank getBank() {
        return bank;
    }

    // creates customer ID when login details are checked. The username is the key
    public synchronized CustomerID checkLogInDetails(String userName, String password) {
        if (customers.containsKey(userName)) {
            return new CustomerID(userName);
        }
        return null;
    }

    // commands from the NewBank customer are processed in this method
    public synchronized String processRequest(CustomerID customer, String request) {
        String[] tokens = request.split(" ");
        String cmd;
        
        if(tokens.length == 0) {
            return "FAIL";
        }
        
        cmd = tokens[0];
        
        // current checks if the HashTable has the username (key) inside
        if (customers.containsKey(customer.getKey())) {
            // if the request says SHOWMYACCOUNTS (with the correct key), then accounts will be shown
            if (cmd.toLowerCase().contains("showmyaccounts") || cmd.equals("1")) {
                return showMyAccounts(customer);
            } else if (cmd.toLowerCase().contains("newaccount") || cmd.toLowerCase().equals("2")) {
                if(tokens.length < 2) {
                    return "FAIL";
                }
                
                return newAccount(customer, tokens[1]);
            } else if (cmd.toLowerCase().contains("move") || cmd.toLowerCase().equals("3")) {
                if(tokens.length < 4) {
                    return "FAIL";
                }
                
                return move(customer, tokens[1], tokens[2], tokens[3]);
            } else if (cmd.toLowerCase().contains("pay") || cmd.toLowerCase().equals("4")) {
                if(tokens.length < 3) {
                    return "FAIL";
                }
                
                return pay(customer, tokens[1], tokens[2]);
            } else if (cmd.toLowerCase().contains("exit") || cmd.toLowerCase().equals("5")) {
                return "exit";
            }else {
                return "FAIL";
            }
        }
        // otherwise, nothing is shown
        return "FAIL";
    }

    // go through the HashTable to get the customer object so we can use the AccountsToString method to see the account details
    // e.g. customers.get("bob") will return bob object. then bob.accountsToString() gets his details
    private String showMyAccounts(CustomerID customer) {
        return (customers.get(customer.getKey())).accountsToString();
    }
    
    private String newAccount(CustomerID customer, String name) {
        if(!customers.containsKey(customer.getKey())) {
            return "FAIL";
        }
        else if(name.length() < 1) {
            return "FAIL";
        }

        Account account = new Account(name, 0.0);
        customers.get(customer.getKey()).addAccount(account);

        return account.toString();
    }
    
    private String move(CustomerID customer, String amount, String from, String to) {
        return "Move money - TBD";
    }
    
    private String pay(CustomerID customer, String person, String amount) {
        return "Pay someone - TBD";
    }

}
