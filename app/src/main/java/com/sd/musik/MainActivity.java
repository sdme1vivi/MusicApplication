package com.sd.musik;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.os.Environment;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class MainActivity extends AppCompatActivity {

    private String TAG_DARK = "dark_theme";
    SharedPreferences sharedPreferences;

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = this.getSharedPreferences(TAG_DARK, Context.MODE_PRIVATE);
        boolean dark = sharedPreferences.getBoolean(TAG_DARK, true);
        if (dark){
            setTheme(R.style.DarkTheme);
        }
        //else{
        //    setTheme(R.style.AppTheme);
        //}


        setContentView(R.layout.activity_main);



        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //FloatingActionButton fab = findViewById(R.id.fab);

        /*fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int it = item.getItemId();
        if (it == R.id.app_bar_switch){
            boolean b = sharedPreferences.getBoolean(TAG_DARK, false);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            if (b) editor.putBoolean(TAG_DARK, false);
            else editor.putBoolean(TAG_DARK, true);
            editor.apply();
            startActivity(new Intent(MainActivity.this, MainActivity.class));
            finish();

            Toast.makeText(this, "Switch theme button was pressed!", Toast.LENGTH_LONG).show();
        }
        if (it == R.id.action_settings){

            // -------------------------
            // before API 29

            String[] perm =  {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            ActivityCompat.requestPermissions(this, perm, 1);

            String path = Environment.getExternalStorageDirectory().getAbsolutePath();
            File file = new File(path+"Musik.doc");
            try {
                file.createNewFile();
                PrintWriter writer = new PrintWriter(new FileWriter(file));
                writer.println("Some text here!");
                writer.close();
            } catch (IOException e) {
                Toast.makeText(this, "File openning error!", Toast.LENGTH_LONG).show();

                e.printStackTrace();
            }

            // -------------------------
            // after API 29
            Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
            intent.setType("application/msword");
            //intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.putExtra(Intent.EXTRA_TITLE, "My Title");
            startActivityForResult(intent, 112);

        }
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
