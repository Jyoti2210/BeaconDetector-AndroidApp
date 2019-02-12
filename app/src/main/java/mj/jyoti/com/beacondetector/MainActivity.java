package mj.jyoti.com.beacondetector;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

/////////////////////////////////////////////////////////////////////////////
//////                   Created By: Jyoti Bansal                   /////////
////// This class is used to initialize the buttons and navigate    /////////
////// to the respective activities on their clicks                 /////////
/////////////////////////////////////////////////////////////////////////////

public class MainActivity extends AppCompatActivity {

    Button location, beaconList, beaconInstall;
    private static final String TAG = "MainActivity";
    static public final int REQUEST_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Externally request permission to access user location
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_LOCATION);

        location = (Button) findViewById(R.id.location);
        beaconList = (Button) findViewById(R.id.beaconInfo);
        beaconInstall = findViewById(R.id.beaconInstall);

        //Transit to Fetch Location Activity
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), FetchLocation.class);
                startActivity(i);
            }
        });

        // Transit to Beacon detector activity
        beaconList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), FetchNearbyBeacons.class);
                startActivity(i);
            }
        });

        /*beaconInstall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), FetchNewBeacons.class);
                startActivity(i);
            }
        });*/
    }

    // Check if the location permission is granted to this application since Location permission falls under dangerous permission category
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_LOCATION) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "Permission granted");
            } else {
                Log.d(TAG, "Permission Denied");
            }
        }
    }
}
