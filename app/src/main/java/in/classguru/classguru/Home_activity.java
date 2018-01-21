package in.classguru.classguru;

import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;

public class Home_activity extends MainActivity {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_activity);
        /*mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer);
        mToggle = new ActionBarDrawerToggle(this,mDrawerLayout,R.string.open,R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/
    }
}
