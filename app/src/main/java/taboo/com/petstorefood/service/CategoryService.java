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
import taboo.com.petstorefood.model.entity.Category;
import taboo.com.petstorefood.model.entity.PetFood;
import taboo.com.petstorefood.model.requestModel.LoginRequest;
import taboo.com.petstorefood.model.responseModel.ApiResponse;
import taboo.com.petstorefood.model.responseModel.CategoryResponse;
import taboo.com.petstorefood.model.responseModel.PetFoodResponse;

public interface CategoryService {
    @GET("category/v1/categories")
    Call<ApiResponse<List<Category>>> getAllCategories();

    @POST("category/v1/category")
    Call<ApiResponse<Category>> createCategory(@Body Category request);

    @PUT("category/v1/category")
    Call<ApiResponse<Category>> updateCategory(@Body Category request);

    @DELETE("category/v1/category/{id}")
    Call<ApiResponse<Category>> deleteCategory(@Path("id") UUID id);

}
