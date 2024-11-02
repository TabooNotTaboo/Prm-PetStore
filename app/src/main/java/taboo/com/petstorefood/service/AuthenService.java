package taboo.com.petstorefood.service;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import taboo.com.petstorefood.model.requestModel.LoginRequest;
import taboo.com.petstorefood.model.responseModel.ApiResponse;
import taboo.com.petstorefood.model.responseModel.LoginResponse;

public interface AuthenService {
  /*  @POST(AUTHEN)
    Call<ApiResponse<LoginResponse>> register(@Body RegisterRequest request);*/

    @POST("authentications/v1/authentication")
    Call<ApiResponse<LoginResponse>> login(@Body LoginRequest request);
}
