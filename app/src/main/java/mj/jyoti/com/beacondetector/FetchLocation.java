package mj.jyoti.com.beacondetector;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.opencsv.CSVWriter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/////////////////////////////////////////////////////////////////////////////
//////                   Created By: Jyoti Bansal                   /////////
////// This class is used to collect the location coordinates of    /////////
////// the rooms and save the data to a CSV file                    /////////
/////////////////////////////////////////////////////////////////////////////

public class FetchLocation extends AppCompatActivity implements LocationListener {

    Button locationCoordinates;
    EditText roomName;
    LocationManager mLocationManager;
    HashMap<String, ArrayList<Double>> map = new HashMap<String, ArrayList<Double>>();
    ArrayList<Double> locationList;
    int count;
    Double latitude, longitude;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fetch_location);

        Intent i = getIntent();

        // location manager to continuously track the location of the user.
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, FetchLocation.this);

        roomName = findViewById(R.id.roomName_input);

        locationCoordinates = findViewById(R.id.locationCoordinates);
        locationCoordinates.setText("Get Coordinates");
        locationCoordinates.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                // This method will fetch the location coordinates receieved from the onLocationChanged() and save it in the csv file
                map.put(roomName.getText().toString(), locationList);
                Toast.makeText(getApplicationContext(), "values put in the db: " + latitude + " " + longitude, Toast.LENGTH_SHORT).show();
                exportToCSV(map);

                Double value1=0.0;
                Double value2 = 0.0;
                for(int i=0;i<locationList.size();i++){
                     value1 = locationList.get(0);
                     value2 = locationList.get(1);
                }
            }
        });

    }

    // This method is invoked whenever there is a change in user location
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onLocationChanged(Location location) {
        //Check the accuracy of the location
        if (location.getAccuracy() < 20.0) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            locationList = new ArrayList<>();
            locationList.add(latitude);
            locationList.add(longitude);
            Toast.makeText(getApplicationContext(), "Click on the button to get the location", Toast.LENGTH_LONG).show();
        }
    }

    // This method saves location coordinates to a csv file
    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void exportToCSV(HashMap<String, ArrayList<Double>> map){
        File folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/MyLocationFolder");
        if(!folder.exists()){
            folder.mkdirs();
        }

       try{
            String fileName = folder.toString() + "/LocationData.csv";
            File file = new File(fileName);
            FileOutputStream fileOutputStream = new FileOutputStream(file, true);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
            for(Map.Entry<String, ArrayList<Double>> entry : map.entrySet()){
                outputStreamWriter.append(entry.getKey());
                outputStreamWriter.append(',');
                outputStreamWriter.append(entry.getValue().get(0).toString());
                outputStreamWriter.append(',');
                outputStreamWriter.append(entry.getValue().get(1).toString());
                outputStreamWriter.append(',');
                outputStreamWriter.append('\n');
                outputStreamWriter.close();
            }
       }catch(FileNotFoundException e){
            e.printStackTrace();
       }catch (IOException e){
            e.printStackTrace();
       }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    // stop collecting change in the location coordinates when no longer required
    public void onBackPressed(){
        super.onBackPressed();
        mLocationManager.removeUpdates(this);
    }
}
