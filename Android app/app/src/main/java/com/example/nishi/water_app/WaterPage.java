package com.example.nishi.water_app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ProgressBar;
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

import java.util.Calendar;
import java.util.Date;

import static com.example.nishi.water_app.MainActivity.MyPREFERENCES;

public class WaterPage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth mAuth;
    GraphView graph;
    Button next;
    DataPoint[] dp;
    double[][] graphtemp;
    private ProgressBar prog;
    TextView height, liters;
    final String TAG = "TAG";
    DatabaseReference myRef, ref2;
    Date dt = new Date();
    Calendar c =Calendar.getInstance();
    String search=(dt.getMonth()+1)+"-"+dt.getDate()+"-"+(c.get(Calendar.YEAR));
    {
        myRef = database.getReference("Distance");
        ref2=database.getReference("logs/"+search);
    }
    private static final String DEBUG_TAG = "Gestures";
    public WaterPage() {

    }
    public WaterPage(FirebaseDatabase database) {
        this.database = database;
    }
    private GestureDetectorCompat mDetector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        graph = (GraphView) findViewById(R.id.selgraph);
        height=findViewById(R.id.height);
        liters=findViewById(R.id.liters);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        mAuth = FirebaseAuth.getInstance();
        prog=findViewById(R.id.progressBar);
        prog.setVisibility(View.GONE);
        // Instantiate the gesture detector with the
        // application context and an implementation of
        // GestureDetector.OnGestureListener
        mDetector = new GestureDetectorCompat(this,this);
        // Set the gesture detector as the double tap
        // listener.
        mDetector.setOnDoubleTapListener(this);
        next=findViewById(R.id.previousData);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(WaterPage.this,PrevData.class);
                startActivity(i);
            }
        });
        SharedPreferences prefs = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Log.d(TAG, "onCreate: "+prefs.getString("username",""));
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu nav_Menu = navigationView.getMenu();
        String temp=prefs.getString("username","");
        Log.d(TAG, "onCreate: "+temp.equals("admin"));
        if(temp.equals("admin")) {
            Log.d(TAG, "onCreate: user is admin");
        }else{
            nav_Menu.findItem(R.id.nav_gallery).setVisible(false);
        }
    }
    @Override
    protected void onStart() {
        Log.d(TAG,search);
        super.onStart();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Double value = dataSnapshot.getValue(Double.class);
                int lit = (int) (14000 *(value/6.15));
                height.setText(value.toString());
                liters.setText(String.valueOf(lit)+" ("+(int)((value/6.15)*100)+"%)");
                Log.d(TAG, "Value is: " + value);
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        ref2.addValueEventListener(new ValueEventListener() {
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
                    Log.w(TAG,"logging :"+ a);
                    graphtemp[index][0]=a;
                    graphtemp[index][1]=Double.parseDouble(Value.toString());
                    index++;
                }
                    dp=new DataPoint[graphtemp.length];
                for ( int k = 0; k < graphtemp.length; k++)
            //       dp[k] = new DataPoint(graphtemp[k][0], graphtemp[k][1]);
                   Log.w(TAG,graphtemp[k][0]+ " "+graphtemp[k][1]);
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
                for ( int k = 0; k < graphtemp.length; k++)
                        dp[k] = new DataPoint(graphtemp[k][0], graphtemp[k][1]);
                    //Log.w(TAG,"sorted"+graphtemp[k][0]+ " "+graphtemp[k][1]);
                LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dp);
                try {
                    graph.getViewport().setYAxisBoundsManual(true);
                    graph.getViewport().setMinY(0);
                    graph.getViewport().setMaxY(10);
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
                    Log.w(TAG,"exception at graph");
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
                            Toast.makeText(WaterPage.this, "Height in foot is : " + dataPoint.getY() + " time:" + temp1 + ":" + (int) min+"pm", Toast.LENGTH_SHORT).show();
                        }
                        else
                            Toast.makeText(WaterPage.this, "Height in foot is : " + dataPoint.getY() + " time:" + temp1 + ":" + (int) min+"am", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            moveTaskToBack(true);
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.display, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            mAuth.signOut();
            Intent i=new Intent(this,MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
            Intent i=new Intent(this,WaterPage.class);
            startActivity(i);
        } else if (id == R.id.nav_gallery) {
        Intent i=new Intent(this,MotorControl.class);
        startActivity(i);
        }else if(id==R.id.nav_send){
            Intent in=new Intent(this,AboutDeveloper.class);
            startActivity(in);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    @Override
    public boolean onTouchEvent(MotionEvent event){
        if (this.mDetector.onTouchEvent(event)) {
            return true;
        }
        return super.onTouchEvent(event);
    }


    @Override
    public boolean onDown(MotionEvent e) {
        prog.setVisibility(View.VISIBLE);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Double value = dataSnapshot.getValue(Double.class);
                int lit=(int)(1000/value);
                height.setText(value.toString());
                liters.setText(String.valueOf(lit));
                Log.d(TAG, "Value is: " + value);
                prog.setVisibility(View.GONE);
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }
}
