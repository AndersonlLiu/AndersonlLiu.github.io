package com.example.yelp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Map;


public class DetailActivity<ViewPagerFragmentAdapter> extends AppCompatActivity {

    // Declare variables
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    String Id = "";
    String name = "";




    ViewPagerFragmentAdapter adapter;

    // array for tab labels
    private String[] labels = new String[]{"Business Details", "Map Locations", "Reviews"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        init();

        ImageView back = (ImageView) findViewById(R.id.imageView5);
        back.setOnClickListener(t -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        });

        Intent intent = getIntent();
        String id= intent.getStringExtra("message_key");
        Id = id;
        RequestQueue volleyQueue = Volley.newRequestQueue(getApplicationContext());
        String url = "https://my-second-project-367205.uw.r.appspot.com/yelp/" + id;
        JsonObjectRequest stringRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            System.out.println(response.getString("name"));
                            name = response.getString("name");
                            String url = response.getString("url");
                            TextView nameText = (TextView) findViewById(R.id.textView33);
                            nameText.setText(name);
                            ImageView facebook = (ImageView) findViewById(R.id.imageView3);
                            facebook.setOnClickListener(v -> {
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/sharer/sharer.php?u=" + url));
                                startActivity(browserIntent);
                            });
                            ImageView twitter = (ImageView) findViewById(R.id.imageView4);
                            twitter.setOnClickListener(s -> {
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/intent/tweet?text=Check Out" + name + " On Yelp" + "&url=" + url));
                                startActivity(browserIntent);
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.toString());
            }
        });

// Add the request to the RequestQueue.
        volleyQueue.add(stringRequest);


        // bind and set tabLayout to viewPager2 and set labels for every tab
        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
            tab.setText(labels[position]);
        }).attach();

        // set default position to 1 instead of default 0
        viewPager2.setCurrentItem(1, false);



    }
    private void init() {
        // initialize tabLayout
        tabLayout = findViewById(R.id.tab_layout);
        // initialize viewPager2
        viewPager2 = findViewById(R.id.view_pager2);
        // create adapter instance
        adapter = new ViewPagerFragmentAdapter(this);
        // set adapter to viewPager2
        viewPager2.setAdapter(adapter);

        // remove default elevation of actionbar
        //getSupportActionBar().setElevation(0);
    }
    private class ViewPagerFragmentAdapter extends FragmentStateAdapter {

        public ViewPagerFragmentAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        // return fragments at every position
        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    Intent intentId = new Intent(getApplicationContext(), busDetail.class);
                    intentId.putExtra("message_key", Id);
                    return new busDetail(); // calls fragment

                case 1:
                    Intent intentId1 = new Intent(getApplicationContext(), MapsFragment.class);
                    intentId1.putExtra("message_key", Id);
                    return new MapsFragment(); // calls fragment

                case 2:
                    Intent intentId2 = new Intent(getApplicationContext(), review.class);
                    intentId2.putExtra("message_key", Id);
                    return new review(); // chats fragment
            }
            return new busDetail(); //chats fragment
        }

        // return total number of tabs in our case we have 3
        @Override
        public int getItemCount() {
            return labels.length;
        }
    }
}

