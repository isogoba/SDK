// Affichage GMAP et Géolocalisation du mobile


package com.ibrahima.appliexo;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;

import com.google.android.gms.maps.model.LatLng;


public class HelloGoogleMapActivity extends FragmentActivity {
	@SuppressWarnings("unused")
	private Context context = this;
	final String EXTRA_Name = "name";
    final String EXTRA_firstName = "firstName";
    String name = null;
	String firstname = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_map);

		GoogleMap gMap = ((SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(com.ibrahima.appliexo.R.id.map)).getMap();

		gMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		gMap.setMyLocationEnabled(true);
		gMap.getUiSettings().setCompassEnabled(true);
		LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, true);
        Location location = locationManager.getLastKnownLocation(provider);
        if(location!=null){
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            LatLng latLng = new LatLng(latitude, longitude);
            gMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            gMap.animateCamera(CameraUpdateFactory.zoomTo(16)); // gestion du Zoom
        }
		Log.e("Maps", "------EOC-------");
		
		
		Intent intent = getIntent();
		if (intent != null) {
	     	  name=intent.getStringExtra(EXTRA_Name);
	     	  firstname=intent.getStringExtra(EXTRA_firstName);
	     	   
	        }
	        
		
		
		
		final Button button = (Button) findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Intent intent = new Intent(HelloGoogleMapActivity.this, HelloUser.class);
            	intent.putExtra(EXTRA_Name, name);
				intent.putExtra(EXTRA_firstName, firstname);
        		startActivity(intent);
        		finish();
   	
		
            }
        });
	}
	
	
}
