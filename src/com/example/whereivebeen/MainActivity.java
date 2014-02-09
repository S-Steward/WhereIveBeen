package com.example.whereivebeen;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		MenuItem mi_about = (MenuItem)menu.findItem(R.id.action_about);
		mi_about.setIntent(new Intent(this, AboutActivity.class));
		return true;
	}
	
	public void onClick(View v){
		switch(v.getId())
		{
		case R.id.button1:
			Intent mapIntent = new Intent(this, MapActivity.class);
			startActivity(mapIntent);
			break;
		case R.id.button2:
			Intent pinIntent = new Intent(this, PinActivity.class);
			startActivity(pinIntent);
			break;
		}
	}

}
