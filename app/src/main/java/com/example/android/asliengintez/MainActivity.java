package com.example.android.asliengintez;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity
        implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    public static final int REQUEST_CODE_LOCATION = 11;
    public static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    public static final int PLACE_AUTOCOMPLETE_RESULT_OK = -1;
    public static final int PLACE_AUTOCOMPLETE_RESULT_CANCELED = 0;
    protected static final String TAG = "Location2-1";
    protected GoogleApiClient mGoogleApiClient;
    protected Location mLastLocation;
    GoogleMap m_map;
    boolean mapReady = false;
    private Map<String, CarPark> carParkMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Constants.saveToDB();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mapReady = true;
        m_map = map;

        carParkMap = Constants.getLocationMap();
        for (Map.Entry<String, CarPark> entry : carParkMap.entrySet()) {
            LatLng latLng = new LatLng(entry.getValue().getLocation().getLatitude(),
                    entry.getValue().getLocation().getLongitude());
            m_map.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title(entry.getKey())
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_car)));
        }

        m_map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Intent intent = new Intent(MainActivity.this, CarParkInfoActivity.class);
                intent.putExtra("carparkObj", marker.getTitle());
                startActivity(intent);
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(10 * 1000);
                        final Random random = new Random();
                        List<String> list = Constants.getCarParksNames();
                        final String name = list.get(random.nextInt(list.size() - 1));
                        Firebase firebaseRef = new Firebase(Constants.FIREBASE_URL).child("parks").child(name);
                        firebaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                CarPark carPark = dataSnapshot.getValue(CarPark.class);
                                int choice = random.nextInt(2);
                                int newCarNo = random.nextInt(5) + 1;
                                if (choice == 0 && carPark.getCapacity() - carPark.getCarNumber() > 5) {
                                    new Firebase(Constants.FIREBASE_URL).child("parks").child(name)
                                            .child("carNumber").setValue(carPark.getCarNumber() + newCarNo);
                                } else {
                                    new Firebase(Constants.FIREBASE_URL).child("parks").child(name)
                                            .child("carNumber").setValue(carPark.getCarNumber() - newCarNo);
                                }
                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {

                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        CameraPosition target = CameraPosition.builder().target(new LatLng(38.3387108, 27.1332407)).zoom(14).build();
        m_map.moveCamera(CameraUpdateFactory.newCameraPosition(target));

    }

    private void flyTo(LatLng latLng, int zoom, int durationSec) {
        m_map.animateCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.builder()
                .target(latLng)
                .zoom(zoom)
                .build()), durationSec * 1000, null);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    /**
     * Runs when a GoogleApiClient object successfully connects.
     */
    @Override
    public void onConnected(Bundle connectionHint) {
//        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
//        if (mLastLocation != null) {
//            LatLng newYork = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
//            CameraPosition target = CameraPosition.builder().target(newYork).zoom(14).build();
//            m_map.moveCamera(CameraUpdateFactory.newCameraPosition(target));
//            m_map.addMarker(new MarkerOptions()
//                    .position(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()))
//                    .title("Just Me")
//                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_car)));
//        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }

    @Override
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_search) {
            createPlaceFinder();
        } else if (id == R.id.action_user_location) {
            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_CODE_LOCATION);
                return false;
            }
            if (!((LocationManager) getSystemService(Context.LOCATION_SERVICE))
                    .isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                Intent gpsOptionsIntent = new Intent(
                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(gpsOptionsIntent);
            } else {
                mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                if (mLastLocation != null) {
                    LatLng userCurrentLoc = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                    m_map.addMarker(new MarkerOptions()
                            .position(userCurrentLoc)
                            .title(getString(R.string.title_current_location))
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker)));
                    flyTo(userCurrentLoc, 14, 2);
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }

    protected void createPlaceFinder() {
        try {
            Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                            .build(this);
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
            Log.e(TAG, "Could not create place finder " + e.getMessage());
        } catch (GooglePlayServicesNotAvailableException e) {
            Log.e(TAG, "Could not create place finder " + e.getMessage());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == PLACE_AUTOCOMPLETE_RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                m_map.addMarker(new MarkerOptions()
                        .position(place.getLatLng())
                        .title(place.getName().toString())
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker)));
                flyTo(place.getLatLng(), 14, 2);
                Log.i(TAG, "Place: " + place.getName());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                Log.i(TAG, status.getStatusMessage());
            } else if (resultCode == PLACE_AUTOCOMPLETE_RESULT_CANCELED) {
                Log.i(TAG, "cancelled");
            }
        }
    }

}
