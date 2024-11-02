package taboo.com.petstorefood;

import android.content.Context;
import android.os.Bundle;

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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import taboo.com.petstorefood.adapter.CategoryManagementAdapter;
import taboo.com.petstorefood.model.entity.Category;
import taboo.com.petstorefood.model.responseModel.ApiResponse;
import taboo.com.petstorefood.repository.CategoryRepository;
import taboo.com.petstorefood.service.CategoryService;


public class CategoryFragment extends Fragment {


    List<Category> categories;
    CategoryService categoryService;
    RecyclerView listItem;
    CategoryManagementAdapter adapter;
    Button btnAdd;

    public CategoryFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        btnAdd = view.findViewById(R.id.addButton);
        listItem = view.findViewById(R.id.categoryRecyclerView);
        categoryService = CategoryRepository.getCategoryService();
        categories = new ArrayList<>();
        adapter = new CategoryManagementAdapter(requireActivity(), categories, this);
        listItem.setAdapter(adapter);
        listItem.setLayoutManager(new LinearLayoutManager(requireActivity()));
        fetchAllCategories();
        LayoutInflater inflaterPopup = (LayoutInflater) requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupLayoutView = inflaterPopup.inflate(R.layout.popup_product, null);
        btnAdd.setOnClickListener(v -> showPopup(popupLayoutView, true, 0));

        return view;
    }

    public void showPopup(View anchorView, boolean check, int index){
        View popupView = getLayoutInflater().inflate(R.layout.popup_category, null);
        PopupWindow popupWindow = new PopupWindow(popupView,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, true);
        popupWindow.showAtLocation(anchorView, android.view.Gravity.CENTER, 0, 0);


        EditText editTextName = popupView.findViewById(R.id.editTextName);
        EditText editTextDescription = popupView.findViewById(R.id.editTextDescription);
        Button buttonSubmit = popupView.findViewById(R.id.buttonSubmit);


        if(!check){
            editTextName.setText(categories.get(index).getName());
            editTextDescription.setText(categories.get(index).getDescription());

        }


        buttonSubmit.setOnClickListener(v -> {
            UUID id = categories.get(index).getId();
            String name = editTextName.getText().toString();
            String description = editTextDescription.getText().toString();

            Category category = new Category(id, name, description);
            if (!name.isEmpty()) {
                if(!check){
                    callApiPut(category);
                }else{
                    callApiPost(category);

                }
                popupWindow.dismiss();
            } else {
                Toast.makeText(getContext(), "Please fill in all fields and select an image", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchAllCategories() {
        Call<ApiResponse<List<Category>>> call = categoryService.getAllCategories();
        call.enqueue(new Callback<ApiResponse<List<Category>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Category>>> call, Response<ApiResponse<List<Category>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    categories.clear();
                    categories.addAll(response.body().getData());
                    adapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Category>>> call, Throwable t) {
                Toast.makeText(getContext(), "Failed to fetch categories: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void callApiPost(Category category) {
        Call<ApiResponse<Category>> call = categoryService.createCategory(category);
        call.enqueue(new Callback<ApiResponse<Category>>() {
            @Override
            public void onResponse(Call<ApiResponse<Category>> call, Response<ApiResponse<Category>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    fetchAllCategories();

                    Toast.makeText(requireActivity(), "Create Category Successfully", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(requireActivity(), "Create Category Fail", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<ApiResponse<Category>> call, Throwable t) {
                Log.e("API Failure", t.getMessage());
            }
        });
    }

    private void callApiPut(Category category) {
        Call<ApiResponse<Category>> call = categoryService.updateCategory(category);
        call.enqueue(new Callback<ApiResponse<Category>>() {
            @Override
            public void onResponse(Call<ApiResponse<Category>> call, Response<ApiResponse<Category>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    fetchAllCategories();
                    Toast.makeText(requireActivity(), "Update Category Successfully", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(requireActivity(), "Update Category Fail", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<ApiResponse<Category>> call, Throwable t) {
                Log.e("API Failure", t.getMessage());
            }
        });
    }

    public void callApiDelete(int index) {
        UUID id = categories.get(index).getId();
        Call<ApiResponse<Category>> call = categoryService.deleteCategory(id);
        call.enqueue(new Callback<ApiResponse<Category>>() {
            @Override
            public void onResponse(Call<ApiResponse<Category>> call, Response<ApiResponse<Category>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    fetchAllCategories();

                    Toast.makeText(requireActivity(), "Delete Category Successfully", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(requireActivity(), "Delete Category Fail", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<ApiResponse<Category>> call, Throwable t) {
                Log.e("API Failure", t.getMessage());
            }
        });
    }
}