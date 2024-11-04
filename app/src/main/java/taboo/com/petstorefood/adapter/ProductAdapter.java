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

import java.text.DecimalFormat;
import java.util.List;

import taboo.com.petstorefood.ProductFragment;
import taboo.com.petstorefood.R;
import taboo.com.petstorefood.StoreFragment;
import taboo.com.petstorefood.model.entity.PetFood;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private List<PetFood> list;

    private Context context; // Khai báo biến context
    private StoreFragment fragment;
    public ProductAdapter(Context context, List<PetFood> list, StoreFragment fragment) {
        this.context = context; // Khởi tạo context
        this.list = list;
        this.fragment = fragment;
    }
    @NonNull
    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.list_product_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ViewHolder holder, int position) {
        DecimalFormat formatter = new DecimalFormat("#,###");

        PetFood food = list.get(position);
        holder.productName.setText(food.getName());
        holder.productPrice.setText(formatter.format(food.getPrice())  + "VND");
        holder.productQuantity.setText(food.getQuantity()+" units");
        holder.img.setImageURI(food.getImage());
        holder.btnAddToCart.setOnClickListener(v -> {
            fragment.showPopup(v, false, position);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView productName;
        TextView productPrice;
        TextView productQuantity;
        ImageView img;
        Button btnAddToCart;
        public ViewHolder(@NonNull View view) {
            super(view);
            productName = (TextView) view.findViewById(R.id.productName);
            productPrice = (TextView) view.findViewById(R.id.productPrice);
            productQuantity = view.findViewById(R.id.productQuantity);
            img = (ImageView) view.findViewById(R.id.productImage);
            btnAddToCart = (Button) view.findViewById(R.id.addToCart);
        }
    }

}
