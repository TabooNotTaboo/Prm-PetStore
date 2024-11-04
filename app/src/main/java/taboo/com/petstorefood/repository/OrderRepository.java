package taboo.com.petstorefood.repository;

import taboo.com.petstorefood.ApiClient;
import taboo.com.petstorefood.service.OrderService;

public class OrderRepository {
    public static OrderService getOrderService(){
        return ApiClient.getClient().create(OrderService.class);

    }
}
