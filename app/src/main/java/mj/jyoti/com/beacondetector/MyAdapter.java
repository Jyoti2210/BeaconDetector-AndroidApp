package mj.jyoti.com.beacondetector;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/////////////////////////////////////////////////////////////////////////////
//////                   Created By: Jyoti Bansal                   /////////
////// This class is used to display the data in card views sent    /////////
////// by the recycler view. Data includes beaconID and distance    /////////
/////////////////////////////////////////////////////////////////////////////

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private ArrayList<HashMap<String, String>> beaconIDList;
    private Context mContext;

    // contructor defined to recieve the beaconIDlist from the recyler view
    public MyAdapter(ArrayList<HashMap<String, String>> beaconIDList, Context context) {
        this.beaconIDList = beaconIDList;
        this.mContext = context;
    }

    //load the card view layout and display it
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.beaconlist_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    //bind the data recieved from the recylcerview to the card views
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HashMap<String, String> item = beaconIDList.get(position);
        for (Map.Entry<String, String> entry : item.entrySet()) {
            Log.d("adapter", "display data is " + entry.getKey());
            Log.d("adapter", "dispaly data is:" + entry.getValue());
            holder.beaconID.setText(entry.getKey());
            holder.beaconDistance.setText(entry.getValue());

            //save the data into the csv file on click of a card view
            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //showToast("Beacon: " + entry.getValue() + " is inserted into db!");
                    Toast.makeText(mContext, "Beacon:" + entry.getKey().toString() + " is inserted into the db! ", Toast.LENGTH_LONG).show();
                    File folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/MyBeaconFolder");
                    if(!folder.exists()){
                        folder.mkdirs();
                    }

                    try{
                        String fileName = folder.toString() + "/BeaconData.csv";
                        File file = new File(fileName);
                        FileOutputStream fileOutputStream = new FileOutputStream(file, true);
                        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
                        outputStreamWriter.append(entry.getKey());
                        outputStreamWriter.append(',');
                        outputStreamWriter.append(entry.getValue());
                        outputStreamWriter.append('\n');
                        outputStreamWriter.close();

                    }catch(FileNotFoundException e){
                        e.printStackTrace();
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    //return the size of beaconIDList recieved from the recycler view
    @Override
    public int getItemCount() {
        return this.beaconIDList.size();
    }

    //define and intitialize the widgets of the card view
    public class ViewHolder extends RecyclerView.ViewHolder {
        public View layout;
        public TextView beaconID;
        public TextView beaconDistance;
        public LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            layout = itemView;
            beaconID = itemView.findViewById(R.id.beaconID);
            beaconDistance = itemView.findViewById(R.id.distance);
            linearLayout = itemView.findViewById(R.id.linearLayout);
        }

    }
}
