package com.example.bobslittlefreelibrary;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.bobslittlefreelibrary.models.User;
import com.example.bobslittlefreelibrary.views.users.MyProfileViewActivity;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.Arrays;
import java.util.HashMap;

public class EditProfileFragment extends AppCompatDialogFragment {
    private EditText phoneEditText;
    private EditText bioEditText;
    private MyProfileViewActivity profile;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private DocumentReference ref;
    private HashMap userMap;
    private String address;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle saveInstanceState) {

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_edit_profile, null);
        phoneEditText = view.findViewById(R.id.phoneNumb);
        bioEditText = view.findViewById(R.id.bio);
        profile = (MyProfileViewActivity)getActivity();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        TextView addy = getActivity().findViewById(R.id.user_address);
        TextView phone = getActivity().findViewById(R.id.phone);
        TextView bio2 = getActivity().findViewById(R.id.bio);

        Places.initialize(getContext(), getString(R.string.PLACES_API_KEY));
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment) getFragmentManager().findFragmentById(R.id.autocomplete_fragment2);

        db = FirebaseFirestore.getInstance();
        // set current info of user to be displayed on edit profile field
        ref = db.collection("users").document(user.getUid());
        ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User userFromDb = documentSnapshot.toObject(User.class);
                address = userFromDb.getAddress();
                autocompleteFragment.setText(address);
                if(userFromDb.getPhone()!=null){
                    phoneEditText.setText(userFromDb.getPhone());
                }
                if(userFromDb.getBio()!=null){
                    bioEditText.setText(userFromDb.getBio());
                }
            }
        });

        final double[] Lat = new double[1];
        final double[] Lng = new double[1];
        autocompleteFragment.setTypeFilter(TypeFilter.ADDRESS);
        autocompleteFragment.setCountries("CA");
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID,Place.Field.LAT_LNG,Place.Field.NAME));

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                Log.d("placedebug","PLACE: "+place.getName()+", "+place.getId()+", "+place.getLatLng());
                address = place.getName();
                Lat[0] = place.getLatLng().latitude;
                Lng[0] = place.getLatLng().longitude;
                Log.d("placedebug","Saved Name: "+address+ ", Saved LatLng: "+ Lat[0] +", "+ Lng[0]);
            }

            @Override
            public void onError(@NonNull Status status) {
                Log.d("placedebug","An error occurred: "+status);
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder((getActivity()));
        return builder
                .setView(view)
                .setTitle("Edit Profile")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        getFragmentManager().beginTransaction().remove(autocompleteFragment).commit();
                    }
                })
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (checkInput(profile.findViewById(R.id.profileView))){

                            String phoneNumb = phoneEditText.getText().toString();
                            String bio = bioEditText.getText().toString();

                            ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {

                                    User userFromDb = documentSnapshot.toObject(User.class);
                                    userMap = new HashMap<String, String>();

                                    if(!address.equals(userFromDb.getAddress())){
                                        userMap.put("address",address);
                                        addy.setText(address);
                                    }
                                    if(phoneNumb.length() > 0){
                                        userMap.put("phone",phoneNumb);
                                        phone.setText(phoneNumb);
                                    } else {
                                        userMap.put("phone",null);
                                        phone.setText("(Optional) Phone Number");
                                    }
                                    if(bio.length() > 0){
                                        userMap.put("bio",bio);
                                        bio2.setText(bio);
                                    } else {
                                        userMap.put("bio",null);
                                        bio2.setText("Add bio by pressing EDIT button");
                                    }

                                    ref.update(userMap);

                                }

                            });
                        }
                        getFragmentManager().beginTransaction().remove(autocompleteFragment).commit();
                    }
                }).create();

    }

    private boolean checkInput(View v){

        String phone = phoneEditText.getText().toString();
        String bio = bioEditText.getText().toString();

        if (phone.length() > 0){
            if (phone.length() != 10 || !phone.matches("[0-9]{10}")){
                Snackbar.make(v,"Phone number is not in correct format; eg.7801112233",Snackbar.LENGTH_SHORT).show();
                return false;
            }
        }
        if (bio.length()>0) {
            if (bio.length() > 50) {
                Snackbar.make(v, "Bio is too long", Snackbar.LENGTH_SHORT).show();
                return false;
            }
        }

        return true;

    }

}
