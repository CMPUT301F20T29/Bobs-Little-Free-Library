package com.example.bobslittlefreelibrary.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bobslittlefreelibrary.R;

/**
 * This fragment is used for pages for the ViewPager in HomeFragment.
 * Instances of this fragment contain a single ImageView, which we tag with the position of the
 * page in the ViewPager.
 * */
public class LatestBooksSlidePageFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
        int position = args.getInt("position");
        View view = (ViewGroup) inflater.inflate(R.layout.fragment_latest_books_slide_screen, container, false);
        view.findViewById(R.id.latest_books_image).setTag(position);
        view.findViewById(R.id.latest_books_title).setTag(position + 6);
        view.findViewById(R.id.content).setTag(position + 12);
        return view;
    }
}
