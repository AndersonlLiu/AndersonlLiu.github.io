package com.example.yelp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.yelp.databinding.FragmentReviewBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MapsFragment extends Fragment {



    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        double lat;
        double lng;


        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {

            Intent intent = getActivity().getIntent();
            String id= intent.getStringExtra("message_key");

            RequestQueue volleyQueue = Volley.newRequestQueue(getContext().getApplicationContext());
            String url = "https://my-second-project-367205.uw.r.appspot.com/yelp/" + id;
            JsonObjectRequest stringRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    url,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONObject coordinate = response.getJSONObject("coordinates");
                                lat = coordinate.getDouble("latitude");
                                System.out.println(lat);
                                lng = coordinate.getDouble("longitude");
                                System.out.println(lng);

                                LatLng sydney = new LatLng(lat, lng);
                                googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in the business"));
                                googleMap.moveCamera(CameraUpdateFactory
                                        .newLatLngZoom(sydney,14));
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


        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }
}