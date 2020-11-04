package com.example.bobslittlefreelibrary;

/**
 * This class is the fragment for the list of requests
 * either sent or received.
 */

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class RequestsFragment extends Fragment {

    private ListView requestsList;
    private Context context;
    private CustomRequestsAdapter customRequestsAdapter;
    private ArrayList<Request> requestDataList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.requests_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // setting variables needed
        ListView requestsList = view.findViewById(R.id.requests_list);
        TabLayout tabLayout = view.findViewById(R.id.tabs);
        context = this.getActivity();
        requestDataList = new ArrayList<>();

        // Create the top tabs when the fragment is created
        tabLayout.addTab(tabLayout.newTab().setText("Received"));
        tabLayout.addTab(tabLayout.newTab().setText("Sent"));

        // TODO initialize the received requests here and add them to the list to display

        customRequestsAdapter = new CustomRequestsAdapter(context, requestDataList, false);
        requestsList.setAdapter(customRequestsAdapter);

        // set a tab on click listener to know when the tabs have been switched & methods to handle
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int tabPosition = tabLayout.getSelectedTabPosition();

                if (tabPosition == 0) {
                    // Received requests tab
                    requestDataList.clear();

                    // TODO query & put the received requests into the list

                    customRequestsAdapter.setSentTab(false);
                    requestsList.setAdapter(customRequestsAdapter);
                } else {
                    // Sent requests tab
                    requestDataList.clear();

                    // TODO query & put the sent requests into the list

                    customRequestsAdapter.setSentTab(true);
                    requestsList.setAdapter(customRequestsAdapter);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // pass
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // pass
            }
        });
    }
}
