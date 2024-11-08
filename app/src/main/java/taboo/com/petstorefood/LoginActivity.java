package taboo.com.petstorefood;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import taboo.com.petstorefood.model.requestModel.LoginRequest;
import taboo.com.petstorefood.model.responseModel.ApiResponse;
import taboo.com.petstorefood.model.responseModel.LoginResponse;
import taboo.com.petstorefood.repository.AuthenRepository;
import taboo.com.petstorefood.service.AuthenService;

public class LoginActivity extends AppCompatActivity {
    EditText username, password;
    AuthenService authenService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        Button loginButton = findViewById(R.id.loginButton);
        authenService = AuthenRepository.getAuthenService();
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = username.getText().toString().trim();
                String pwd = password.getText().toString().trim();
                loginUser(new LoginRequest(userName,pwd));
            }
        });

        TextView registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect to Register Activity
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }

    private void loginUser(LoginRequest loginRequest) {
        Call<ApiResponse<LoginResponse>> call = authenService.login(loginRequest);
        Log.d("API_CALL", "Calling login endpoint: " + call.request().url());
        call.enqueue(new Callback<ApiResponse<LoginResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<LoginResponse>> call, Response<ApiResponse<LoginResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<LoginResponse> apiResponse = response.body();
                    String accessToken = apiResponse.getData().getAccessToken();
                    String refreshToken = apiResponse.getData().getRefreshToken();
                    String role = apiResponse.getData().getRole();
                    UUID userId = apiResponse.getData().getUserId();
                    String userName = apiResponse.getData().getUserName();
                    String email = apiResponse.getData().getEmail();
                    String fullName = apiResponse.getData().getFullName();
                    String phoneNumber = apiResponse.getData().getPhoneNumber();
                    String address = apiResponse.getData().getAddress();
                    String gender = apiResponse.getData().getGender();
                    String dob = apiResponse.getData().getDateOfBirth();

                    String formattedDob = dob;
                    try {
                        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
                        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        Date date = inputFormat.parse(dob);
                        if (date != null) {
                            formattedDob = outputFormat.format(date); // "2003-03-04"
                        }
                    } catch (ParseException e) {
                        Log.e("DateParsing", "Failed to parse date: " + e.getMessage());
                    }

                    SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    editor.putString("UserId", userId.toString());
                    editor.putString("Role", role);
                    editor.putString("Username", userName);
                    editor.putString("Email", email);
                    editor.putString("FullName", fullName);
                    editor.putString("PhoneNumber", phoneNumber);
                    editor.putString("Address", address);
                    editor.putString("Gender", gender);
                    editor.putString("DOB", formattedDob);

                    editor.apply();
                    Toast.makeText(LoginActivity.this, "Login success!", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Đăng nhập thất bại!", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<ApiResponse<LoginResponse>> call, Throwable t) {
                Log.e("API Failure", t.getMessage());
            }
        });
    }
}
