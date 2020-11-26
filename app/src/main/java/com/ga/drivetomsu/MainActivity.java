package com.ga.drivetomsu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
//display app descriptions
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void Start_Navigation(View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }
}