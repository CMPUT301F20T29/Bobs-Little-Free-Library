package com.example.bobslittlefreelibrary.views;

import android.nfc.Tag;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bobslittlefreelibrary.R;

public class LatestBooksSlidePageFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
        int position = args.getInt("position");
        View view = (ViewGroup) inflater.inflate(R.layout.fragment_latest_books_slide_screen, container, false);
        view.findViewById(R.id.latest_books_image).setTag(position);
        return view;
    }
}
