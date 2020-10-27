package com.example.bobslittlefreelibrary;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

/*
 * ScanFragment presents a full screen bottom sheet with a camera view.
 *
 * TODO: implement camera view and barcode scanning in firebase.
 *
 * If your activity has to scan any ISBN:
 *      1. make your activity implement ScanFragment.OnFragmentInteractionListener
 *      2. use the isbn in the onIsbnFound() method
 *
 */
public class ScanFragment extends BottomSheetDialogFragment {

    private EditText isbnInput;
    private Button submitButton;
    private OnFragmentInteractionListener listener;

    public interface OnFragmentInteractionListener {
        void onIsbnFound(String isbn);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_scan, null);

        isbnInput = view.findViewById(R.id.isbn_input);
        submitButton = view.findViewById(R.id.submit_button);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isbnInput.getText() != null) {
                    String isbnCandidate = isbnInput.getText().toString().trim();
                    if (isbnCandidate.length() == 10 || isbnCandidate.length() == 13) {
                        //9780345514400 9781305109391
                        listener.onIsbnFound("9781305109391");
                        dismiss();
                    }
                }
            }
        });

        return view;
    }

    /*
     * Overrided onCreateDialog() makes the bottom sheet cover the entire screen.
     *
     * Code from answer by user "Gabriele Mariotti" on SO:
     * https://stackoverflow.com/questions/58065771/bottomsheetdialogfragment-full-screen
     *
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override public void onShow(DialogInterface dialogInterface) {
                BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) dialogInterface;
                setupFullHeight(bottomSheetDialog);
            }
        });
        return  dialog;
    }

    private void setupFullHeight(BottomSheetDialog bottomSheetDialog) {
        FrameLayout bottomSheet = (FrameLayout) bottomSheetDialog.findViewById(R.id.design_bottom_sheet);
        BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
        ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();

        int windowHeight = getWindowHeight();
        if (layoutParams != null) {
            layoutParams.height = windowHeight;
        }
        bottomSheet.setLayoutParams(layoutParams);
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    private int getWindowHeight() {
        // Calculate window height for fullscreen use
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }
}