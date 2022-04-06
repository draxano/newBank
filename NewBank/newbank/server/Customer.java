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
        StringBuilder s = new StringBuilder();
        for (Account a : accounts) {
            s.append(a.toString());
            s.append(" , ");
        }
        return s.toString();
    }

    // to add a new account to a customer's list
    public void addAccount(Account account) {
        accounts.add(account);
    }

    // if customer has any accounts loaded in the program
    public boolean isEmpty() {
        return accounts.isEmpty();
    }

    // get the customer account of the given name
    public Account getAccount(String name) {
        for(Account a : accounts) {
            if(a.getName().equalsIgnoreCase(name)) {
                return a;
            }
        }
        return null;
    }

    public ArrayList<Account> getAccounts() {
        return accounts;
    }
}
