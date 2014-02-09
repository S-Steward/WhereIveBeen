package com.example.whereivebeen;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

import com.google.android.gms.maps.*;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.*;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class MapActivity extends Activity {

	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        
        GoogleMap map = ((MapFragment)getFragmentManager()
                .findFragmentById(R.id.the_map)).getMap();
        
        
        
        defaultSet(map);
        loadPreferences(map);

    }
    
    private void loadPreferences(GoogleMap map){
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
    				LatLng coord = new LatLng(lat, lon);
    				String title = stringArray[2];
    				String snip = stringArray[3];
    				String icon = stringArray[4];
    				addPins(coord, title, snip, icon, map);
    			}
    			din.close();
    		}catch (IOException exc){
    			exc.printStackTrace();
    		}
    		//this will load the saved data from markerDetails
    }
    
    private void defaultSet(GoogleMap map){
    	LatLng america = new LatLng(42.9837, -81.2497);

        map.setMyLocationEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(america, 5));
        map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        
        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setCompassEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(true);
        
        map.getUiSettings().setAllGesturesEnabled(true);
        
        map.setTrafficEnabled(true);
        
    }
    
    private void addPins(LatLng lat_long, String title, String snip, String icon, GoogleMap map)
    {
    	//Below delete the ); and undo the comment infront of .icon to add it to the marker. Then change icon_location to
    	//the icon's name. The images should be in all of the drawables. 
    	if(icon == "Home"){
    		map.addMarker(new MarkerOptions()
    		.title(title)
    		.snippet(snip)
    		.position(lat_long));
    		//.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_location));
    		}
    	if(icon == "Work"){
        	map.addMarker(new MarkerOptions()
        	.title(title)
        	.snippet(snip)
        	.position(lat_long));
        	//.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_location));
        	}
    	if(icon == "Vacation"){
        	map.addMarker(new MarkerOptions()
        	.title(title)
        	.snippet(snip)
        	.position(lat_long));
        	//.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_location));
        	}
    	if(icon == "Friends"){
        	map.addMarker(new MarkerOptions()
        	.title(title)
        	.snippet(snip)
        	.position(lat_long));
        	//.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_location));
        	}
    	
    	//Need this to allow you to see the info window.
    	map.setOnMarkerClickListener(new OnMarkerClickListener() {
    	    public boolean onMarkerClick(Marker mark) {
    	    	mark.showInfoWindow();
    	        return true;
    	    }
    	});
    	
    }
}
