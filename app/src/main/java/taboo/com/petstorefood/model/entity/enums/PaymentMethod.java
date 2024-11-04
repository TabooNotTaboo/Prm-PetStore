package taboo.com.petstorefood.model.entity.enums;

public enum PaymentMethod {
    Cash(0),
    Online(1);

    private final int value;

    // Constructor
    PaymentMethod(int value) {
        this.value = value;
    }

    // Getter for value
    public int getValue() {
        return value;
    }
}
