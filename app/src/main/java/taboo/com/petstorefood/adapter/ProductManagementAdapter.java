package taboo.com.petstorefood.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import taboo.com.petstorefood.ProductFragment;
import taboo.com.petstorefood.R;
import taboo.com.petstorefood.model.entity.PetFood;

public class ProductManagementAdapter extends RecyclerView.Adapter<ProductManagementAdapter.ViewHolder> {
    private List<PetFood> list;
    public ProductManagementAdapter(List<PetFood> ist){
        this.list = list;
    }
    private Context context; // Khai báo biến context
    private ProductFragment fragment;
    public ProductManagementAdapter(Context context, List<PetFood> list, ProductFragment fragment) {
        this.context = context; // Khởi tạo context
        this.list = list;
        this.fragment = fragment;
    }
    @NonNull
    @Override
    public ProductManagementAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.list_product_management, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProductManagementAdapter.ViewHolder holder, int position) {
        PetFood food = list.get(position);
        holder.productName.setText(food.getName());
        holder.productPrice.setText(food.getPrice() + "VND");
        holder.productQuantity.setText(food.getQuantity()+"");
        holder.img.setImageURI(food.getImage());
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
        TextView productName, productQuantity,productPrice;
        ImageView img;
        Button btnAddToCart, btnUpdate, btnDelete;


        public ViewHolder(@NonNull View view) {
            super(view);
            productName = (TextView) view.findViewById(R.id.textProductName);
            productPrice = (TextView) view.findViewById(R.id.textPrice);
            productQuantity = (TextView) view.findViewById(R.id.textQuantity);
            img = (ImageView) view.findViewById(R.id.imageView);
            btnAddToCart = (Button) view.findViewById(R.id.addToCart);
            btnUpdate = (Button) view.findViewById(R.id.buttonUpdate);
            btnDelete = (Button) view.findViewById(R.id.buttonDelete);

        }
    }
}
