package taboo.com.petstorefood.model.requestModel;

import java.util.UUID;

public class CartItemRequest {
    private String userId;
    private String petFoodId;
    private int quantity;

    public CartItemRequest(String userId, String petFoodId, int quantity) {
        this.userId = userId;
        this.petFoodId = petFoodId;
        this.quantity = quantity;
    }

    public String getId() {
        return userId;
    }

    public void setId(String userId) {
        this.userId = userId;
    }

    public String getPetFoodId() {
        return petFoodId;
    }

    public void setPetFoodId(String petFoodId) {
        this.petFoodId = petFoodId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
