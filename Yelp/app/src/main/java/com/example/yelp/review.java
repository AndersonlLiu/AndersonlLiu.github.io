package com.example.yelp;

import static android.content.Intent.getIntent;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.yelp.databinding.FragmentReviewBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class review extends Fragment {
    private FragmentReviewBinding binding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Intent intent = getActivity().getIntent();
        String id= intent.getStringExtra("message_key");

        binding = FragmentReviewBinding.inflate(inflater, container, false);
        View view = inflater.inflate(R.layout.fragment_review, container, false);
        RequestQueue volleyQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        String url = "https://my-second-project-367205.uw.r.appspot.com/yelp/reviews/" + id;
        JsonObjectRequest stringRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray reviews = response.getJSONArray("reviews");


                            JSONObject review1 = reviews.getJSONObject(0);

                            TextView name1 = view.findViewById(R.id.textView4);
                            JSONObject user = review1.getJSONObject("user");
                            name1.setText(user.getString("name"));

                            String r1 = "Rating:";
                            String rate1 = review1.getString("rating");
                            r1 += rate1;
                            r1 += "/5";
                            TextView rating1 = view.findViewById(R.id.textView6);
                            rating1.setText(r1);

                            TextView text1 = view.findViewById(R.id.textView8);
                            text1.setText(review1.getString("text"));

                            TextView time1 = view.findViewById(R.id.textView10);
                            ArrayList<String> t1 = new ArrayList<>();
                            t1 = new ArrayList<>(Arrays.asList(review1.getString("time_created").split(" ", 2)));
                            String t11 = t1.get(0);
                            time1.setText(t11);


                            JSONObject review2 = reviews.getJSONObject(1);

                            TextView name2 = view.findViewById(R.id.textView12);
                            JSONObject user1 = review2.getJSONObject("user");
                            name2.setText(user1.getString("name"));

                            String r2 = "Rating:";
                            String rate2 = review2.getString("rating");
                            r2 += rate2;
                            r2 += "/5";
                            TextView rating2 = view.findViewById(R.id.textView14);
                            rating2.setText(r2);

                            TextView text2 = view.findViewById(R.id.textView15);
                            text2.setText(review2.getString("text"));

                            TextView time2 = view.findViewById(R.id.textView16);
                            ArrayList<String> t2 = new ArrayList<>();
                            t2 = new ArrayList<>(Arrays.asList(review2.getString("time_created").split(" ", 2)));
                            String t22 = t2.get(0);
                            time2.setText(t22);


                            JSONObject review3 = reviews.getJSONObject(2);

                            TextView name3 = view.findViewById(R.id.textView17);
                            JSONObject user2 = review3.getJSONObject("user");
                            name3.setText(user2.getString("name"));

                            String r3 = "Rating:";
                            String rate3 = review3.getString("rating");
                            r3 += rate3;
                            r3 += "/5";
                            TextView rating3 = view.findViewById(R.id.textView18);
                            rating3.setText(r3);

                            TextView text3 = view.findViewById(R.id.textView19);
                            text3.setText(review3.getString("text"));

                            TextView time3 = view.findViewById(R.id.textView20);
                            ArrayList<String> t3 = new ArrayList<>();
                            t3 = new ArrayList<>(Arrays.asList(review3.getString("time_created").split(" ", 2)));
                            String t33 = t3.get(0);
                            time3.setText(t33);




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


        return view;
    }


}
