package taboo.com.petstorefood.service;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import taboo.com.petstorefood.model.requestModel.LoginRequest;
import taboo.com.petstorefood.model.requestModel.RegisterRequest;
import taboo.com.petstorefood.model.requestModel.UpdateAccountRequest;
import taboo.com.petstorefood.model.responseModel.ApiResponse;
import taboo.com.petstorefood.model.responseModel.LoginResponse;
import taboo.com.petstorefood.model.responseModel.RegisterResponse;
import taboo.com.petstorefood.model.responseModel.UpdateAccountResponse;

public interface AuthenService {
  /*  @POST(AUTHEN)
    Call<ApiResponse<LoginResponse>> register(@Body RegisterRequest request);*/

    @POST("authentications/v1/authentication")
    Call<ApiResponse<LoginResponse>> login(@Body LoginRequest request);

    @POST("authentications/v1/registration")
    Call<ApiResponse<RegisterResponse>> register(@Body RegisterRequest request);

    @PUT("accounts/v1/account")
    Call<ApiResponse<UpdateAccountResponse>> updateAccount(@Body UpdateAccountRequest request);
}
