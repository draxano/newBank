package newbank.server;

public class CustomerID {
    // all customers are assigned an ID, which at the moment is just a string value
    private String key;

    public CustomerID(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
