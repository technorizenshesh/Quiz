package com.my.quiz.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.my.quiz.R;
import com.my.quiz.fragments.CalanderFragment;
import com.my.quiz.fragments.HomeFragment;
import com.my.quiz.fragments.ListFragment;
import com.my.quiz.fragments.ProfileFragment;

public class HomeAct extends AppCompatActivity {

   public static BottomNavigationView navView;
    NavController navController;
    ImageView ivLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ivLocation = findViewById(R.id.ivLocation);
        navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_calander,R.id.navigation_list,R.id.navigation_profile)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        ivLocation.setOnClickListener(view ->
                {
                    navController.navigate(R.id.searchFragment);
//                    navView.getMenu().setGroupCheckable(0, false, true);
                }
                );




//        navView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                navView.getMenu().setGroupCheckable(0, true, true);
//                switch (item.getItemId())
//                {
//                    case R.id.navigation_home : navController.navigate(R.id.navigation_home);
//                        break;
//                    case R.id.navigation_calander : navController.navigate(R.id.navigation_calander);
//                        break;
//                    case R.id.navigation_list : navController.navigate(R.id.navigation_list);
//                        break;
//                    case R.id.navigation_profile : navController.navigate(R.id.navigation_profile);
//                }
//                return true;
//            }
//        });



    }
}