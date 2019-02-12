package mj.jyoti.com.beacondetector;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class FetchNewBeacons extends AppCompatActivity implements BeaconConsumer {
    private BeaconManager mbeaconManager;
    private static final String TAG = "FetchNewBeacons";
    private String beaconID;
    private Double distance;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter mAdapter;
    private HashMap<String, String> beaconIDMap;
    private ArrayList<HashMap<String, String>> beaconIDList = new ArrayList<>();
    private Context mContext;

    private static final String UUID1 = "d77657c4-52a7-426f-b9d0-d71e10798c8a";
    private static final String UUID2 = "74278BDA-B644-4520-8F0C-720EAF059935";
    private static final String UUID3 = "FDA50693-A4E2-4FB1-AFCF-C6EB07647830";


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newbeacons);
        recyclerView = findViewById(R.id.newBeaconList_view);
        mContext = this;
        //recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL, false));

        // to enable detection of ibeacons using AltBeacon Library, using Parser class
        try{
            mbeaconManager = BeaconManager.getInstanceForApplication(mContext);
            //Beacon layout is for an ibeacon
            mbeaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:0-3=4c000215,i:4-19,i:20-21,i:22-23,p:24-24"));
            mbeaconManager.bind(this);
            mbeaconManager.setRegionStatePeristenceEnabled(false);
        }catch (Exception e){
            e.printStackTrace();
            Log.d(TAG, "ERROR:" + e.getMessage());
        }


        //mAdapter = new MyAdapter(beaconIDList, FetchNewBeacons.this);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onBeaconServiceConnect() {
        mbeaconManager.addRangeNotifier(new RangeNotifier() {
            // gets a collection of all the beacons identified in a region and their distances from the current location
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> collection, Region region) {
                if(collection.size() > 0){
                    Log.d(TAG, "Saw the first beacon: " + collection.iterator().next().getId1() + " in " + collection.iterator().next().getDistance() + " meters away");
                    // Match the UUID of the identified beacon with the UUID of our beacon to check if our beacon is working successfully
                    beaconID = collection.iterator().next().getId1().toString();
                    distance = collection.iterator().next().getDistance();

                    if(beaconIDList.size() > 0){
                        int i = 0;
                        while(i < beaconIDList.size()){
                            Log.d(TAG, "SIZE IS: " + beaconIDList.size());
                            HashMap<String, String> item = beaconIDList.get(i);
                            for(Map.Entry<String, String > entry: item.entrySet()){
                                Log.d(TAG, "KEY IN MAP: " + entry.getKey() + " key receieved: " + beaconID);
                            }
                            if(item.containsKey(beaconID)){
                                Log.d(TAG, "Beacon is already present in the list");
                                item.put(beaconID, distance.toString());
                                break;
                            } else {
                                i++;
                                if(i >= beaconIDList.size()){
                                    beaconIDMap = new HashMap<>();
                                    beaconIDMap.put(beaconID,distance.toString());
                                    beaconIDList.add(beaconIDMap);
                                }
                            }
                        }
                    } else {
                        Log.d(TAG, "Size is 0");
                        beaconIDMap = new HashMap<>();
                        beaconIDMap.put(beaconID,distance.toString());
                        beaconIDList.add(beaconIDMap);
                        Log.d(TAG, "hashmap added to the list");
                    }

                    if(beaconID.equals(UUID1)){
                        Toast.makeText(getApplicationContext(), "Beacon is installed and working successfully", Toast.LENGTH_LONG).show();
                    }
                }

                //displayData(beaconIDList);
                mAdapter.notifyDataSetChanged();
                recyclerView.setVisibility(View.VISIBLE);
            }
        });

        try {
            // Start identifying all the beacons in range and the region will be identified by a unique name called "my beacon range"
            mbeaconManager.startRangingBeaconsInRegion(new Region("my beacon range", null, null, null));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    protected void onDestroy(){
        super.onDestroy();
        mbeaconManager.unbind(this);
    }

    @Override
    public Context getApplicationContext() {
        return null;
    }

    @Override
    public void unbindService(ServiceConnection serviceConnection) {

    }

    @Override
    public boolean bindService(Intent intent, ServiceConnection serviceConnection, int i) {
        return false;
    }
}
