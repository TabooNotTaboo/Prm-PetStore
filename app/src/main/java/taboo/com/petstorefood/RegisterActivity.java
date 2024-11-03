package taboo.com.petstorefood;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import taboo.com.petstorefood.model.requestModel.RegisterRequest;
import taboo.com.petstorefood.model.responseModel.ApiResponse;
import taboo.com.petstorefood.model.responseModel.RegisterResponse;
import taboo.com.petstorefood.repository.AuthenRepository;
import taboo.com.petstorefood.service.AuthenService;

public class RegisterActivity extends AppCompatActivity {
    EditText username, password, fullname, email, phonenumber, gender, dateofbirth, address;
    AuthenService authenService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = findViewById(R.id.usernameInRegister);
        password = findViewById(R.id.passwordInRegister);
        fullname = findViewById(R.id.fullNameInRegister);
        email = findViewById(R.id.emailInRegister);
        phonenumber = findViewById(R.id.phoneNumberInRegister);
        gender = findViewById(R.id.genderInRegister);
        dateofbirth = findViewById(R.id.dobInRegister);
        address = findViewById(R.id.addressInRegister);

        authenService = AuthenRepository.getAuthenService();

//        dateofbirth.setInputType(N);
        dateofbirth.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                showDatePicker();
            }
        });

        Button btnBack = findViewById(R.id.backButtonInRegister);
        Button registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usernameRq = username.getText().toString();
                String passwordRq = password.getText().toString();
                String fullnameRq = fullname.getText().toString();
                String emailRq = email.getText().toString();
                String phonenumberRq = phonenumber.getText().toString();
                String genderRq = gender.getText().toString();
                String dobRq = dateofbirth.getText().toString();
                String addressRq = address.getText().toString();

                if(usernameRq.equals("")) {
                    Toast.makeText(RegisterActivity.this, "Please enter Username!", Toast.LENGTH_LONG).show();
                } else if(passwordRq.equals("")) {
                    Toast.makeText(RegisterActivity.this, "Please enter Password!", Toast.LENGTH_LONG).show();
                } else if (fullnameRq.equals("")) {
                    Toast.makeText(RegisterActivity.this, "Please enter Fullname!", Toast.LENGTH_LONG).show();
                } else if (emailRq.equals("")) {
                    Toast.makeText(RegisterActivity.this, "Please enter Email!", Toast.LENGTH_LONG).show();
                } else if (phonenumberRq.equals("")) {
                    Toast.makeText(RegisterActivity.this, "Please enter Phone Number!", Toast.LENGTH_LONG).show();
                } else if (genderRq.equals("")) {
                    Toast.makeText(RegisterActivity.this, "Please enter Gender!", Toast.LENGTH_LONG).show();
                } else if (dobRq.equals("")) {
                    Toast.makeText(RegisterActivity.this, "Please enter Date of birth!", Toast.LENGTH_LONG).show();
                } else if (addressRq.equals("")) {
                    Toast.makeText(RegisterActivity.this, "Please enter Address!", Toast.LENGTH_LONG).show();
                }

                registerUser(new RegisterRequest(passwordRq, emailRq, usernameRq, genderRq, addressRq, dobRq, fullnameRq, phonenumberRq));
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }

    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    calendar.set(selectedYear, selectedMonth, selectedDay);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    dateofbirth.setText(dateFormat.format(calendar.getTime()));
                    dateofbirth.clearFocus(); // Clear focus to avoid re-triggering dialog
                }, year, month, day);
        datePickerDialog.show();
    }


    private void registerUser(RegisterRequest registerRequest) {
        Call<ApiResponse<RegisterResponse>> call = authenService.register(registerRequest);
        call.enqueue(new Callback<ApiResponse<RegisterResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<RegisterResponse>> call, Response<ApiResponse<RegisterResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(RegisterActivity.this, "Đăng ký thành công!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<RegisterResponse>> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Đăng nhập thất bại!", Toast.LENGTH_LONG).show();
            }
        });
    }
}
