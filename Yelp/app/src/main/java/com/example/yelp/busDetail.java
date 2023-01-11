package com.example.yelp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.regex.Pattern;

public class busDetail extends Fragment {
    private FragmentReviewBinding binding;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    EditText date;
    DatePickerDialog datePickerDialog;
    TimePickerDialog picker;
    String name;
    int hourCheck;
    public static final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    );

    private boolean checkEmail(String email) {
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Intent intent = getActivity().getIntent();
        String id= intent.getStringExtra("message_key");
        View view = inflater.inflate(R.layout.fragment_bus_detail, container, false);

        binding = FragmentReviewBinding.inflate(inflater, container, false);


        RequestQueue volleyQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
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
                            //name
                            name = response.getString("name");

                            //address
                            TextView address = view.findViewById(R.id.address);
                            String add = "";
                            JSONArray a = response.getJSONObject("location").getJSONArray("display_address");
                            add += a.getString(0);
                            add += ", ";
                            for (int i = 1; i < a.length(); i++){
                                add += a.getString(i);
                            }
                            address.setText(add);

                            //price
                            TextView price = view.findViewById(R.id.price);
                            price.setText(response.getString("price"));

                            //phone
                            TextView phone = view.findViewById(R.id.phonenumber);
                            phone.setText(response.getString("display_phone"));

                            //status
                            TextView status = view.findViewById((R.id.status));
                            JSONArray hours = response.getJSONArray("hours");
                            Boolean isOpen = hours.getJSONObject(0).getBoolean("is_open_now");
                            if (isOpen) {
                                status.setText("Open Now");
                                status.setTextColor(Color.parseColor("#AAFF00"));
                            } else {
                                status.setText("Closed");
                                status.setTextColor(Color.parseColor("#FF0000"));
                            }

                            //category
                            TextView cat = view.findViewById(R.id.category);
                            String category = "";
                            JSONArray c = response.getJSONArray("categories");
                            JSONObject y = c.getJSONObject(0);
                            category += y.getString("title");
                            for (int i = 1; i < c.length(); i++) {
                                JSONObject x = c.getJSONObject(i);
                                category += " | ";
                                category += x.getString("title");
                            }
                            cat.setText(category);

                            //link
                            TextView link = view.findViewById(R.id.link);
                            String url = response.getString("url");
                            //url = url.replaceAll("\"", "'");
                            String text = "<a href=" + url + "> Business Link </a>";
                            link.setText(Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT));
                            link.setOnClickListener(v -> {
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                startActivity(browserIntent);
                            });

                            //photos
                            LinearLayout l = view.findViewById(R.id.linearLayout);
                            JSONArray ps = response.getJSONArray("photos");
                            for (int i = 0; i < ps.length(); i++) {
                                String p = ps.getString(i);
                                ImageView imageView = new ImageView(getContext());
                                Picasso.get()
                                        .load(p)
                                        .resize(600, 600)
                                        .into(imageView);
                                imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT));
                                l.addView(imageView);
                            }

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

        Button reserve = view.findViewById(R.id.button4);
        reserve.setOnClickListener(v -> {
            dialogBuilder = new AlertDialog.Builder(getContext());
            final View PopupView = getLayoutInflater().inflate(R.layout.activity_reserve_popup, null);

            TextView nameText = PopupView.findViewById(R.id.textView26);
            nameText.setText(name);

            TextView editTextDate = PopupView.findViewById(R.id.editTextDate);
            editTextDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // calender class's instance and get current date , month and year from calender
                    final Calendar c = Calendar.getInstance();
                    int mYear = c.get(Calendar.YEAR); // current year
                    int mMonth = c.get(Calendar.MONTH); // current month
                    int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                    // date picker dialog
                    datePickerDialog = new DatePickerDialog(getContext(),
                            new DatePickerDialog.OnDateSetListener() {

                                @Override
                                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                    editTextDate.setText((month + 1)  + "/"
                                            + dayOfMonth + "/" + year);
                                }


                            }, mYear, mMonth, mDay);
                    datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                    datePickerDialog.show();
                }
            });

            TextView editTextTime1 = PopupView.findViewById(R.id.editTextTime);
            editTextTime1.setInputType(InputType.TYPE_NULL);
            editTextTime1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Calendar cldr = Calendar.getInstance();
                    int hour = cldr.get(Calendar.HOUR_OF_DAY);
                    hourCheck = hour;
                    int minutes = cldr.get(Calendar.MINUTE);
                    // time picker dialog
                    picker = new TimePickerDialog(getContext(),
                            new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                    editTextTime1.setText(hourOfDay + ":" + minute);
                                    System.out.println(hourCheck);
                                }
                            }, hour, minutes, true);
                    picker.show();
                }
            });

            dialogBuilder.setView(PopupView);
            dialog = dialogBuilder.create();
            dialog.show();

            //Submit
            TextView submit = PopupView.findViewById(R.id.textView31);
            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView date = PopupView.findViewById(R.id.editTextDate);
                    String datee = date.getText().toString();
                    TextView email = PopupView.findViewById(R.id.email);
                    String emaill = email.getText().toString();
                    TextView time = PopupView.findViewById(R.id.editTextTime);
                    String timee= time.getText().toString();
                    String[] times = timee.split(":");
                    ArrayList<String> timess = new ArrayList<String>(Arrays.asList(times));
                    int hour = Integer.parseInt(timess.get(0));
                    int minute = Integer.parseInt(timess.get(1));
                    if (!checkEmail(emaill)) {
                        Toast.makeText(getActivity(), "Invalid Email Address",
                                Toast.LENGTH_LONG).show();
                    }
                    else if (hour < 10 || hour > 17) {
                        Toast.makeText(getActivity(), "Time should be between 10AM AND 5PM",
                                Toast.LENGTH_LONG).show();
                    }
                    else if (hour == 10 || hour == 17) {
                        if(minute == 0) {
                            Toast.makeText(getActivity(), "Time should be between 10AM AND 5PM",
                                    Toast.LENGTH_LONG).show();
                        }
                    } else {
                        SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE); // 0 - for private mode
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("Name", name + "_");
                        editor.putString("Email", emaill + "_");
                        editor.putString("Date", datee + "_");
                        editor.putString("Time", timee + "_");
                        if (!pref.contains("row")){
                            editor.putString("row", name + "_" + datee + "_" + timee + "_" + emaill);
                        } else {
                            String value = pref.getString("row", "");
                            String appendedValue = value + "-" + name + "_" + datee + "_" + timee + "_" + emaill;
                            editor.putString("row",appendedValue);
                        }
                        editor.commit();
                        Toast.makeText(getActivity(), "Reservation Booked",
                                Toast.LENGTH_LONG).show();
                        dialog.dismiss();

                    }
                }
            });


            //Cancel
            TextView cancel = PopupView.findViewById(R.id.textView30);
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        });


        return view;
    }
}


