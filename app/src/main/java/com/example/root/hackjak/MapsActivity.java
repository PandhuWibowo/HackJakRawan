package com.example.root.hackjak;

import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.example.root.hackjak.AllRestAPI.Response;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener , com.google.android.gms.location.LocationListener {

    //google maps
    private GoogleMap mMaps;
    GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Marker mCurrLocationMarker;
    private Location mLastLocation;

    ProgressDialog progressDialog;

    private String maps_link_url;
    private String mosque = "mosque";
    private String sensor = "true";
    private String radius = "5000";
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    String listId, desc, address, longitude, latitude, date, starthour, endhour, quota, userId;
    TextView tvDesc, tvAddress, tvLongitude, tvLatitude, tvDate, tvStartHour, tvEndHour, tvQuota;
    Button btnDaftar;

    //Toolbar
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.maps_lokasi);
        mapFragment.getMapAsync(this);

        latitude = "-6.235510";
        longitude = "106.747263";

        toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        toolbar.setLogoDescription(getResources().getString(R.string.sign_up_title));
        toolbar.setTitleTextColor(getResources().getColor(R.color.title));
        toolbar.setTitle("Rawan");
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }


        //menghentikan pembaruan lokasi
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMaps = googleMap;
        //setupMaps();

        ambilData();
    }

    private void ambilData()
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http:ikutevent.com/api-event/hackjak/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService service = retrofit.create(ApiService.class);
        Call<Response> result = service.getProperties();

        result.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                int warna = 0;
                String kategori;


                mMaps.setMapType(GoogleMap.MAP_TYPE_NORMAL); //merubah tampilan maps
                mMaps.setTrafficEnabled(true);

                LatLng latLng = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));

                CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(16f).build();

                mMaps.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
//

                // reqcoor.php POST: kelurahan, kecamatan, kota
//                Double a = response.body().getData().get(1).getCoordinates().get(0).getLongtd();
//                Log.e("ERROR :" , a.toString());
                for (int i = 0; i< 250; i++)
                {
                    PolygonOptions polyOption = new PolygonOptions();

                    kategori = response.body().getData().get(i).getProperties().getKategori().toString();
                    if (kategori.equalsIgnoreCase("tidak rawan"))
                    {
                        warna = 0x5500ff00; //hijau
                    }
                    else if (kategori.equalsIgnoreCase("rawan berat"))
                    {
                        warna = 0x7fcd5c5c; //merah
                    }
                    else if (kategori.equalsIgnoreCase("Rawan Sangat Ringan"))
                    {
                        warna = 0x7fadff2f; //kuning muda
                    }
                    else if (kategori.equalsIgnoreCase("Rawan Ringan"))
                    {
                        warna = 0x7fffff00; //kuning tua
                    }
                    for (int j = 0; j < response.body().getData().get(i).getCoordinates().size(); j++)
                    {
                        Double lattd = response.body().getData().get(i).getCoordinates().get(j).getLat();
                        Double longtd = response.body().getData().get(i).getCoordinates().get(j).getJsonMemberLong();
                        String a = response.body().getData().get(i).getProperties().getKategori().toString();
                        Log.e("ERROR :" , a.toString());
                        polyOption.add(new LatLng(lattd, longtd));
                    }
                    polyOption.strokeColor(Color.BLACK);
                    polyOption.fillColor(warna);

                    polyOption.strokeWidth(3);
                    mMaps.addPolygon(polyOption);
                }


                //Memulai Google Play Services
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(getApplicationContext(),
                            android.Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        buildGoogleApiClient();
                        mMaps.setMyLocationEnabled(true);
                    }
                }
                else {
                    buildGoogleApiClient();
                    mMaps.setMyLocationEnabled(true);
                }


            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {

                Toast.makeText(MapsActivity.this, "Error : " + t.toString(), Toast.LENGTH_LONG).show();
                Log.e("ERROR" , t.toString());
            }
        });

//        mMaps.setMapType(GoogleMap.MAP_TYPE_NORMAL); //merubah tampilan maps
//                mMaps.setTrafficEnabled(true);
//
//                LatLng latLng = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
//
//                CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(16f).build();
//
//                mMaps.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

//        Circle circle = mMaps.addCircle(new CircleOptions()
//                .center(latLng)
//                .radius(100)
//                .strokeColor(Color.RED)
//                .fillColor(Color.BLUE));
//        float[] dst = new float[2];
//
//        Location myLocation = mMaps.getMyLocation();
//        double lat = myLocation.getLatitude();
//        double longtd = myLocation.getLongitude();
//
//        LatLng latLng2 = new LatLng(lat, longtd);
//
//        Marker mark = mMaps.addMarker(new MarkerOptions()
//                .position(latLng2)
//                .title("Hello world"));
//
//        Location.distanceBetween(mark.getPosition().latitude, mark.getPosition().longitude, circle.getCenter().latitude, circle.getCenter().longitude, dst);
//        if (dst[0] > circle.getRadius())
//        {
//            Log.e("Circle : " , "DI LUAR RADIUS");
//        }
//        else
//        {
//            Log.e("Circle : " , "DI DALAM RADIUS");
//        }

    }

    private void setupPoly(double lattd, double longtd)
    {
        mMaps.addPolygon(new PolygonOptions()
                .add(new LatLng(-6.235615, 106.747218))
                .add(new LatLng(-6.236727, 106.747207))
                .add(new LatLng(-6.236052, 106.747848))
                .strokeColor(Color.RED)
                .fillColor(0x5500ff00));

//        mMaps.addPolygon(new PolygonOptions()
//                .add(new LatLng(-6.236727, 106.747207))
//                .strokeColor(Color.RED)
//                .fillColor(0x5500ff00));
//
//        mMaps.addPolygon(new PolygonOptions()
//                .add(new LatLng(-6.236052, 106.747848))
//                .strokeColor(Color.RED)
//                .fillColor(0x5500ff00));

    }
    private void setupMaps(){
        mMaps.setMapType(GoogleMap.MAP_TYPE_NORMAL); //merubah tampilan maps
        mMaps.setTrafficEnabled(true);

        LatLng latLng = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));

        CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(16f).build();

        mMaps.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));




        //warna hijau transparent 0x5500ff00
//        mMaps.addCircle(new CircleOptions()
//                .center(latLng)
//                .radius(50)
//                .strokeColor(Color.RED)
//                .strokeWidth(3)
//                .fillColor(10485588));
//
//        mMaps.addCircle(new CircleOptions()
//                .center(new LatLng(-6.236009, 106.749519))
//                .radius(50)
//                .strokeWidth(5)
//                .strokeColor(Color.RED)
//                .fillColor(Color.BLUE));


        //Memulai Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMaps.setMyLocationEnabled(true);
            }
        }
        else {
            buildGoogleApiClient();
            mMaps.setMyLocationEnabled(true);
        }

    }
    protected synchronized void buildGoogleApiClient(){
        mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        mGoogleApiClient.connect();
    }

    /* end setup */

    /* permission (marshmellow*/
    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // Izin diberikan.
                    if (ContextCompat.checkSelfPermission(this,
                            android.Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMaps.setMyLocationEnabled(true);
                    }

                } else {

                    // Izin ditolak.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    /*end permission*/
}
