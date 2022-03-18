package newbank.server;

import java.util.ArrayList;

public class Customer {
    // When a customer is made, each gets given an empty list of accounts
    private ArrayList<Account> accounts;

    public Customer() {
        accounts = new ArrayList<>();
    }

    // to print out a customer's list of accounts
    public String accountsToString() {
        String s = "";
        for (Account a : accounts) {
            s += a.toString();
        }
        return s;
    }

    // to add a new account to a customer's list
    public void addAccount(Account account) {
        accounts.add(account);
    }

    // Whether the customer has an account of the given name
    public boolean hasAccount(String name) {
        for(Account a : accounts) {
            if(a.getName().equals(name)) {
                return true;
            }
        }

        return false;
    }
}
