package com.example.nishi.water_app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

public class SelectiveData extends AppCompatActivity {
    String seldate ;
    TextView MIN,MAX,DATE;
    double min=999,minpos,max=0,maxpos;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth mAuth;
    GraphView graph;
    DataPoint[] dp;
    double[][] graphtemp;
    DatabaseReference myRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selective_data);
        graph = findViewById(R.id.selgraph);
        MIN=findViewById(R.id.minValue);
        MAX=findViewById(R.id.maxValue);
        DATE=findViewById(R.id.curDate);
        Log.d("seldate", "onCreate: ");
    }
    private void getIncomingIntent(){
        if(getIntent().hasExtra("selDate")){
        seldate=getIntent().getStringExtra("selDate");
        Log.w("seldate",seldate);
        myRef=database.getReference("logs/"+seldate);
        }
        else{
            Log.d("TAG", "getIncomingIntent: no intent found");
            finish();
        }
    }
private void formGraph(){
    myRef.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            // This method is called once with the initial value and again
            // whenever data at this location is updated.
            //Loop 1 to go through all the child nodes of users
            int index = 0;
            graphtemp= new double[(int) dataSnapshot.getChildrenCount()][2];
            for (DataSnapshot waterSnapshot : dataSnapshot.getChildren()) {
                //loop 2 to go through all the child nodes of books node
                String key = waterSnapshot.getKey();
                String[] newkey = key.split(":");
                float a=((float)((Double.parseDouble(newkey[1])/60)+Double.parseDouble(newkey[0])));
                Object Value = waterSnapshot.getValue();
                Log.w("TAG","logging :"+ a);
                graphtemp[index][0]=a;
                graphtemp[index][1]=Double.parseDouble(Value.toString());
                index++;
            }
            dp=new DataPoint[graphtemp.length];
            for ( int k = 0; k < graphtemp.length; k++)
                //       dp[k] = new DataPoint(graphtemp[k][0], graphtemp[k][1]);
                Log.w("TAG",graphtemp[k][0]+ " "+graphtemp[k][1]);
            double[] temp;
            for(int l=0;l<graphtemp.length-1; l++){
                for (int m=l+1;m<graphtemp.length;m++){
                    if(graphtemp[l][0]>graphtemp[m][0]){
                        temp=graphtemp[l];
                        graphtemp[l]=graphtemp[m];
                        graphtemp[m]=temp;
                    }
                }
            }
            for ( int k = 0; k < graphtemp.length; k++) {
                if (graphtemp[k][1] >= max) {
                    max = graphtemp[k][1];
                    Log.w("TAG", "onDataChange: max value "+max);
                }
                if (graphtemp[k][1] <= min) {
                    min = graphtemp[k][1];
                    Log.w("TAG", "onDataChange: min value "+min);
                }
                dp[k] = new DataPoint(graphtemp[k][0], graphtemp[k][1]);
            }
            MIN.setText(String.valueOf((float)min)+" ft");
            MAX.setText(String.valueOf((float)max)+" ft");
            //Log.w(TAG,"sorted"+graphtemp[k][0]+ " "+graphtemp[k][1]);
            LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dp);
            try {
                graph.getViewport().setYAxisBoundsManual(true);
                graph.getViewport().setMinY(0);
                graph.getViewport().setMaxY(16);
                graph.getViewport().setXAxisBoundsManual(true);
                graph.getViewport().setMinX(0);
                graph.getViewport().setMaxX(24);
                // enable scaling and scrolling
                graph.getViewport().setScrollable(true ); // enables horizontal scrolling
                graph.getViewport().setScrollableY(true); // enables vertical scrolling
                graph.getViewport().setScalable(true); // enables horizontal zooming and scrolling
                graph.getViewport().setScalableY(false); // enables vertical zooming and scrolling
                graph.addSeries(series);
            } catch (Exception e) {
                Log.w("TAG","exception at graph");
            }
            series.setOnDataPointTapListener(new OnDataPointTapListener() {
                @Override
                public void onTap(Series series, DataPointInterface dataPoint) {
                    double temp =dataPoint.getX();
                    int temp1=(int)temp;
                    double temp2=temp-temp1;
                    float min=(float)temp2*60;
                    if(temp1>=12) {
                        if(temp>12)
                        temp1 = temp1 - 12;
                        Toast.makeText(SelectiveData.this, "Height in foot is : " + dataPoint.getY() + " Time:" + temp1 + ":" + (int) min+"pm", Toast.LENGTH_SHORT).show();
                    }
                    else
                    Toast.makeText(SelectiveData.this, "Height in foot is : " + dataPoint.getY() + " Time:" + temp1 + ":" + (int) min+"am", Toast.LENGTH_SHORT).show();
                }
            });
        }
        @Override
        public void onCancelled(DatabaseError error) {
            // Failed to read value
            Log.w("TAG", "Failed to read value.", error.toException());
        }
    });
}
private void updateRead(){
    DATE.setText(seldate);

}

    @Override
    protected void onStart() {
        super.onStart();
        getIncomingIntent();
        formGraph();
        updateRead();
    }
}
