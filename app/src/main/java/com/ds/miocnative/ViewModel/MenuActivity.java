package com.ds.miocnative.ViewModel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.ds.miocnative.AuditionFragment;
import com.ds.miocnative.ChatFragment;
import com.ds.miocnative.CourseFragment;
import com.ds.miocnative.ProfileFragment;
import com.ds.miocnative.R;
import com.ds.miocnative.StudyFragment;
import com.ds.miocnative.service.UserService;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MenuActivity extends AppCompatActivity {

    private static long backPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        /*
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        NavController navController = Navigation.findNavController(this, R.id.fragment);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
        */


        final BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setSelectedItemId(R.id.menuProfile);
        //getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new ProfileFragment()).commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment newFragment = null;
                switch(item.getItemId()){
                    case R.id.menuProfile:
                        //startActivity(new Intent(getApplicationContext(), ProfileFragment.class));
                        //overridePendingTransition(0,0);
                        newFragment = new ProfileFragment();
                        break;
                    case R.id.menuCourse:
                        newFragment = new CourseFragment();
                        break;
                    case R.id.menuStudy:
                        newFragment = new StudyFragment();
                        break;
                    case R.id.menuChat:
                        newFragment = new ChatFragment();
                        break;
                    case R.id.menuAudition:
                        newFragment = new AuditionFragment();
                        break;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment, newFragment).commit();
                return true;
            }
        });

    }


    public void logout(){
        UserService.cleanUser(this);

        Intent closeIntent = new Intent();
        setResult(RESULT_CANCELED, closeIntent);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (backPressed + 2000 > System.currentTimeMillis()) {
            finish();
            System.exit(0);
        } else {
            Toast.makeText(getBaseContext(), "Нажмите еще раз для выхода", Toast.LENGTH_SHORT).show();
        }

        backPressed = System.currentTimeMillis();
    }
}