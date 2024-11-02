package taboo.com.petstorefood.model.entity;

import java.util.UUID;

public class Cart {
    private UUID id;
    private UUID userId;

    public Cart(UUID id, UUID userId) {
        this.id = id;
        this.userId = userId;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }
}
