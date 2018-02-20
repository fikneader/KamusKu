package com.ngeartstudio.kamus.kamusku;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class DefinitionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_definition);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarDefinition);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        String kata = getIntent().getStringExtra("KATA");
        String pengertian = getIntent().getStringExtra("PENGERTIAN");

        TextView kataText = (TextView) findViewById(R.id.kataText);
        TextView pengertianText = (TextView) findViewById(R.id.pengertianText);

        kataText.setText(kata);
        pengertianText.setText(pengertian);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
