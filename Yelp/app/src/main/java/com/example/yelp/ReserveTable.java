package com.example.yelp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

public class ReserveTable extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    public int rowNum = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve_table);
        ImageView back = (ImageView) findViewById(R.id.imageView7);
        back.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        });
        SharedPreferences pref = this.getSharedPreferences("MyPref", MODE_PRIVATE);
        onSharedPreferenceChanged(pref, "row");


    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        TableLayout tb = (TableLayout) findViewById(R.id.table);
        ArrayList<String> rows = new ArrayList<>();
        String row = sharedPreferences.getString("row", "error BUT FUCK PAPA");
        if (row != "error BUT FUCK PAPA") {
            TextView noResult = (TextView) findViewById(R.id.textView39);
            noResult.setVisibility(View.INVISIBLE);
            if (row.contains("-")){
                String[] r = row.split("-");
                for (String x : r) {
                    rows.add(x);
                }
            } else {
                rows.add(row);
            }
            int i = 1;
            for (String r : rows) {
                TableRow tbrow = (TableRow) new TableRow(this);
                String[] Info = r.split("_");
                ArrayList<String> Infos =  new ArrayList<String>(Arrays.asList(Info));

                // index
                TextView tv0 = (TextView) new TextView(this);
                tv0.setText("" + i);
                i++;
                tv0.setTextColor(Color.BLACK);
                tv0.setGravity(Gravity.CENTER);
                tv0.setWidth(100);

                //name
                TextView tv1 = (TextView) new TextView(this);
                tv1.setText(Infos.get(0));
                tv1.setTextColor(Color.BLACK);
                tv1.setGravity(Gravity.CENTER);
                tv1.setWidth(250);

                //date
                TextView tv2 = (TextView) new TextView(this);
                tv2.setText(Infos.get(1));
                tv2.setTextColor(Color.BLACK);
                tv2.setGravity(Gravity.CENTER);
                tv2.setWidth(250);

                //time
                TextView tv3 = (TextView) new TextView(this);
                tv3.setText(Infos.get(2));
                tv3.setTextColor(Color.BLACK);
                tv3.setGravity(Gravity.CENTER);
                tv3.setWidth(250);

                //email
                TextView tv4 = (TextView) new TextView(this);
                tv4.setText(Infos.get(3));
                tv4.setTextColor(Color.BLACK);
                tv4.setGravity(Gravity.CENTER);
                tv4.setWidth(250);

                tbrow.addView(tv0);
                tbrow.addView(tv1);
                tbrow.addView(tv2);
                tbrow.addView(tv3);
                tbrow.addView(tv4);

                tb.addView(tbrow);


            }
        } else {
            TextView noResult = (TextView) findViewById(R.id.textView39);
            noResult.setVisibility(View.VISIBLE);

        }
    }
}