package taboo.com.petstorefood;


import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String role = sharedPreferences.getString("Role", "USER");

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        if(role.equals("ADMIN")){
            bottomNavigationView.getMenu().clear();
            bottomNavigationView.inflateMenu(R.menu.bottom_nav_menu_admin);
            navigationView.inflateMenu(R.menu.drawer_menu_admin);
        }else{
            bottomNavigationView.getMenu().clear();
            bottomNavigationView.inflateMenu(R.menu.bottom_nav_menu);
            navigationView.inflateMenu(R.menu.drawer_menu);

        }
        navigationView.setNavigationItemSelectedListener(item -> {
            handleNavigationItemSelected(item.getItemId());
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            handleNavigationItemSelected(item.getItemId());
            return true;
        });

        // Default fragment
        if (savedInstanceState == null) {
            navigationView.setCheckedItem(R.id.nav_store);
            bottomNavigationView.setSelectedItemId(R.id.nav_store);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new StoreFragment()).commit();
        }
    }

    private void handleNavigationItemSelected(int itemId) {

        Fragment selectedFragment = null;
        if (itemId == R.id.nav_store) {
            selectedFragment = new StoreFragment();
        } else if (itemId == R.id.nav_profile) {
            selectedFragment = new ProfileFragment();
        }else if(itemId == R.id.nav_product){
            selectedFragment = new ProductFragment();
        }else if(itemId == R.id.nav_category){
            selectedFragment = new CategoryFragment();
        }else if(itemId == R.id.nav_cart){
            selectedFragment = new CartFragment();
        }

        if (selectedFragment != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, selectedFragment);
            transaction.commit();
        }
    }




}
