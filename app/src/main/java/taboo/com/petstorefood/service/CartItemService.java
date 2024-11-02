package taboo.com.petstorefood.service;

import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import taboo.com.petstorefood.model.entity.CartItem;
import taboo.com.petstorefood.model.requestModel.CartItemRequest;
import taboo.com.petstorefood.model.responseModel.ApiResponse;

public interface CartItemService {
    @GET("cart-items/v1/cart-items")
    Call<ApiResponse<List<CartItem>>> getAllCarts(@Query("filterOptions.UserId") String userId);

    @POST("cart-items/v1/cart-item")
    Call<ApiResponse<CartItem>> createCart(@Body CartItemRequest request);

    @PUT("cart-items/v1/cart-item")
    Call<ApiResponse<CartItem>> updateCart(@Body CartItem request);

    @DELETE("cart-items/v1/cart-item/{id}")
    Call<ApiResponse<CartItem>> deleteCart(@Path("id") UUID id);
}
