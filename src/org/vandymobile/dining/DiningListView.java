package org.vandymobile.dining;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DiningListView extends ListActivity {
    public static class ViewHolder {
        public TextView tvTitle, tvDesc, tvDist;
        private ImageView imgView;
        }
    private static DatabaseHelper myDbHelper;
    private static SQLiteDatabase diningDatabase;
    private String[] adapterInput;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dining_list_view);
        
        
        myDbHelper = new DatabaseHelper(this);
         
        try {
            myDbHelper.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }
         
        try {
            myDbHelper.openDataBase();
        } catch(SQLException sqle) {
            throw sqle;
        }
        diningDatabase = myDbHelper.getReadableDatabase();
        
        String[] tmp2 = {"_id"};
        Cursor adapterCursor = diningDatabase.query("dining", tmp2, null, null, null, null, null);
        adapterInput = new String[adapterCursor.getCount()];
        adapterCursor.close();
        
        setListAdapter(new IconicAdapter(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_dining_list_view, menu);
        return true;
    }

    public void homeClick(View v){
        //nothing here
    }
    public void mapsClick(View v){
        Intent _int = new Intent(getApplicationContext(), DiningMap.class);
        startActivity(_int);
    }
    public void menuClick(View v){
        //TODO implement this
    }
    public void happyClick(View v){
        //TODO implement this
    }
    
    
    class IconicAdapter extends ArrayAdapter<String> { 
        Activity context;

        IconicAdapter(Activity context) {
            super(context, R.layout.row, adapterInput);
            this.context=context; 
            }

        public View getView(int position, View convertView, ViewGroup parent) { 
            ViewHolder holder;
            if(convertView==null){
                LayoutInflater inflater=context.getLayoutInflater();
                convertView=inflater.inflate(R.layout.row, null);
                holder = new ViewHolder();
                holder.tvTitle = (TextView) convertView.findViewById(R.id.tvtitle);
                holder.tvDesc = (TextView) convertView.findViewById(R.id.tvdesc);
                holder.tvDist = (TextView) convertView.findViewById(R.id.tvdist);
                holder.imgView = (ImageView) convertView.findViewById(R.id.image);
                convertView.setTag(holder);
               }
            else {
                  holder = (ViewHolder) convertView.getTag();
              }
            
            String[] tmp = {"name"};
            Cursor _cur = diningDatabase.query("dining", tmp, null, null, null, null, "name");
            
            String tempName = "this is a default value";
            
            _cur.moveToFirst();//initialize the cursor
            _cur.move(position);
            tempName = _cur.getString(0); //grab the name value for the current row
            _cur.close();
            
            holder.tvTitle.setText(tempName);
            holder.tvDesc.setText("Open for 15 more minutes!");//TODO fix temp value
            holder.tvDist.setText("0.2mi away"); //TODO fix temp value
            holder.imgView.setImageResource(R.drawable.compass); //TODO fix temp value

            return(convertView);         
        }
    }

    
    
}