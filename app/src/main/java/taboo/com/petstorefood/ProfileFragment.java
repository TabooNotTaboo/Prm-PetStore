package taboo.com.petstorefood;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import taboo.com.petstorefood.model.requestModel.UpdateAccountRequest;
import taboo.com.petstorefood.model.responseModel.ApiResponse;
import taboo.com.petstorefood.model.responseModel.UpdateAccountResponse;
import taboo.com.petstorefood.repository.AuthenRepository;
import taboo.com.petstorefood.service.AuthenService;

public class ProfileFragment extends Fragment {
    Button btnLogout, btnEditProfile, btnChangePassword;
    TextView txtFullName, txtEmail, txtPhoneNumber, txtUsername, txtDOB, txtGender, txtAddress;
    AuthenService authenService;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        txtFullName = view.findViewById(R.id.txtFullName);
        txtEmail = view.findViewById(R.id.txtEmail);
        txtPhoneNumber = view.findViewById(R.id.txtPhoneNumber);
        txtUsername = view.findViewById(R.id.txtUsername);
        txtDOB = view.findViewById(R.id.txtDOB);
        txtGender = view.findViewById(R.id.txtGender);
        txtAddress = view.findViewById(R.id.txtAddress);
        btnEditProfile = view.findViewById(R.id.button_edit_profile);
        btnLogout = view.findViewById(R.id.button_logout);

        authenService = AuthenRepository.getAuthenService();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        String userId = sharedPreferences.getString("UserId", null);
        String fullname = sharedPreferences.getString("FullName", null);
        String email = sharedPreferences.getString("Email", null);
        String phoneNumber = sharedPreferences.getString("PhoneNumber", null);
        String username = sharedPreferences.getString("Username", null);
        String dob = sharedPreferences.getString("DOB", null);
        String gender = sharedPreferences.getString("Gender", null);
        String address = sharedPreferences.getString("Address", null);

        txtFullName.setText(fullname);
        txtEmail.setText(email);
        txtPhoneNumber.setText(phoneNumber);
        txtUsername.setText(username);
        txtDOB.setText(dob);
        txtGender.setText(gender);
        txtAddress.setText(address);

        btnEditProfile.setOnClickListener(v -> {
            View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.popup_edit_profile, null);
            EditText editTextFullName = dialogView.findViewById(R.id.editTextFullName);
            EditText editTextEmail = dialogView.findViewById(R.id.editTextEmail);
            EditText editTextUsername = dialogView.findViewById(R.id.editTextUsername);
            EditText editTextPhoneNumber = dialogView.findViewById(R.id.editTextPhoneNumber);
            EditText editTextGender = dialogView.findViewById(R.id.editTextGender);
            EditText editTextAddress = dialogView.findViewById(R.id.editTextAddress);
            EditText editTextDOB = dialogView.findViewById(R.id.editTextDOB);

            editTextFullName.setText(txtFullName.getText().toString());
            editTextEmail.setText(txtEmail.getText().toString());
            editTextUsername.setText(txtUsername.getText().toString());
            editTextPhoneNumber.setText(txtPhoneNumber.getText().toString());
            editTextGender.setText(txtGender.getText().toString());
            editTextAddress.setText(txtAddress.getText().toString());
            editTextDOB.setText(txtDOB.getText().toString());

            AlertDialog dialog = new AlertDialog.Builder(getActivity())
                    .setView(dialogView)
                    .setPositiveButton("Save", (dialogInterface, i) -> {
                        updateProfile(
                                editTextFullName.getText().toString(),
                                editTextEmail.getText().toString(),
                                editTextUsername.getText().toString(),
                                editTextPhoneNumber.getText().toString(),
                                editTextGender.getText().toString(),
                                editTextAddress.getText().toString(),
                                editTextDOB.getText().toString()
                        );
                    })
                    .setNegativeButton("Cancel", null)
                    .create();
            dialog.show();
        });

        btnLogout.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        });

        return view;
    }

    private void updateProfile(String fullName, String email, String username, String phoneNumber, String gender, String address, String dateOfBirth) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        String userId = sharedPreferences.getString("UserId", null);

        // Create UpdateAccountRequest with new values
        UpdateAccountRequest request = new UpdateAccountRequest(userId, username, email, phoneNumber, fullName, address, gender, dateOfBirth);

        // Call API to update profile
        Call<ApiResponse<UpdateAccountResponse>> call = authenService.updateAccount(request);
        call.enqueue(new Callback<ApiResponse<UpdateAccountResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<UpdateAccountResponse>> call, Response<ApiResponse<UpdateAccountResponse>> response) {
                if (response.isSuccessful()) {
                    UpdateAccountResponse updatedProfile = response.body().getData();

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("FullName", updatedProfile.getFullName());
                    editor.putString("Email", updatedProfile.getEmail());
                    editor.putString("Username", updatedProfile.getUserName());
                    editor.putString("PhoneNumber", updatedProfile.getPhoneNumber());
                    editor.putString("Address", updatedProfile.getAddress());
                    editor.putString("Gender", updatedProfile.getGender());
                    editor.putString("DOB", updatedProfile.getDateOfBirth());

                    editor.apply();


                    txtFullName.setText(updatedProfile.getFullName());
                    txtEmail.setText(updatedProfile.getEmail());
                    txtPhoneNumber.setText(updatedProfile.getPhoneNumber());
                    txtUsername.setText(updatedProfile.getUserName());
                    txtGender.setText(updatedProfile.getGender());
                    txtAddress.setText(updatedProfile.getAddress());
                    txtDOB.setText(updatedProfile.getDateOfBirth());

                    Toast.makeText(getActivity(), "Profile updated!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<UpdateAccountResponse>> call, Throwable t) {
                Toast.makeText(getActivity(), "Failed to update profile", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
