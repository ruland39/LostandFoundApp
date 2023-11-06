package com.lostandfoundapp;

import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.core.view.WindowCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.lostandfoundapp.databinding.ActivityFeatureBinding;

public class FeatureActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityFeatureBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityFeatureBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}