package com.example.whereivebeen;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.maps.model.LatLng;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class PinActivity extends Activity{

	ArrayList<LatLng> listOfPoints = new ArrayList<LatLng>();
	ArrayList<String> details = new ArrayList<String>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pin);
		if(!checkGpsState()){
			enableGPS();
		}
		loadPreferences();
	}
	
	private LatLng getLocation(){
		// Get the location manager
		LocationManager locationManager = (LocationManager) 
	            getSystemService(LOCATION_SERVICE);
		
	    String bestProvider = LocationManager.GPS_PROVIDER;
	    Location location = locationManager.getLastKnownLocation(bestProvider);
	    LocationListener loc_listener = new LocationListener() {

	        public void onLocationChanged(Location l) {}

	        public void onProviderEnabled(String p) {}

	        public void onProviderDisabled(String p) {}

	        public void onStatusChanged(String p, int status, Bundle extras) {}
	    };
	    locationManager.requestLocationUpdates(bestProvider, 0, 0, loc_listener);
		location = locationManager.getLastKnownLocation(bestProvider);
		double lat;
		double lon;
		
	    try {
	       lat = location.getLatitude();
	       lon = location.getLongitude();
	    } catch (NullPointerException e) {
	       lat = -1.0;
	       lon = -1.0;
	    }
	    LatLng coord = new LatLng(lat, lon);
	    return coord;
	}
	
	private boolean checkGpsState(){
		LocationManager locationManager = (LocationManager) 
	            getSystemService(LOCATION_SERVICE);
		
		
		boolean status = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		
		return status;
		
	}
	
	private void enableGPS(){
		
		Intent gpsOptionsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		startActivity(gpsOptionsIntent);
	}
	
	public void onClick(View v) {
		// TODO Auto-generated method stub
		CheckBox check = (CheckBox)findViewById(R.id.checkBox);
		EditText address = (EditText)findViewById(R.id.address);
		TextView v_add = (TextView)findViewById(R.id.view_address);
		switch(v.getId())
		{
		case R.id.checkBox:
			if(!check.isChecked()){
				address.setVisibility(View.VISIBLE);
				v_add.setVisibility(View.VISIBLE);
			}
			if(check.isChecked()){
				address.setVisibility(View.INVISIBLE);
				v_add.setVisibility(View.INVISIBLE);
			}
			break;
		case R.id.addPin:
			
			EditText title = (EditText)findViewById(R.id.title);
			EditText snip = (EditText)findViewById(R.id.desc);
			Spinner mySpinner = (Spinner)findViewById(R.id.type);
			String icon = mySpinner.getSelectedItem().toString();
			
			
			if(check.isChecked()){
				LatLng coord = getLocation();
				try{
				listOfPoints.add(coord);
				} catch(Exception e){
					e.printStackTrace();
				}
			}else{
				Geocoder g = new Geocoder(this);
				String text_address = address.getText().toString();
				List<Address> addressList = null;
				try{
					addressList = g.getFromLocationName(text_address, 1);
				}catch (IOException e){
					e.printStackTrace();
				}finally{
					Address loc = addressList.get(0);
					if(loc.hasLatitude() && loc.hasLongitude()){
					double lat = loc.getLatitude();
					double lon = loc.getLongitude();
					LatLng crds = new LatLng(lat, lon);
					listOfPoints.add(crds);
					}
				}
			}
			String value = title.getText().toString() + "," + snip.getText().toString() + "," + icon;
			try{
			details.add(value);
			}catch(Exception e)
			{
				e.printStackTrace();
			}
			savePreferences(listOfPoints, details);
			clearFields();
			break;
		case R.id.viewMap:
			Intent mapIntent = new Intent(this, MapActivity.class);
			startActivity(mapIntent);
			break;
		}
	}
	
	private void clearFields(){
		CheckBox check = (CheckBox)findViewById(R.id.checkBox);
		EditText title = (EditText)findViewById(R.id.title);
		EditText snip = (EditText)findViewById(R.id.desc);
		Spinner mySpinner = (Spinner)findViewById(R.id.type);
		EditText address = (EditText)findViewById(R.id.address);
		
		check.setChecked(true);
		title.setText("");
		snip.setText("");
		mySpinner.setSelection(0);
		address.setText("");
	}
	
	private void loadPreferences(){
		try{
			FileInputStream input = openFileInput("markerDetails.txt");
			DataInputStream din = new DataInputStream(input);
			int sz = din.readInt();
			for(int i = 0; i < sz; i++){
				String str = din.readUTF();
				Log.v("read", str);
				String[] stringArray = str.split(",");
				double lat = Double.parseDouble(stringArray[0]);
				double lon = Double.parseDouble(stringArray[1]);
				String values = stringArray[2] + "," + stringArray[3] + "," + stringArray[4];
				listOfPoints.add(new LatLng(lat, lon));
				details.add(values);
			}
			din.close();
		}catch (IOException exc){
			exc.printStackTrace();
		}
	}
	
	private void savePreferences(ArrayList<LatLng> marks, ArrayList<String> details){
		try{
			FileOutputStream output = openFileOutput("markerDetails.txt", Context.MODE_PRIVATE);
			
			DataOutputStream dout = new DataOutputStream(output);
			dout.writeInt(marks.size());
			for(int i = 0; i < marks.size(); i++){
				dout.writeUTF(marks.get(i).latitude + "," + marks.get(i).longitude + "," + details.get(i));
				Log.v("write", marks.get(i).latitude + "," + marks.get(i).longitude + "," + details.get(i));

			}
			dout.flush();
			dout.close();
		}catch(IOException exc){
			exc.printStackTrace();
		}
	}
}
