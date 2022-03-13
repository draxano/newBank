package newbank.server;

import java.util.HashMap;

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
        // current checks if the HashTable has the username (key) inside
        if (customers.containsKey(customer.getKey())) {
            // if the request says SHOWMYACCOUNTS (with the correct key), then accounts will be shown
            if (request.equals("SHOWMYACCOUNTS") || request.equals("1")) {
                return showMyAccounts(customer);
            } else if (request.toLowerCase().contains("newaccount") || request.toLowerCase().equals("2")) {
                return "Create new account - TBD";
            } else if (request.toLowerCase().contains("move") || request.toLowerCase().equals("3")) {
                return "Move money - TBD";
            } else if (request.toLowerCase().contains("pay") || request.toLowerCase().equals("4")) {
                return "Pay someone - TBD";
            } else if (request.toLowerCase().contains("exit") || request.toLowerCase().equals("5")) {
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

}
