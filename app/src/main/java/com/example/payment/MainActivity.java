package com.example.payment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.payment.fragment.History;
import com.example.payment.fragment.Home;
import com.example.payment.fragment.Profil;
import com.example.payment.util.PrefManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
PrefManager prefManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadFragment(new Home());

        BottomNavigationView bottomNavigationView = findViewById(R.id.bn_main);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        prefManager = new PrefManager(this);
//        if (prefManager.getJenisUser().equalsIgnoreCase("1")){
//            MenuItem item = bottomNavigationView.getMenu().findItem(R.id.history);
//            item.setVisible(false);
//        }else {
//            MenuItem item = bottomNavigationView.getMenu().findItem(R.id.history);
//            item.setVisible(true);
//        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        Fragment fragment = null;
         switch (menuItem.getItemId()){
             case R.id.home:
                 fragment = new Home();
                 break;

             case R.id.history:
                 fragment = new History();
                 break;

             case R.id.profil:
                 fragment = new Profil();
                 break;

         }


        return loadFragment(fragment);
    }

    private boolean loadFragment(Fragment fragment) {
        if(fragment!=null){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fl_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
