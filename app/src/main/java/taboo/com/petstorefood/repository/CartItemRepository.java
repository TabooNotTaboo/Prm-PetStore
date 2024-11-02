package taboo.com.petstorefood.repository;

import taboo.com.petstorefood.api.ApiClient;
import taboo.com.petstorefood.service.CartItemService;

public class CartItemRepository {
    public static CartItemService getCartService(){
        return ApiClient.getClient().create(CartItemService.class);

    }
}
