package com.example.meinuniverwalter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {



    Button buttonAnzeigeFaecher;
    public ArrayList<String> meineFächer;
    public SharedPreferences sharedPreferences;
    public static final String mypreferenceFach = "mypref";
    public static File saveDir = null;
    public static Path path = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        saveDir = getFilesDir();
        path = saveDir.toPath();
        setContentView(R.layout.activity_main);


        meineFächer = new ArrayList<>();

        buttonAnzeigeFaecher = findViewById(R.id.buttonFaecher);


        sharedPreferences = getSharedPreferences(mypreferenceFach,
                Context.MODE_PRIVATE);


/*
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
 */


        givePermission();



        buttonAnzeigeFaecher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ViewFaecher.class);
                startActivity(i);


            }
        });


    }
    @AfterPermissionGranted(123)
    public void givePermission() {
        String[] perms = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) {

            //toDo


        } else {
            EasyPermissions.requestPermissions(this, "Möchte die PDF öffenen", 123, perms);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {

        }
    }


    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }


}




