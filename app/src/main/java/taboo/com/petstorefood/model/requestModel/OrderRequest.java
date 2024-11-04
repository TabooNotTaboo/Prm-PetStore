package taboo.com.petstorefood.model.requestModel;

import java.util.List;
import java.util.UUID;

public class OrderRequest {
    private String userId;
    private String orderCode;
    private int shipFees;
    private String shipAddress;
    private String shippingDate;
    private int paymentMethod;
    private List<UUID> cartItemIds;

    public OrderRequest(String userId, String orderCode, int shipFees, String shipAddress, String shippingDate, int paymentMethod, List<UUID> cartItemIds) {
        this.userId = userId;
        this.orderCode = orderCode;
        this.shipFees = shipFees;
        this.shipAddress = shipAddress;
        this.shippingDate = shippingDate;
        this.paymentMethod = paymentMethod;
        this.cartItemIds = cartItemIds;
    }
}
