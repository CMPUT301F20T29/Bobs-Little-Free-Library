package com.example.bobslittlefreelibrary;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

/*
 * SelectImageFragment presents a bottom sheet where user can chooses to select an image,
 * or take an image.
 *
 * TODO: Right now images returned from camera are only a thumbnail, we'll want them to be full size
 *  images for a better user experience.
 *
 * If your activity allows user to choose an image:
 *      1. make your activity implement SelectImageFragment.OnFragmentInteractionListener
 *      2. use the image by getting the data stored in the extras of imageReturnedIntent
 *         parameter from the imageSelected method, see AddBookActivity for example.
 *
 */
public class SelectImageFragment extends BottomSheetDialogFragment {

    private Button selectImage;
    private Button takeImage;
    private SelectImageFragment.OnFragmentInteractionListener listener;

    public interface OnFragmentInteractionListener {
        void imageSelected(int requestCode, int resultCode, Intent imageReturnedIntent);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof ScanFragment.OnFragmentInteractionListener) {
            listener = (SelectImageFragment.OnFragmentInteractionListener) context;
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

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_select_image, null);

        // get views
        selectImage = view.findViewById(R.id.select_image_option);
        takeImage = view.findViewById(R.id.take_image_option);

        // button onClick listeners to open camera/image picker.
        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto , 1);//one can be replaced with any action code
            }
        });

        takeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicture, 0);//zero can be replaced with any action code (called requestCode)
            }
        });

        return view;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        listener.imageSelected(requestCode, resultCode, imageReturnedIntent);
        dismiss();

    }
}
