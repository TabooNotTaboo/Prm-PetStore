package taboo.com.petstorefood.service;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import taboo.com.petstorefood.model.entity.PetFood;
import taboo.com.petstorefood.model.requestModel.OrderRequest;
import taboo.com.petstorefood.model.responseModel.ApiResponse;

public interface OrderService {
    @POST("orders/v1/order")
    Call<ApiResponse<OrderRequest>> CreateOrder(@Body OrderRequest request);

    @PUT("orders/v1/order/success/{orderCode}")
    Call<ApiResponse<OrderRequest>> CreateOrderSuccess(@Path("orderCode") String orderCode);

    @PUT("orders/v1/order/fail/{orderCode}")
    Call<ApiResponse<OrderRequest>> CreateOrderFail(@Path("orderCode") String orderCode);
}
