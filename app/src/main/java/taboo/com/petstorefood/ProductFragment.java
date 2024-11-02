package taboo.com.petstorefood;

import android.content.Context;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
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
import taboo.com.petstorefood.adapter.ProductManagementAdapter;
import taboo.com.petstorefood.model.entity.Category;
import taboo.com.petstorefood.model.entity.PetFood;
import taboo.com.petstorefood.model.responseModel.ApiResponse;
import taboo.com.petstorefood.repository.CategoryRepository;
import taboo.com.petstorefood.repository.PetFoodRepository;
import taboo.com.petstorefood.service.CategoryService;
import taboo.com.petstorefood.service.PetFoodService;

public class ProductFragment extends Fragment {
    List<PetFood> petFoodList;
    List<Category> categories = new ArrayList<>();
    RecyclerView listItem;
    PetFoodService petFoodService;
    CategoryService categoryService;
    ProductManagementAdapter adapter;
    Button btnAdd;
    private Uri imageUri;
    private ActivityResultLauncher<String> imagePickerLauncher;
    public ProductFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product, container, false);
        petFoodService = PetFoodRepository.getPetFoodService();
        categoryService = CategoryRepository.getCategoryService();
        listItem = view.findViewById(R.id.productRecyclerView);
        btnAdd = view.findViewById(R.id.addButton);
        petFoodList = new ArrayList<>();
        adapter = new ProductManagementAdapter(requireActivity(), petFoodList, this);
        listItem.setAdapter(adapter);
        listItem.setLayoutManager(new LinearLayoutManager(requireActivity()));
        fetchPetFood();

        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                result -> {
                    if (result != null) {
                        imageUri = result;
                    }
                });

        LayoutInflater inflaterPopup = (LayoutInflater) requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupLayoutView = inflaterPopup.inflate(R.layout.popup_product, null);
        btnAdd.setOnClickListener(v -> showPopup(popupLayoutView, true, 0));

        return view;
    }

    private void fetchPetFood() {
        Call<ApiResponse<List<PetFood>>> call = petFoodService.getAllFood(null, null, null, 0);
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
    private void callApiPost(File file, String name, String description, double price, int quantity, String brand, UUID categoryId){
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        RequestBody namePart = RequestBody.create(MediaType.parse("text/plain"), name);
        RequestBody descriptionPart = RequestBody.create(MediaType.parse("text/plain"), description);
        RequestBody pricePart = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(price));
        RequestBody quantityPart = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(quantity));
        RequestBody brandPart = RequestBody.create(MediaType.parse("text/plain"), brand);
        RequestBody categoryIdPart = RequestBody.create(MediaType.parse("text/plain"), categoryId.toString());

        Call<ApiResponse<PetFood>> call = petFoodService.createFood(body, namePart, descriptionPart, pricePart, quantityPart, brandPart, categoryIdPart);
        Log.d("API_CALL", "Calling endpoint: " + call.request().url());
        call.enqueue(new Callback<ApiResponse<PetFood>>() {
            @Override
            public void onResponse(Call<ApiResponse<PetFood>> call, Response<ApiResponse<PetFood>> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Item created successfully", Toast.LENGTH_SHORT).show();
                    fetchPetFood();
                } else {
                    Toast.makeText(getContext(), "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<PetFood>> call, Throwable t) {
                Toast.makeText(getContext(), "Failed to create item: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void callApiPut(UUID id ,File file, String name, String description, double price, int quantity, String brand, UUID categoryId){
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        RequestBody idPart = RequestBody.create(MediaType.parse("text/plain"), id.toString());
        RequestBody namePart = RequestBody.create(MediaType.parse("text/plain"), name);
        RequestBody descriptionPart = RequestBody.create(MediaType.parse("text/plain"), description);
        RequestBody pricePart = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(price));
        RequestBody quantityPart = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(quantity));
        RequestBody brandPart = RequestBody.create(MediaType.parse("text/plain"), brand);
        RequestBody categoryIdPart = RequestBody.create(MediaType.parse("text/plain"), categoryId.toString());

        Call<ApiResponse<PetFood>> call = petFoodService.updateFood(body, idPart, namePart, descriptionPart, pricePart, quantityPart, brandPart, categoryIdPart);
        Log.d("API_CALL", "Calling endpoint: " + call.request().url());
        call.enqueue(new Callback<ApiResponse<PetFood>>() {
            @Override
            public void onResponse(Call<ApiResponse<PetFood>> call, Response<ApiResponse<PetFood>> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Item updated successfully", Toast.LENGTH_SHORT).show();
                    fetchPetFood();
                } else {
                    Toast.makeText(getContext(), "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<PetFood>> call, Throwable t) {
                Toast.makeText(getContext(), "Failed to create item: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void callApiDelete(int index){
        UUID id = petFoodList.get(index).getId();
        Call<ApiResponse<PetFood>> call = petFoodService.deleteFood(id);
        Log.d("API_CALL", "Calling endpoint: " + call.request().url());
        call.enqueue(new Callback<ApiResponse<PetFood>>() {
            @Override
            public void onResponse(Call<ApiResponse<PetFood>> call, Response<ApiResponse<PetFood>> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Item updated successfully", Toast.LENGTH_SHORT).show();
                    fetchPetFood();
                } else {
                    Toast.makeText(getContext(), "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<PetFood>> call, Throwable t) {
                Toast.makeText(getContext(), "Failed to create item: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void showPopup(View anchorView, boolean check, int index){
        View popupView = getLayoutInflater().inflate(R.layout.popup_product, null);
        PopupWindow popupWindow = new PopupWindow(popupView,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, true);
        popupWindow.showAtLocation(anchorView, android.view.Gravity.CENTER, 0, 0);
        if (categories.isEmpty()) {
            fetchAllCategories(popupView);
        } else {
            setupSpinner(popupView, categories);
        }

        EditText editTextName = popupView.findViewById(R.id.editTextName);
        EditText editTextDescription = popupView.findViewById(R.id.editTextDescription);
        EditText editTextPrice = popupView.findViewById(R.id.editTextPrice);
        EditText editTextQuantity = popupView.findViewById(R.id.editTextQuantity);
        EditText editTextBrand = popupView.findViewById(R.id.editTextBrand);
        Spinner spinnerCategoryId = popupView.findViewById(R.id.spinnerCategoryId);
        Button buttonSubmit = popupView.findViewById(R.id.buttonSubmit);
        Button buttonUploadImage = popupView.findViewById(R.id.buttonUploadImage);


        if(!check){
            int position = 0;
            editTextName.setText(petFoodList.get(index).getName());
            editTextDescription.setText(petFoodList.get(index).getDescription());
            editTextPrice.setText(petFoodList.get(index).getPrice()+"");
            editTextQuantity.setText(petFoodList.get(index).getQuantity()+"");
            editTextBrand.setText(petFoodList.get(index).getBrand());
                for (int i = 0; i < categories.size(); i++) {
                    if (categories.get(i).getId().equals(petFoodList.get(index).getCategory().getId())) {
                        position = i;
                        break;
                    }
                }
            spinnerCategoryId.setSelection(position);
        }

        buttonUploadImage.setOnClickListener(v -> openImageChooser());

        buttonSubmit.setOnClickListener(v -> {
            UUID id = petFoodList.get(index).getId();
            String name = editTextName.getText().toString();
            String description = editTextDescription.getText().toString();
            double price = Double.parseDouble(editTextPrice.getText().toString());
            int quantity = Integer.parseInt(editTextQuantity.getText().toString());
            String brand = editTextBrand.getText().toString();
            Category selectedCategory = (Category) spinnerCategoryId.getSelectedItem();
            String categoryId = selectedCategory.getId().toString();

            // Giả sử bạn có một file để gửi
            File file = createFileFromUri(imageUri); // Lấy đường dẫn từ URI

            if (!name.isEmpty() && !description.isEmpty() && quantity > 0 ) {
                if(!check){
                    callApiPut(id, file, name, description, price, quantity, brand, UUID.fromString(categoryId));

                }else{
                    callApiPost(file, name, description, price, quantity, brand, UUID.fromString(categoryId));

                }
                popupWindow.dismiss();
            } else {
                Toast.makeText(getContext(), "Please fill in all fields and select an image", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openImageChooser() {
        imagePickerLauncher.launch("image/*");
    }

    private File createFileFromUri(Uri uri) {
        File file = new File(getContext().getCacheDir(), "temp_image.jpg");
        try (InputStream inputStream = getContext().getContentResolver().openInputStream(uri);
             OutputStream outputStream = new FileOutputStream(file)) {

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return file;
    }

    private void fetchAllCategories(View view) {
        Call<ApiResponse<List<Category>>> call = categoryService.getAllCategories(null);
        call.enqueue(new Callback<ApiResponse<List<Category>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Category>>> call, Response<ApiResponse<List<Category>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                        categories.clear();
                        categories.addAll(response.body().getData());
                    setupSpinner(view, categories);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Category>>> call, Throwable t) {
                Toast.makeText(getContext(), "Failed to fetch categories: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupSpinner(View view, List<Category> categories) {
        Spinner spinnerCategoryId = view.findViewById(R.id.spinnerCategoryId);
        ArrayAdapter<Category> adapter = new ArrayAdapter<Category>(requireActivity(), android.R.layout.simple_spinner_item, categories) {
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                return view;
            }
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                return view;
            }
        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategoryId.setAdapter(adapter);
    }
}