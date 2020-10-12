package com.example.pengtok;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.pengtok.fragment.PeopleFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction().replace(R.id.mainActivity_framelayout,new PeopleFragment()).commit();
    }
}