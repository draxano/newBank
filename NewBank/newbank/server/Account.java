package newbank.server;

public class Account {

    // All accounts that can be made has a name and starting balance
    private String accountName;
    private double openingBalance;

    public Account(String accountName, double openingBalance) {
        this.accountName = accountName;
        this.openingBalance = openingBalance;
    }

    // In order to print out account details
    public String toString() {
        return (accountName + ": " + openingBalance);
    }

    // Get the name of the account
    public String getName() {
        return accountName;
    }

    public double getBalance() {
        return openingBalance;
    }

    public void setBalance(double balance) {
        this.openingBalance = balance;
    }

}
