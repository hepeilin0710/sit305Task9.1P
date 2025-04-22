package com.example.a71p;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.widget.*;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.*;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class CreateAdvertActivity extends AppCompatActivity {

    RadioGroup radioGroupType;
    EditText editName, editPhone, editDescription, editDate, editLocation;
    Button buttonSave, buttonGetLocation;
    DatabaseHelper db;
    FusedLocationProviderClient fusedLocationClient;

    private static final int AUTOCOMPLETE_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_advert);


        radioGroupType = findViewById(R.id.radioGroupType);
        editName = findViewById(R.id.editName);
        editPhone = findViewById(R.id.editPhone);
        editDescription = findViewById(R.id.editDescription);
        editDate = findViewById(R.id.editDate);
        editLocation = findViewById(R.id.editLocation);
        buttonSave = findViewById(R.id.buttonSave);
        buttonGetLocation = findViewById(R.id.buttonGetLocation);

        db = new DatabaseHelper(this);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // init Places API
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), "AIzaSyBIuh2BzVm2cTMgZIBH5Ai1Xgs7QLXFfGk", Locale.getDefault());
        }

        // auto
        editLocation.setFocusable(false);
        editLocation.setOnClickListener(v -> {
            List<Place.Field> fields = Arrays.asList(Place.Field.ADDRESS);
            Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields).build(this);
            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
        });

        // cur location
        buttonGetLocation.setOnClickListener(v -> {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
                return;
            }

            fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
                if (location != null) {
                    getAddressFromLocation(location);
                } else {
                    Toast.makeText(this, "Location not available", Toast.LENGTH_SHORT).show();
                }
            });
        });


        buttonSave.setOnClickListener(v -> {
            int selectedId = radioGroupType.getCheckedRadioButtonId();
            RadioButton selectedRadio = findViewById(selectedId);
            String type = selectedRadio.getText().toString();

            String name = editName.getText().toString();
            String phone = editPhone.getText().toString();
            String description = editDescription.getText().toString();
            String date = editDate.getText().toString();
            String location = editLocation.getText().toString();

            boolean inserted = db.insertData(type, name, phone, description, date, location);
            if (inserted) {
                Toast.makeText(this, "Saved Successfully!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to Save!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE && resultCode == RESULT_OK) {
            Place place = Autocomplete.getPlaceFromIntent(data);
            editLocation.setText(place.getAddress());
        }
    }

    // Converts a Location into a human-readable address
    private void getAddressFromLocation(Location location) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (addressList != null && !addressList.isEmpty()) {
                Address address = addressList.get(0);
                editLocation.setText(address.getAddressLine(0));
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to get address", Toast.LENGTH_SHORT).show();
        }
    }
}


