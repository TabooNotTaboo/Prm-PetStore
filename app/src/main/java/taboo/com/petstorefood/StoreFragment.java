package taboo.com.petstorefood;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import taboo.com.petstorefood.adapter.ProductAdapter;
import taboo.com.petstorefood.model.entity.CartItem;
import taboo.com.petstorefood.model.entity.Category;
import taboo.com.petstorefood.model.entity.PetFood;
import taboo.com.petstorefood.model.requestModel.CartItemRequest;
import taboo.com.petstorefood.model.responseModel.ApiResponse;
import taboo.com.petstorefood.model.responseModel.LoginResponse;
import taboo.com.petstorefood.model.responseModel.PetFoodResponse;
import taboo.com.petstorefood.repository.CartItemRepository;
import taboo.com.petstorefood.repository.CategoryRepository;
import taboo.com.petstorefood.repository.PetFoodRepository;
import taboo.com.petstorefood.service.CartItemService;
import taboo.com.petstorefood.service.CategoryService;
import taboo.com.petstorefood.service.PetFoodService;

public class StoreFragment extends Fragment {
    List<PetFood> petFoodList;
    RecyclerView listItem;
    PetFoodService petFoodService;
    CategoryService categoryService;
    CartItemService cartItemService;

    ProductAdapter adapter;

    public StoreFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        petFoodService = PetFoodRepository.getPetFoodService();
        categoryService = CategoryRepository.getCategoryService();
        cartItemService = CartItemRepository.getCartService();
        View view = inflater.inflate(R.layout.fragment_store, container, false);
        listItem = view.findViewById(R.id.recycleFood);
        petFoodList = new ArrayList<>();
        adapter = new ProductAdapter(requireActivity(), petFoodList, this);
        listItem.setAdapter(adapter);
        listItem.setLayoutManager(new LinearLayoutManager(requireActivity()));
        fetchPetFood();
        return view;
    }

    private void fetchPetFood() {
        Call<ApiResponse<List<PetFood>>> call = petFoodService.getAllFood();
        Log.d("API_CALL", "Calling endpoint: " + call.request().url());
        call.enqueue(new Callback<ApiResponse<List<PetFood>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<PetFood>>> call, Response<ApiResponse<List<PetFood>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Cập nhật danh sách thực phẩm và thông báo cho adapter
                    petFoodList.clear(); // Xóa danh sách hiện tại
                    petFoodList.addAll(response.body().getData()); // Thêm dữ liệu mới vào danh sách
                    adapter.notifyDataSetChanged(); // Cập nhật adapter
                } else {
                    Toast.makeText(getContext(), "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<PetFood>>> call, Throwable t) {

            }
        });
    }

    public void callApiPost(CartItemRequest request){
        Call<ApiResponse<CartItem>> call = cartItemService.createCart(request);
        Log.d("API_CALL", "Calling endpoint: " + call.request().url());
        call.enqueue(new Callback<ApiResponse<CartItem>>() {
            @Override
            public void onResponse(Call<ApiResponse<CartItem>> call, Response<ApiResponse<CartItem>> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Item add successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<CartItem>> call, Throwable t) {
                Toast.makeText(getContext(), "Failed to create item: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showPopup(View anchorView, boolean check, int index){
        View popupView = getLayoutInflater().inflate(R.layout.popup_cart, null);
        PopupWindow popupWindow = new PopupWindow(popupView,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, true);
        popupWindow.showAtLocation(anchorView, android.view.Gravity.CENTER, 0, 0);


        EditText editTextQuantity = popupView.findViewById(R.id.editTextQuantity);
        TextView txtTitle = popupView.findViewById(R.id.textViewTitle);
        Button buttonSubmit = popupView.findViewById(R.id.buttonSubmit);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        String userId = sharedPreferences.getString("UserId", null);
        if(!check){

        }


        buttonSubmit.setOnClickListener(v -> {
            String foodId = petFoodList.get(index).getId().toString();
            int quantity = Integer.parseInt( editTextQuantity.getText().toString());
            CartItemRequest cartItem = new CartItemRequest(userId, foodId ,quantity);

            if (quantity > 0 ) {
                callApiPost(cartItem);
                popupWindow.dismiss();
            } else {
                Toast.makeText(getContext(), "Please fill in all fields and select an image", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
