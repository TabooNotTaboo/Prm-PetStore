package taboo.com.petstorefood.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import taboo.com.petstorefood.CategoryFragment;
import taboo.com.petstorefood.R;
import taboo.com.petstorefood.model.entity.Category;

public class CategoryManagementAdapter extends RecyclerView.Adapter<CategoryManagementAdapter.ViewHolder>{
    private List<Category> list;
    public CategoryManagementAdapter(List<Category> ist){
        this.list = list;
    }
    private Context context; // Khai báo biến context
    private CategoryFragment fragment;
    public CategoryManagementAdapter(Context context, List<Category> list, CategoryFragment fragment) {
        this.context = context; // Khởi tạo context
        this.list = list;
        this.fragment = fragment;
    }
    @NonNull
    @Override
    public CategoryManagementAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.list_category_management, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryManagementAdapter.ViewHolder holder, int position) {
        Category category = list.get(position);
        holder.productName.setText(category.getName());
        holder.productDescription.setText(category.getDescription());
        holder.btnUpdate.setOnClickListener(v -> {
            fragment.showPopup(v, false, position);
        });
        holder.btnDelete.setOnClickListener(v -> {
            fragment.callApiDelete(position);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView productName, productDescription;
        Button  btnUpdate, btnDelete;


        public ViewHolder(@NonNull View view) {
            super(view);
            productName = (TextView) view.findViewById(R.id.textCategoryName);
            productDescription = (TextView) view.findViewById(R.id.textCategoryDescription);
            btnUpdate = (Button) view.findViewById(R.id.buttonUpdate);
            btnDelete = (Button) view.findViewById(R.id.buttonDelete);

        }
    }


}
