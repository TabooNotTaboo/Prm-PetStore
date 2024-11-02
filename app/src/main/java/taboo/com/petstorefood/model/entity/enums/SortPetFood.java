package taboo.com.petstorefood.model.entity.enums;

public enum SortPetFood {
    PriceAscending(1),
    PriceDescending(2),
    QuantityAscending(3),
    QuantityDescending(4),
    CreateTimeAscending(5),
    CreateTimeDescending(6);

    private final int value;

    // Constructor
    SortPetFood(int value) {
        this.value = value;
    }

    // Getter for value
    public int getValue() {
        return value;
    }
}
