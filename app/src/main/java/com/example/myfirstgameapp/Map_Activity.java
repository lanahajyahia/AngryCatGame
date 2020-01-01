package com.example.myfirstgameapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class Map_Activity extends AppCompatActivity implements OnMapReadyCallback {
    private ArrayList<User> usersList;
    private GoogleMap mMap;
    private LatLng[] locations;
    private ListView listView_top10Scores;
    private Button btn_backTomenu;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_map_);

        initComponents();

    }

    /**
     * initialize the activity
     */
    private void initComponents() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_map);
        mapFragment.getMapAsync(this);

        UserManager userManager = new UserManager(Map_Activity.this);
        usersList = userManager.getRecords();


        listView_top10Scores = findViewById(R.id.listView_top10Scores);
        btn_backTomenu = findViewById(R.id.btn_backTomenu);
        btn_backTomenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        locations = new LatLng[usersList.size()];
        fillPlacesList();

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker where every record was reached and move the camera for the 1st place
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (usersList.size() == 0)
            return;
        mMap = googleMap;
        User user;
        for (int i = 0; i < locations.length; i++) {
            user = usersList.get(i);
            locations[i] = new LatLng(user.getLatitude(), user.getLongitude());
            Log.d("lag",user.getLatitude()+"and "+ user.getLongitude() );
            mMap.addMarker(new MarkerOptions().position(locations[i]).title("" + (i + 1) + ": " + user.toString()).icon(BitmapDescriptorFactory.defaultMarker()));
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(locations[0], 18.0f));
    }


    private AdapterView.OnItemClickListener listClickedHandler = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView parent, View v, int position, long id) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(locations[position], 18.0f));
        }
    };

    private void fillPlacesList() {
        ArrayAdapter<User> placesAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, usersList);
        listView_top10Scores.setAdapter(placesAdapter);
        listView_top10Scores.setOnItemClickListener(listClickedHandler);
    }
}
