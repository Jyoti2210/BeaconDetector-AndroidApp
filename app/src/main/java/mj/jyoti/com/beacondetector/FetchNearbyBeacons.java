package mj.jyoti.com.beacondetector;

import android.os.Bundle;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//////////////////////////////////////////////////////////////////////////////
//////                   Created By: Jyoti Bansal                     ////////
////// This class is used to detect the nearby ibeacons using the     ////////
//////  alt beacon library and send the details to the adapter to     ////////
/////             to display the data on card views                   ///////
/////////////////////////////////////////////////////////////////////////////

public class FetchNearbyBeacons extends AppCompatActivity implements BeaconConsumer{

    private BeaconManager beaconManager;
    private static final String TAG = "FetchNearbyBeacons";
    String beaconID;
    Double distance;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter mAdapter;
    HashMap<String, String> beaconIDMap;
    ArrayList<HashMap<String, String>> beaconIDList = new ArrayList<>();



    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nearbybeacons);
        recyclerView = findViewById(R.id.beaconList_view);
        //recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false));

        // to enable detection of ibeacons using AltBeacon Library, using Parser class
        beaconManager = BeaconManager.getInstanceForApplication(this);
        //Beacon layout is for an ibeacon
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:0-3=4c000215,i:4-19,i:20-21,i:22-23,p:24-24"));
        beaconManager.bind(this);
        beaconManager.setRegionStatePeristenceEnabled(false);

        mAdapter = new MyAdapter(beaconIDList, FetchNearbyBeacons.this);
        recyclerView.setAdapter(mAdapter);
    }

    // This method will connect to all the nearby beacons using bluetooth technology and fetch the list to display on the recylcer view
    @Override
    public void onBeaconServiceConnect() {
        beaconManager.addRangeNotifier(new RangeNotifier() {
            // gets a collection of all the beacons identified in a region and their distances from the current location
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> collection, Region region) {
                if(collection.size() > 0){
                    //fetch the beaconID and its distance from the current location of the beacon detected
                    beaconID = collection.iterator().next().getId1().toString();
                    distance = collection.iterator().next().getDistance();

                    // add the beacon details in the arraylist to be sent to adapter to display in the card views
                    if(beaconIDList.size() > 0){
                        int i = 0;
                        while(i < beaconIDList.size()){
                            Log.d(TAG, "SIZE IS: " + beaconIDList.size());
                            HashMap<String, String> item = beaconIDList.get(i);
                            //Ensure that the same beacon is not displayed again in the list
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
                }

               //notify the changes in the arraylist
                mAdapter.notifyDataSetChanged();
                recyclerView.setVisibility(View.VISIBLE);
            }

        });

        try{
            // Start identifying all the beacons in range and the region will be identified by a unique name called "my beacon range"
            beaconManager.startRangingBeaconsInRegion(new Region("my beacon range",null, null, null));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    //disable the beaconManager when no longer required
    protected void onDestroy(){
        super.onDestroy();
        beaconManager.unbind(this);
    }

    /*public void sort(ArrayList beaconIDList){
        Collections.sort(beaconIDList, new Comparator<HashMap<String, String>>() {
            @Override
            public int compare(HashMap<String, String> m1, HashMap<String, String> m2) {
                return m1.get("beaconID").compareTo(m2.get("beaconID"));
            }
        });
    }*/
}
