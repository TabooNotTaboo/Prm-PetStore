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

import android.text.Editable;
import android.text.TextWatcher;
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
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.text.DecimalFormat;

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
import taboo.com.petstorefood.model.entity.enums.SortPetFood;
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
    EditText search;
    ProductAdapter adapter;
    Button btnPriceChange;
    String name;
    Double minPrice = null;
    Double maxPrice = null;
    Spinner sortSpinner;
    Integer sortOption;
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
        btnPriceChange = view.findViewById(R.id.priceRangeButton);
        sortSpinner = view.findViewById(R.id.sortSpinner);
        search = view.findViewById(R.id.searchBar);
        petFoodList = new ArrayList<>();
        adapter = new ProductAdapter(requireActivity(), petFoodList, this);
        listItem.setAdapter(adapter);
        listItem.setLayoutManager(new LinearLayoutManager(requireActivity()));
        fetchPetFood(null, null, null, null);

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                name = search.getText().toString();
                fetchPetFood(name, minPrice, maxPrice, sortOption);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        btnPriceChange.setOnClickListener(v -> {
            showPriceRangePopup(v);
        });

        String[] sortOptions = new String[SortPetFood.values().length];
        for (int i = 0; i < SortPetFood.values().length; i++) {
            sortOptions[i] = SortPetFood.values()[i].name();
        }

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, sortOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        sortSpinner.setAdapter(adapter);

        // Set a listener for item selection
        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected item
                SortPetFood selectedSort = SortPetFood.values()[position];
                // Handle sorting logic based on selectedSort
                handleSort(selectedSort);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle case when nothing is selected if needed
            }
        });
        return view;
    }

    private void fetchPetFood(String name, Double minPrice, Double maxPrice, Integer sortOption) {
        Call<ApiResponse<List<PetFood>>> call = petFoodService.getAllFood(name, minPrice, maxPrice, sortOption);
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


    public void showPriceRangePopup(View anchorView) {
        View popupView = getLayoutInflater().inflate(R.layout.layout_price_range, null);
        PopupWindow popupWindow = new PopupWindow(popupView,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, true);
        popupWindow.showAtLocation(anchorView, android.view.Gravity.CENTER, 0, 0);

        // Khởi tạo các view trong layout
        TextView priceRangeTextView = popupView.findViewById(R.id.priceRangeTextView);
        SeekBar priceSeekBar = popupView.findViewById(R.id.priceSeekBar);
        TextView minPriceTextView = popupView.findViewById(R.id.minPriceTextView);
        TextView maxPriceTextView = popupView.findViewById(R.id.maxPriceTextView);
        Button applyFilterButton = popupView.findViewById(R.id.applyFilterButton);

        // Đặt khoảng giá
        double minPrice = 0.0;
        double maxPrice = 10000000.0;

        // Thiết lập giá trị max của SeekBar
        priceSeekBar.setMax((int) maxPrice);

        // Tạo DecimalFormat để định dạng số
        DecimalFormat formatter = new DecimalFormat("#,###");

        // Cập nhật giá trị hiển thị cho min và max ban đầu
        minPriceTextView.setText("Min Price: " + formatter.format(minPrice) + " VND");
        maxPriceTextView.setText("Max Price: " + formatter.format(maxPrice) + " VND");

        // Lắng nghe sự thay đổi của SeekBar
        priceSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Giá trị khoảng giá dựa vào giá trị của SeekBar
                double selectedMaxPrice = minPrice + progress;

                // Cập nhật TextView hiển thị khoảng giá với định dạng dấu phẩy
                priceRangeTextView.setText("Price Range: " + formatter.format(minPrice) + " VND - " + formatter.format(selectedMaxPrice) + " VND");
                maxPriceTextView.setText("Max Price: " + formatter.format(selectedMaxPrice) + " VND");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Không cần dùng ở đây
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Không cần dùng ở đây
            }
        });

        // Thiết lập hành động cho nút áp dụng
        applyFilterButton.setOnClickListener(v -> {
            double selectedMaxPrice = minPrice + priceSeekBar.getProgress();

            // Gọi API hoặc thực hiện thao tác lọc sản phẩm theo khoảng giá
            fetchPetFood(name, minPrice, selectedMaxPrice, sortOption);
            popupWindow.dismiss(); // Đóng popup sau khi áp dụng lọc
        });
    }
    private void handleSort(SortPetFood sortOption) {
        // Implement sorting logic based on the selected sorting option
        switch (sortOption) {
            case PriceAscending:
                fetchPetFood(name, minPrice, maxPrice, SortPetFood.PriceAscending.getValue());
                break;
            case PriceDescending:
                fetchPetFood(name, minPrice, maxPrice, SortPetFood.PriceDescending.getValue());

                break;
            case QuantityAscending:
                fetchPetFood(name, minPrice, maxPrice, SortPetFood.QuantityAscending.getValue());

                break;
            case QuantityDescending:
                fetchPetFood(name, minPrice, maxPrice, SortPetFood.QuantityDescending.getValue());

                break;
            case CreateTimeAscending:
                fetchPetFood(name, minPrice, maxPrice, SortPetFood.CreateTimeAscending.getValue());

                break;
            case CreateTimeDescending:
                fetchPetFood(name, minPrice, maxPrice, SortPetFood.CreateTimeDescending.getValue());

                break;
        }
    }

}
