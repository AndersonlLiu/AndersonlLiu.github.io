package com.example.yelp;

import static android.content.Intent.getIntent;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.yelp.databinding.FragmentReviewBinding;

public class MapLocation extends Fragment {
    private FragmentReviewBinding binding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Intent intent = getActivity().getIntent();
        String id= intent.getStringExtra("message_key");

        binding = FragmentReviewBinding.inflate(inflater, container, false);
        View view = inflater.inflate(R.layout.fragment_map_location, container, false);


        return view;
    }


}
