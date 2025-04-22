package com.example.a71p;

import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        db = new DatabaseHelper(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        loadMarkers();
    }

    private void loadMarkers() {
        Cursor cursor = db.getAllItems();
        if (cursor.getCount() == 0) return;

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        while (cursor.moveToNext()) {
            String type = cursor.getString(cursor.getColumnIndexOrThrow("type"));
            String desc = cursor.getString(cursor.getColumnIndexOrThrow("description"));
            String location = cursor.getString(cursor.getColumnIndexOrThrow("location"));

            try {
                List<Address> addresses = geocoder.getFromLocationName(location, 1);
                if (addresses != null && !addresses.isEmpty()) {
                    Address address = addresses.get(0);
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                    mMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .title(type + ": " + desc));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10f));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
