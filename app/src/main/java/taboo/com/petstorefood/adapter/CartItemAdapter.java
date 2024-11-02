package taboo.com.petstorefood.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import taboo.com.petstorefood.CartFragment;
import taboo.com.petstorefood.R;
import taboo.com.petstorefood.model.entity.CartItem;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.ViewHolder>{
    private List<CartItem> list;
    public CartItemAdapter(List<CartItem> ist){
        this.list = list;
    }
    private Context context; // Khai báo biến context
    private CartFragment fragment;

    public CartItemAdapter(Context context, List<CartItem> list, CartFragment fragment) {
        this.context = context; // Khởi tạo context
        this.list = list;
        this.fragment = fragment;
    }
    @NonNull
    @Override
    public CartItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.list_cart, parent, false);
        CartItemAdapter.ViewHolder viewHolder = new CartItemAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemAdapter.ViewHolder holder, int position) {
        CartItem cartItem = list.get(position);
        holder.productName.setText(cartItem.getPetFood().getName());
        holder.productPrice.setText(cartItem.getPetFood().getPrice() + "VND");
        holder.productQuantity.setText(cartItem.getQuantity() + "");
        holder.img.setImageURI(cartItem.getPetFood().getImage());
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
        TextView productName;
        TextView productPrice;
        TextView productQuantity;
        ImageView img;
        ImageButton btnUpdate, btnDelete;
        public ViewHolder(@NonNull View view) {
            super(view);
            productName = (TextView) view.findViewById(R.id.productNameTextView);
            productPrice = (TextView) view.findViewById(R.id.productPriceTextView);
            productQuantity = (TextView) view.findViewById(R.id.productQuantityTextView);
            img = (ImageView) view.findViewById(R.id.productImageView);
            btnDelete = view.findViewById(R.id.removeItemButton);
            btnUpdate = view.findViewById(R.id.updateItemButton);
        }
    }

}
