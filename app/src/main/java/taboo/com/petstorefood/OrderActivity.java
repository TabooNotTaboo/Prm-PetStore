package taboo.com.petstorefood;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import taboo.com.petstorefood.Api.CreateOrder;
import taboo.com.petstorefood.adapter.CartItemAdapter;
import taboo.com.petstorefood.adapter.OrderAdapter;

import taboo.com.petstorefood.model.entity.CartItem;
import taboo.com.petstorefood.model.entity.Category;
import taboo.com.petstorefood.model.entity.PetFood;
import taboo.com.petstorefood.model.entity.enums.PaymentMethod;
import taboo.com.petstorefood.model.entity.enums.SortPetFood;
import taboo.com.petstorefood.model.requestModel.OrderRequest;
import taboo.com.petstorefood.model.responseModel.ApiResponse;
import taboo.com.petstorefood.repository.CartItemRepository;
import taboo.com.petstorefood.repository.OrderRepository;
import taboo.com.petstorefood.service.CartItemService;
import taboo.com.petstorefood.service.CategoryService;
import taboo.com.petstorefood.service.OrderService;
import taboo.com.petstorefood.service.PetFoodService;
import vn.zalopay.sdk.Environment;
import vn.zalopay.sdk.ZaloPayError;
import vn.zalopay.sdk.ZaloPaySDK;
import vn.zalopay.sdk.listeners.PayOrderListener;

