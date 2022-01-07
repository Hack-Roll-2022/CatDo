package com.example.catdonotification;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private ImageButton setCat1, setCat2, setCat3, setCat4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void release(View view) {
        Toast.makeText(this, "Cat Released", Toast.LENGTH_SHORT).show();
    }

    public void select(View view) {
        dialogBuilder = new AlertDialog.Builder(this);
        final View popupView = getLayoutInflater().inflate(R.layout.popup, null);
        setCat1 = (ImageButton) popupView.findViewById(R.id.cat1);
        setCat2 = (ImageButton) popupView.findViewById(R.id.cat2);
        setCat3 = (ImageButton) popupView.findViewById(R.id.cat3);
        setCat4 = (ImageButton) popupView.findViewById(R.id.cat4);
        dialogBuilder.setView(popupView);
        dialog = dialogBuilder.create();
        dialog.show();

        setCat1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageView img = findViewById(R.id.imageView);
                img.setImageResource(R.drawable.cat1);
                dialog.hide();
            }
        });

        setCat2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageView img = findViewById(R.id.imageView);
                img.setImageResource(R.drawable.cat2);
                dialog.hide();
            }
        });

        setCat3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageView img = findViewById(R.id.imageView);
                img.setImageResource(R.drawable.cat3);
                dialog.hide();
            }
        });

        setCat4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageView img = findViewById(R.id.imageView);
                img.setImageResource(R.drawable.cat4);
                dialog.hide();
            }
        });
    }
}