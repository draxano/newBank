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

}