public class OrderActivity extends AppCompatActivity {
    TextView txtAddress, txtFee, txtTotal, txtSubtotal;
    Button btnOrder;
    List<CartItem> cartItemList;
    List<UUID> cartItemIds;
    Spinner spinner;
    RecyclerView listItem;
    CartItemService cartItemService;
    OrderService orderService;
    OrderAdapter adapter;
    String userId = "";
    int subTotal = 0;
    int fee;
    int paymentChoose;
    String address;
    Calendar shippingDate = Calendar.getInstance();;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order);
        txtAddress = findViewById(R.id.textViewAddress);
        txtFee = findViewById(R.id.textViewShippingFee);
        listItem = findViewById(R.id.rvCartItems);
        txtTotal = findViewById(R.id.total);
        txtSubtotal = findViewById(R.id.subTotal);
        spinner = findViewById(R.id.spinner);
        btnOrder = findViewById(R.id.orderButton);
        cartItemService = CartItemRepository.getCartService();
        orderService = OrderRepository.getOrderService();
        cartItemList = new ArrayList<>();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        ZaloPaySDK.init(2553, Environment.SANDBOX);
        SharedPreferences sharedPreferences = getSharedPreferences("OrderMap", Context.MODE_PRIVATE);
        fee = sharedPreferences.getInt("ShippingFee", 0);
        address = sharedPreferences.getString("ShippingLocation", null);
        DecimalFormat formatter = new DecimalFormat("#,###");
        txtFee.setText("Delivery: " + formatter.format(fee) + " VND");
        txtAddress.setText(address);
        adapter = new OrderAdapter(cartItemList, this);
        listItem.setAdapter(adapter);
        listItem.setLayoutManager(new LinearLayoutManager(this));
        fetchCart();

        String[] paymentMethods = new String[PaymentMethod.values().length];
        for (int i = 0; i < PaymentMethod.values().length; i++) {
            paymentMethods[i] = PaymentMethod.values()[i].name();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, paymentMethods);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedPaymentMethod = (String) adapterView.getItemAtPosition(i);
                paymentChoose = PaymentMethod.values()[i].getValue();
                Toast.makeText(getApplicationContext(), "Selected: " + selectedPaymentMethod, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    btnOrder.setOnClickListener(v -> {
        callApiPost();
    });

    }

    private void fetchCart() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        userId = sharedPreferences.getString("UserId", null);
        Call<ApiResponse<List<CartItem>>> call = cartItemService.getAllCarts(userId);
        Log.d("API_CALL", "Calling endpoint: " + call.request().url());
        call.enqueue(new Callback<ApiResponse<List<CartItem>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<CartItem>>> call, Response<ApiResponse<List<CartItem>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    DecimalFormat formatter = new DecimalFormat("#,###");
                    cartItemList.clear();
                    cartItemList .addAll(response.body().getData());
                    adapter.notifyDataSetChanged();
                    for (CartItem cartItem:
                            cartItemList) {
                        subTotal += cartItem.getPetFood().getPrice() * cartItem.getQuantity();
                    }
                    txtSubtotal.setText("Subtotal: " + formatter.format(subTotal) +"VND");
                    txtTotal.setText("Total: " + formatter.format(subTotal + fee)+ "VND");
                } else {
                    Toast.makeText(OrderActivity.this, "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<CartItem>>> call, Throwable t) {

            }
        });
    }


    public void callApiPost(){
        cartItemIds = cartItemList.stream()
                .map(CartItem::getId)
                .collect(Collectors.toList());
        String orderCode = "ORD-" + System.currentTimeMillis();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        shippingDate.add(Calendar.DAY_OF_YEAR, 1);
        OrderRequest request = new OrderRequest(userId, orderCode, fee, address, dateFormat.format(shippingDate.getTime()), paymentChoose, cartItemIds);
        Call<ApiResponse<OrderRequest>> call = orderService.CreateOrder(request);
        Log.d("API_CALL", "Calling endpoint: " + call.request().url());
        call.enqueue(new Callback<ApiResponse<OrderRequest>>() {
            @Override
            public void onResponse(Call<ApiResponse<OrderRequest>> call, Response<ApiResponse<OrderRequest>> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(OrderActivity.this, "Item order successfully", Toast.LENGTH_SHORT).show();
                    if (paymentChoose == 1){
                        String totalString = String.format("%d" , subTotal + fee);
                        zaloPayment(totalString, orderCode);
                    }else{
                        Toast.makeText(OrderActivity.this, "Successfully" , Toast.LENGTH_SHORT).show();
                        createSuccess(orderCode);
                        Intent intent = new Intent(OrderActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(OrderActivity.this, "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<OrderRequest>> call, Throwable t) {
                Toast.makeText(OrderActivity.this, "Failed to create item: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void createSuccess(String orderCode){
        Call<ApiResponse<OrderRequest>> call = orderService.CreateOrderSuccess(orderCode);
        Log.d("API_CALL", "Calling endpoint: " + call.request().url());
        call.enqueue(new Callback<ApiResponse<OrderRequest>>() {
            @Override
            public void onResponse(Call<ApiResponse<OrderRequest>> call, Response<ApiResponse<OrderRequest>> response) {
                if (response.isSuccessful()) {


                } else {
                    Toast.makeText(OrderActivity.this, "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<OrderRequest>> call, Throwable t) {
                Toast.makeText(OrderActivity.this, "Failed to create item: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void createFail(String orderCode){

        Call<ApiResponse<OrderRequest>> call = orderService.CreateOrderFail(orderCode);
        Log.d("API_CALL", "Calling endpoint: " + call.request().url());
        call.enqueue(new Callback<ApiResponse<OrderRequest>>() {
            @Override
            public void onResponse(Call<ApiResponse<OrderRequest>> call, Response<ApiResponse<OrderRequest>> response) {
                if (response.isSuccessful()) {

                } else {
                    Toast.makeText(OrderActivity.this, "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<OrderRequest>> call, Throwable t) {
                Toast.makeText(OrderActivity.this, "Failed to create item: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void zaloPayment(String totalString, String orderCode){
        CreateOrder orderApi = new CreateOrder();
        try {
            JSONObject data = orderApi.createOrder(totalString);
            //khi ấn thanh toán thì sẽ tự in ra 1 chuỗi token
            String code = data.getString("return_code");
            if (code.equals("1")) {
                String token = data.getString("zp_trans_token");
                ZaloPaySDK.getInstance().payOrder(OrderActivity.this, token, "demozpdk://app", new PayOrderListener() {
                    @Override
                    public void onPaymentSucceeded(String s, String s1, String s2) {
                        Log.d("ZaloPay", "Payment Succeeded: " + s); // Cập nhật log
                        createSuccess(orderCode);
                        Toast.makeText(OrderActivity.this, "Successfully" , Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(OrderActivity.this, MainActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onPaymentCanceled(String s, String s1) {
                        Log.d("ZaloPay", "Payment Canceled: " + s);
                        createFail(orderCode);

                        Intent intent = new Intent(OrderActivity.this, MainActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onPaymentError(ZaloPayError zaloPayError, String s, String s1) {
                        createFail(orderCode);

                        Log.d("ZaloPay", "Payment Error: " + zaloPayError.toString());
                        Intent intent = new Intent(OrderActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}