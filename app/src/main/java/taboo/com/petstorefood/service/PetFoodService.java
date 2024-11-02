package taboo.com.petstorefood.service;

import java.util.List;
import java.util.UUID;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import taboo.com.petstorefood.model.entity.PetFood;
import taboo.com.petstorefood.model.responseModel.ApiResponse;

public interface PetFoodService {
    @GET("pet-food/v1/pet-foods")
    Call<ApiResponse<List<PetFood>>> getAllFood(@Query("filterOptions.Name") String name,
                                                @Query("filterOptions.PriceRange.PriceFrom") Double minPrice,
                                                @Query("filterOptions.PriceRange.PriceTo") Double maxPrice,
                                                @Query("sortOptions") Integer sortOption);

    @Multipart
    @POST("pet-food/v1/pet-food")
    Call<ApiResponse<PetFood>> createFood(
            @Part MultipartBody.Part image,
            @Part("name") RequestBody name,
            @Part("description") RequestBody description,
            @Part("price") RequestBody price,
            @Part("quantity") RequestBody quantity,
            @Part("brand") RequestBody brand,
            @Part("categoryId") RequestBody categoryId
    );

    @Multipart
    @PUT("pet-food/v1/pet-food")
    Call<ApiResponse<PetFood>> updateFood(
            @Part MultipartBody.Part image,
            @Part("id") RequestBody id,
            @Part("name") RequestBody name,
            @Part("description") RequestBody description,
            @Part("price") RequestBody price,
            @Part("quantity") RequestBody quantity,
            @Part("brand") RequestBody brand,
            @Part("categoryId") RequestBody categoryId
    );

    @DELETE("pet-food/v1/pet-food/{id}")
    Call<ApiResponse<PetFood>> deleteFood(@Path("id")UUID id);

}
