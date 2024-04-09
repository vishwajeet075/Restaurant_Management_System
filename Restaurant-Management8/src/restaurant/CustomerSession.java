package restaurant;

public class CustomerSession {
    private static int customerId;

    public static int getCustomerId() {
        return customerId;
    }

    public static void setCustomerId(int customerId) {
        CustomerSession.customerId = customerId;
    }
}
