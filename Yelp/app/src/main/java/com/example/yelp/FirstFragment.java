package com.example.yelp;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.yelp.databinding.FragmentFirstBinding;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    private JSONArray responseArray;
    private String distance_string = "1.0";



    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        RequestQueue volleyQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        binding = FragmentFirstBinding.inflate(inflater, container, false);
        View view = inflater.inflate(R.layout.fragment_first, container, false);
        ArrayList<String> fruits = new ArrayList<String>();


        AutoCompleteTextView actv = view.findViewById(R.id.keyword);
        TextView category = view.findViewById(R.id.textView);
        category.setText(Html.fromHtml("Category <span style=\"color:red\">*</span>"));
        actv.setHint(Html.fromHtml("Keyword <span style=\"color:red\">*</span>"));

        actv.addTextChangedListener(new TextWatcher()  {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void afterTextChanged(Editable s)  {
                if (actv.getText().toString().length() > 0) {
                    String auto = actv.getText().toString();
                    String autoURL = "https://my-second-project-367205.uw.r.appspot.com/yelp/search/" + auto;
                    JsonObjectRequest autoRequest = new JsonObjectRequest(
                            Request.Method.GET,
                            autoURL,
                            null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    fruits.clear();
                                    try {
                                        System.out.println("word");
                                        JSONArray cat = response.getJSONArray("categories");
                                        for (int i = 0; i < cat.length(); i++) {
                                            JSONObject c = cat.getJSONObject(i);
                                            String title = c.getString("title");
                                            fruits.add(title);
                                        }
                                        JSONArray terms = response.getJSONArray("terms");
                                        for (int i = 0; i < terms.length(); i++) {
                                            JSONObject t = terms.getJSONObject(i);
                                            String term = t.getString("text");
                                            fruits.add(term);
                                        }
                                        System.out.println(fruits);
                                        ArrayAdapter<String> adapterAuto = new ArrayAdapter<String>
                                                (getContext(), android.R.layout.select_dialog_item, fruits);
                                        actv.setThreshold(1);//will start working from first character
                                        actv.setAdapter(adapterAuto);//setting the adapter data into the AutoCompleteTextView

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
                    volleyQueue.add(autoRequest);
                }
            }
        });



        Spinner spinner= null;
        spinner = view.findViewById(R.id.category);
        ArrayAdapter<CharSequence>adapter=ArrayAdapter.createFromResource(this.getContext(), R.array.languages, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);


        Button buttonSubmit = view.findViewById(R.id.button);
        Button buttonClear = view.findViewById(R.id.button2);
        CheckBox checkBox = view.findViewById(R.id.checkBox);
        ImageView reserveTable = view.findViewById(R.id.imageView2);
        reserveTable.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getActivity().getApplicationContext(), ReserveTable.class);
                startActivity(intent1);
            }
        });

        String autoApi = "https://ipinfo.io/?token=925c24435c3495";
        EditText locationText = view.findViewById(R.id.location);
        locationText.setHint(Html.fromHtml("Location <span style=\"color:red\">*</span>"));
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (checkBox.isChecked()){
                    locationText.setVisibility(View.INVISIBLE);
                } else {
                    locationText.setVisibility(View.VISIBLE);
                }
            }
        });




        buttonSubmit.setOnClickListener(v -> {
            EditText keywordText = view.findViewById(R.id.keyword);
            EditText DistanceText = view.findViewById(R.id.distance);
            if (keywordText.getText().toString().length() <= 0) {
                keywordText.setError("This field is required");
            } else {
                keywordText.setError(null);
            }
            if (locationText.getText().toString().length() <= 0) {
                locationText.setError("This field is required");
            } else {
                locationText.setError(null);
            }
            if (DistanceText.getText().toString().length() <= 0) {
                DistanceText.setError("This field is required");
            } else {
                distance_string = DistanceText.getText().toString();
                DistanceText.setError(null);
            }
            String keyword = keywordText.getText().toString();


            double distance_double = Double.parseDouble(distance_string) * 1609.344;
            int distance_int = (int) distance_double;
            String distance = distance_int + "";
            System.out.println(distance);
            String location = locationText.getText().toString();
            Spinner mySpinner = view.findViewById(R.id.category);
            String cat = mySpinner.getSelectedItem().toString();
            if (checkBox.isChecked()){
                System.out.println("check");
                JsonObjectRequest myRequest = new JsonObjectRequest(
                        Request.Method.GET,
                        autoApi,
                        null,
                        // lambda function for handling the case
                        // when the HTTP request succeeds
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    String loc = response.getString("loc");
                                    String[] l = loc.split(",");
                                    String lat = l[0];
                                    String lng = l[1];
                                    String searchApi = "https://my-second-project-367205.uw.r.appspot.com/yelp/table/" + lat + "/" + lng + "/" + keyword + "/" + cat + "/" + distance;
                                    System.out.println(searchApi);
                                    JsonArrayRequest busRequest = new JsonArrayRequest(
                                            Request.Method.GET,
                                            searchApi,
                                            null,
                                            // lambda function for handling the case
                                            // when the HTTP request succeeds
                                            new Response.Listener<JSONArray>() {
                                                @Override
                                                public void onResponse(JSONArray response) {
                                                    System.out.println(response);
                                                    if (response.length() != 0){
                                                        TextView noResult = view.findViewById(R.id.textView24);
                                                        noResult.setVisibility(View.INVISIBLE);
                                                        responseArray = response;
                                                        TableLayout TableLayout = view.findViewById(R.id.table);

                                                        for (int i = 0; i < responseArray.length(); i++) {
                                                            TableRow tbrow = new TableRow(getContext());
                                                            JSONObject jsonObject = null;
                                                            try {
                                                                jsonObject = responseArray.getJSONObject(i);
                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }
                                                            String id = "";
                                                            try {
                                                                id = jsonObject.getString("id");
                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }
                                                            TextView tv0 = new TextView(getContext());
                                                            int num = i + 1;
                                                            tv0.setText("" + num);
                                                            tv0.setTextColor(Color.BLACK);
                                                            tv0.setGravity(Gravity.CENTER);
                                                            tv0.setWidth(100);
                                                            String finalId = id;
                                                            tv0.setOnClickListener(new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View v) {
                                                                    showDetail(finalId);
                                                                }
                                                            });
                                                            String imageurl = null;
                                                            try {
                                                                imageurl = jsonObject.getString("image_url");
                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }
                                                            System.out.println(imageurl);
                                                            ImageView tv1 = new ImageView(getContext());
                                                            tv1.setOnClickListener(new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View v) {
                                                                    showDetail(finalId);
                                                                }
                                                            });
                                                            Picasso.get()
                                                                    .load(imageurl)
                                                                    .resize(300, 200)
                                                                    .into(tv1);
                                                            TextView tv2 = new TextView(getContext());
                                                            String name = "";
                                                            try {
                                                                name = jsonObject.getString("name");
                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }
                                                            tv2.setText(name);
                                                            tv2.setTextColor(Color.BLACK);
                                                            tv2.setGravity(Gravity.CENTER);
                                                            tv2.setWidth(300);
                                                            tv2.setOnClickListener(new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View v) {
                                                                    showDetail(finalId);
                                                                }
                                                            });
                                                            TextView tv3 = new TextView(getContext());
                                                            String rating = "";
                                                            try {
                                                                rating = jsonObject.getString("rating");
                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }
                                                            tv3.setText(rating);
                                                            tv3.setTextColor(Color.BLACK);
                                                            tv3.setGravity(Gravity.CENTER);
                                                            tv3.setWidth(250);
                                                            tv3.setOnClickListener(new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View v) {
                                                                    showDetail(finalId);
                                                                }
                                                            });
                                                            TextView tv4 = new TextView(getContext());
                                                            float distance = 0;
                                                            try {
                                                                distance = BigDecimal.valueOf(jsonObject.getDouble("distance")).floatValue();
                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }
                                                            float distance_mile = (float) (distance * 0.000621371);
                                                            int distance_final = (int) distance_mile;
                                                            tv4.setText(String.valueOf(distance_final));
                                                            tv4.setTextColor(Color.BLACK);
                                                            tv4.setGravity(Gravity.CENTER);
                                                            tv4.setWidth(150);
                                                            tv4.setOnClickListener(new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View v) {
                                                                    showDetail(finalId);
                                                                }
                                                            });


                                                            tbrow.addView(tv0);
                                                            tbrow.addView(tv1);
                                                            tbrow.addView(tv2);
                                                            tbrow.addView(tv3);
                                                            tbrow.addView(tv4);

                                                            View v = new View(getContext());
                                                            v.setLayoutParams(new LinearLayout.LayoutParams(
                                                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                                                    5
                                                            ));
                                                            v.setBackgroundColor(Color.parseColor("#000000"));
                                                            TableLayout.addView(tbrow);
                                                            TableLayout.addView(v);

                                                        }

                                                    } else {
                                                        TextView noResult = view.findViewById(R.id.textView24);
                                                        noResult.setVisibility(View.VISIBLE);
                                                    }
                                                }
                                            },new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            System.out.println(error.toString());
                                        }
                                    });
                                    volleyQueue.add(busRequest);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        },new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error.toString());
                    }
                });
                volleyQueue.add(myRequest);
            } else {
                String googleApi = "https://my-second-project-367205.uw.r.appspot.com/google/" + location;
                JsonObjectRequest googleRequest = new JsonObjectRequest(
                        Request.Method.GET,
                        googleApi,
                        null,
                        // lambda function for handling the case
                        // when the HTTP request succeeds
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    JSONArray result = response.getJSONArray("results");
                                    String lat = result.getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getString("lat");
                                    String lng = result.getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getString("lng");
                                    String searchApi = "https://my-second-project-367205.uw.r.appspot.com/yelp/table/" + lat + "/" + lng + "/" + keyword + "/" + cat + "/" + distance;
                                    System.out.println(searchApi);
                                    JsonArrayRequest busRequest = new JsonArrayRequest(
                                            Request.Method.GET,
                                            searchApi,
                                            null,
                                            // lambda function for handling the case
                                            // when the HTTP request succeeds
                                            new Response.Listener<JSONArray>() {
                                                @Override
                                                public void onResponse(JSONArray response) {
                                                    System.out.println(response);
                                                    if (response.length() != 0) {
                                                        TextView noResult = view.findViewById(R.id.textView24);
                                                        noResult.setVisibility(View.INVISIBLE);
                                                        responseArray = response;
                                                        TableLayout TableLayout = view.findViewById(R.id.table);

                                                        for (int i = 0; i < responseArray.length(); i++) {
                                                            TableRow tbrow = new TableRow(getContext());
                                                            JSONObject jsonObject = null;
                                                            try {
                                                                jsonObject = responseArray.getJSONObject(i);
                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }
                                                            String id = "";
                                                            try {
                                                                id = jsonObject.getString("id");
                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }
                                                            TextView tv0 = new TextView(getContext());
                                                            int num = i + 1;
                                                            tv0.setText("" + num);
                                                            tv0.setTextColor(Color.BLACK);
                                                            tv0.setGravity(Gravity.CENTER);
                                                            tv0.setWidth(100);
                                                            String finalId = id;
                                                            tv0.setOnClickListener(new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View v) {
                                                                    showDetail(finalId);
                                                                }
                                                            });
                                                            String imageurl = null;
                                                            try {
                                                                imageurl = jsonObject.getString("image_url");
                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }
                                                            ImageView tv1 = new ImageView(getContext());
                                                            Picasso.get()
                                                                    .load(imageurl)
                                                                    .resize(300, 200)
                                                                    .into(tv1);
                                                            tv1.setOnClickListener(new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View v) {
                                                                    showDetail(finalId);
                                                                }
                                                            });
                                                            TextView tv2 = new TextView(getContext());
                                                            String name = "";
                                                            try {
                                                                name = jsonObject.getString("name");
                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }
                                                            tv2.setText(name);
                                                            tv2.setTextColor(Color.BLACK);
                                                            tv2.setGravity(Gravity.CENTER);
                                                            tv2.setWidth(300);
                                                            tv2.setOnClickListener(new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View v) {
                                                                    showDetail(finalId);
                                                                }
                                                            });
                                                            TextView tv3 = new TextView(getContext());
                                                            String rating = "";
                                                            try {
                                                                rating = jsonObject.getString("rating");
                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }
                                                            tv3.setText(rating);
                                                            tv3.setTextColor(Color.BLACK);
                                                            tv3.setGravity(Gravity.CENTER);
                                                            tv3.setWidth(250);
                                                            tv3.setOnClickListener(new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View v) {
                                                                    showDetail(finalId);
                                                                }
                                                            });
                                                            TextView tv4 = new TextView(getContext());
                                                            float distance = 0;
                                                            try {
                                                                distance = BigDecimal.valueOf(jsonObject.getDouble("distance")).floatValue();
                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }
                                                            float distance_mile = (float) (distance * 0.000621371);
                                                            int distance_final = (int) distance_mile;
                                                            tv4.setText(String.valueOf(distance_final));
                                                            tv4.setTextColor(Color.BLACK);
                                                            tv4.setGravity(Gravity.CENTER);
                                                            tv4.setWidth(150);
                                                            tv4.setOnClickListener(new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View v) {
                                                                    showDetail(finalId);
                                                                }
                                                            });


                                                            tbrow.addView(tv0);
                                                            tbrow.addView(tv1);
                                                            tbrow.addView(tv2);
                                                            tbrow.addView(tv3);
                                                            tbrow.addView(tv4);

                                                            View v = new View(getContext());
                                                            v.setLayoutParams(new LinearLayout.LayoutParams(
                                                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                                                    5
                                                            ));
                                                            v.setBackgroundColor(Color.parseColor("#000000"));
                                                            TableLayout.addView(tbrow);
                                                            TableLayout.addView(v);

                                                        }

                                                    } else {
                                                        TextView noResult = view.findViewById(R.id.textView24);
                                                        noResult.setVisibility(View.VISIBLE);
                                                    }
                                                }
                                            },new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            System.out.println(error.toString());
                                        }
                                    });
                                    volleyQueue.add(busRequest);


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }


                        },new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error.toString());
                    }
                });
                volleyQueue.add(googleRequest);
            }


        });

        buttonClear.setOnClickListener(s -> {
            TextView noResult = view.findViewById(R.id.textView24);
            noResult.setVisibility(View.INVISIBLE);
            EditText keywordText = view.findViewById(R.id.keyword);
            EditText DistanceText = view.findViewById(R.id.distance);
            keywordText.getText().clear();
            DistanceText.getText().clear();
            locationText.getText().clear();
            Spinner mySpinner = view.findViewById(R.id.category);
            mySpinner.setSelection(0);
            TableLayout TableLayout = view.findViewById(R.id.table);
            TableLayout.removeViews(0, Math.max(0, TableLayout.getChildCount() - 1));
            responseArray = new JSONArray(new ArrayList<String>());
            CheckBox chk1 = view.findViewById(R.id.checkBox);
            if(chk1.isChecked()){
                chk1.toggle();
            }
            //SharedPreferences pref = getContext().getSharedPreferences("MyPref", MODE_PRIVATE);
            //pref.edit().remove("row").commit();

        });
        return view;

    }

    public void showDetail(String finalId) {
        Intent intent = new Intent(getActivity().getApplicationContext(), DetailActivity.class);
        intent.putExtra("message_key", finalId);
        startActivity(intent);
    }




    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);




    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}