package taboo.com.petstorefood;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final LatLng STORE_ADDRESS = new LatLng(10.759422, 106.681774);
    private static final int BASE_FEE = 10000;
    private static final int ADDITIONAL_FEE_PER_KM = 6000;

    private GoogleMap mMap;
    private Marker userMarker;
    private Button confirmLocationButton;
    private EditText searchField;
    ImageView searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        searchField = findViewById(R.id.searchField);
        searchButton = findViewById(R.id.searchIcon);
        confirmLocationButton = findViewById(R.id.confirmLocationButton);

        searchButton.setOnClickListener(v -> {
            String address = searchField.getText().toString();
            if (!address.isEmpty()) {
                LatLng location = getLocationFromAddress(address);
                if (location != null) {
                    updateMapLocation(location, "Searched Location");
                } else {
                    Toast.makeText(this, "Location not found", Toast.LENGTH_SHORT).show();
                }
            }
        });

        confirmLocationButton.setOnClickListener(v -> {
            if (userMarker != null) {
                LatLng selectedLocation = userMarker.getPosition();
                double distanceInKm = calculateDistanceInKm(STORE_ADDRESS, selectedLocation);
                int shippingFee = calculateShippingFee(distanceInKm);
                String address = getAddressFromLocation(selectedLocation);

                Toast.makeText(this, "Location confirmed. Shipping Fee: " + shippingFee, Toast.LENGTH_SHORT).show();
                SharedPreferences sharedPreferences = getSharedPreferences("OrderMap", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("ShippingFee", shippingFee);
                editor.putString("ShippingLocation", address);
                editor.apply();

                Intent intent = new Intent(MapActivity.this, OrderActivity.class);
                startActivity(intent);
                finish();

            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        Marker storeMarker = mMap.addMarker(new MarkerOptions().position(STORE_ADDRESS).title("Pet Store"));
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        String userAddress = sharedPreferences.getString("Address", null);

        LatLng defaultLocation;
        String markerTitle;

        if (userAddress != null) {
            // Try to get location from userAddress
            LatLng userLocation = getLocationFromAddress(userAddress);
            if (userLocation != null) {
                defaultLocation = userLocation;
                markerTitle = "Your Address";
            } else {
                // Fallback to FPTU TPHCM if userAddress location not found
                defaultLocation = new LatLng(10.841622, 106.810598); // FPTU TPHCM coordinates
                markerTitle = "Default";
            }
        } else {
            // Use FPTU TPHCM if no userAddress is available
            defaultLocation = new LatLng(10.841622, 106.810598);
            markerTitle = "Default";
        }

        updateMapLocation(defaultLocation, markerTitle);

        mMap.setOnMapClickListener(latLng -> {
            updateMapLocation(latLng, "Selected Location");
        });
    }


    private LatLng getLocationFromAddress(String strAddress) {
        Geocoder geocoder = new Geocoder(this);
        try {
            List<Address> addressList = geocoder.getFromLocationName(strAddress, 1);
            if (addressList != null && !addressList.isEmpty()) {
                Address address = addressList.get(0);
                return new LatLng(address.getLatitude(), address.getLongitude());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void updateMapLocation(LatLng latLng, String title) {
        if (userMarker != null) userMarker.remove();
        userMarker = mMap.addMarker(new MarkerOptions().position(latLng).title(title));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
    }

    private double calculateDistanceInKm(LatLng start, LatLng end) {
        double earthRadius = 6371.0; // Radius of the earth in km
        double latDiff = Math.toRadians(end.latitude - start.latitude);
        double lngDiff = Math.toRadians(end.longitude - start.longitude);
        double a = Math.sin(latDiff / 2) * Math.sin(latDiff / 2) +
                Math.cos(Math.toRadians(start.latitude)) * Math.cos(Math.toRadians(end.latitude)) *
                        Math.sin(lngDiff / 2) * Math.sin(lngDiff / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return earthRadius * c;
    }

    private int calculateShippingFee(double distanceInKm) {
        if (distanceInKm <= 1) {
            return BASE_FEE;
        } else {
            int additionalDistance = (int) Math.ceil(distanceInKm - 1);
            return BASE_FEE + (additionalDistance * ADDITIONAL_FEE_PER_KM);
        }
    }

    private String getAddressFromLocation(LatLng latLng) {
        Geocoder geocoder = new Geocoder(this);
        try {
            List<Address> addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (addressList != null && !addressList.isEmpty()) {
                Address address = addressList.get(0);
                return address.getAddressLine(0); // Lấy địa chỉ dòng đầu tiên
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null; // Trả về null nếu không tìm thấy địa chỉ
    }
}
