package taboo.com.petstorefood.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.List;

import taboo.com.petstorefood.CategoryFragment;
import taboo.com.petstorefood.R;
import taboo.com.petstorefood.model.entity.CartItem;
import taboo.com.petstorefood.model.entity.Category;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder>{
    private List<CartItem> list;
    public OrderAdapter(List<CartItem> ist){
        this.list = list;
    }
    private Context context; // Khai báo biến context

    public OrderAdapter(List<CartItem> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public OrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.list_order, parent, false);
        OrderAdapter.ViewHolder viewHolder = new OrderAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.ViewHolder holder, int position) {
        CartItem cartItem = list.get(position);
        DecimalFormat formatter = new DecimalFormat("#,###");
        holder.itemName.setText(cartItem.getPetFood().getName());
        holder.itemPrice.setText(formatter.format(cartItem.getPetFood().getPrice())+"VND");
        holder.itemQuantity.setText(cartItem.getQuantity()+"");

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView itemName, itemPrice, itemQuantity;


        public ViewHolder(@NonNull View view) {
            super(view);
            itemName = (TextView) view.findViewById(R.id.itemName);
            itemPrice = (TextView) view.findViewById(R.id.itemPrice);
            itemQuantity = (TextView) view.findViewById(R.id.itemQuantity);
        }
    }

}
