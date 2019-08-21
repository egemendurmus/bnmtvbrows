package com.mobilfabrikator.goaltivibrowser.favoripack;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.mobilfabrikator.goaltivibrowser.VodORStreamActivity;
import com.mobilfabrikator.goaltivibrowser.R;

public class FavoriVodOrSeriActivity extends AppCompatActivity {

    Button btnDizi, btnFilm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favori);
        btnDizi = findViewById(R.id.button_favori_diziler);
        btnFilm = findViewById(R.id.button_favori_filmler);

        btnFilm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VodORStreamActivity.vodType = "vod";
                Intent i = new Intent(FavoriVodOrSeriActivity.this, FavorilerimListActivity.class);
                i.putExtra("tipi", "film");
                startActivity(i);
            }
        });


        btnDizi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VodORStreamActivity.vodType = "series";
                Intent i = new Intent(FavoriVodOrSeriActivity.this, FavorilerimListActivity.class);
                i.putExtra("tipi", "seri");
                startActivity(i);
            }
        });

    }
}
