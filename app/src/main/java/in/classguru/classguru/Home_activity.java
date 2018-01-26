package in.classguru.classguru;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

public class Home_activity extends MainActivity {
    AlertDialog alertDialog;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_activity);
        Intent data = getIntent();
        String id = data.getStringExtra("id");
        String permission = data.getStringExtra("permission");
        String dbname = data.getStringExtra("dbname");
        Profile_Activity profile_activity = new Profile_Activity(this);
        
        profile_activity.execute("student",id,permission,dbname);
        /*mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer);
        mToggle = new ActionBarDrawerToggle(this,mDrawerLayout,R.string.open,R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/
    }
}
