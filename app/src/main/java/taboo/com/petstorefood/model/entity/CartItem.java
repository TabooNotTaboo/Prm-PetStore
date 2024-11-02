package taboo.com.petstorefood.model.entity;

import java.util.UUID;

public class CartItem {
    private UUID id;
    private int quantity;
    private PetFood petFood;

    public CartItem(UUID id, int quantity) {
        this.id = id;
        this.quantity = quantity;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public PetFood getPetFood() {
        return petFood;
    }

    public void setPetFood(PetFood petFood) {
        this.petFood = petFood;
    }


}
