package taboo.com.petstorefood;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import taboo.com.petstorefood.adapter.CartItemAdapter;
import taboo.com.petstorefood.model.entity.CartItem;
import taboo.com.petstorefood.model.entity.Category;
import taboo.com.petstorefood.model.entity.PetFood;
import taboo.com.petstorefood.model.responseModel.ApiResponse;
import taboo.com.petstorefood.repository.CartItemRepository;
import taboo.com.petstorefood.repository.CategoryRepository;
import taboo.com.petstorefood.repository.PetFoodRepository;
import taboo.com.petstorefood.service.CartItemService;
import taboo.com.petstorefood.service.CategoryService;
import taboo.com.petstorefood.service.PetFoodService;

public class CartFragment extends Fragment {
    List<PetFood> petFoodList;
    List<CartItem> cartItemList;
    List<Category> categories = new ArrayList<>();
    RecyclerView listItem;
    PetFoodService petFoodService;
    CategoryService categoryService;
    CartItemService cartItemService;
    CartItemAdapter adapter;
    private Uri imageUri;
    private ActivityResultLauncher<String> imagePickerLauncher;
    TextView txtTotal;
    public CartFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        petFoodService = PetFoodRepository.getPetFoodService();
        categoryService = CategoryRepository.getCategoryService();
        cartItemService = CartItemRepository.getCartService();
        listItem = view.findViewById(R.id.cartRecyclerView);
        txtTotal = view.findViewById(R.id.totalTextView);
        petFoodList = new ArrayList<>();
        cartItemList = new ArrayList<>();
        adapter = new CartItemAdapter(requireActivity(), cartItemList, this);
        listItem.setAdapter(adapter);
        listItem.setLayoutManager(new LinearLayoutManager(requireActivity()));

        fetchCart();

        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                result -> {
                    if (result != null) {
                        imageUri = result;
                    }
                });

        return view;
    }

    private void fetchCart() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        String userId = sharedPreferences.getString("UserId", null);
        Call<ApiResponse<List<CartItem>>> call = cartItemService.getAllCarts(userId);
        Log.d("API_CALL", "Calling endpoint: " + call.request().url());
        call.enqueue(new Callback<ApiResponse<List<CartItem>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<CartItem>>> call, Response<ApiResponse<List<CartItem>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    int total = 0;
                    // Cập nhật danh sách thực phẩm và thông báo cho adapter
                    cartItemList.clear(); // Xóa danh sách hiện tại
                    cartItemList .addAll(response.body().getData()); // Thêm dữ liệu mới vào danh sách
                    adapter.notifyDataSetChanged(); // Cập nhật adapter
                    for (CartItem cartItem:
                         cartItemList) {
                        total += cartItem.getPetFood().getPrice() * cartItem.getQuantity();
                    }
                    txtTotal.setText("Total: " + total +"VND");
                } else {
                    Toast.makeText(getContext(), "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<CartItem>>> call, Throwable t) {

            }
        });
    }

    private void callApiPut(CartItem cartItem){

        Call<ApiResponse<CartItem>> call = cartItemService.updateCart(cartItem);
        Log.d("API_CALL", "Calling endpoint: " + call.request().url());
        call.enqueue(new Callback<ApiResponse<CartItem>>() {
            @Override
            public void onResponse(Call<ApiResponse<CartItem>> call, Response<ApiResponse<CartItem>> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Item updated successfully", Toast.LENGTH_SHORT).show();
                    fetchCart();
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

    public void callApiDelete(int index){
        UUID id = cartItemList.get(index).getId();
        Call<ApiResponse<CartItem>> call = cartItemService.deleteCart(id);
        Log.d("API_CALL", "Calling endpoint: " + call.request().url());
        call.enqueue(new Callback<ApiResponse<CartItem>>() {
            @Override
            public void onResponse(Call<ApiResponse<CartItem>> call, Response<ApiResponse<CartItem>> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Item updated successfully", Toast.LENGTH_SHORT).show();
                    fetchCart();
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
        if(!check){
            txtTitle.setText("Update quantity of "+ cartItemList.get(index).getPetFood().getName());
             editTextQuantity.setText(cartItemList.get(index).getQuantity()+"");
        }


        buttonSubmit.setOnClickListener(v -> {
            UUID id = cartItemList.get(index).getId();
            int quantity = Integer.parseInt( editTextQuantity.getText().toString());
            CartItem cartItem = new CartItem(id,quantity);

            if (quantity > 0 ) {
                if(!check){
                    callApiPut(cartItem);

                }
                popupWindow.dismiss();
            } else {
                Toast.makeText(getContext(), "Please fill in all fields and select an image", Toast.LENGTH_SHORT).show();
            }
        });
    }



}

