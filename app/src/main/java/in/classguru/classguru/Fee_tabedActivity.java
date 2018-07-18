package in.classguru.classguru;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

public class Fee_tabedActivity extends Home_activity implements Fee_Frag1.OnFragmentInteractionListener,Fee_Frag2.OnFragmentInteractionListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    public DrawerLayout mDrawerLayout;
    public ActionBarDrawerToggle mToggle;
    public TextView tvSidename;
    public TextView tvSidenumb;
    public ImageView ivsprofile;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fee_tabed);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        mDrawerLayout = (DrawerLayout)findViewById(R.id.main_content_feeDrawer);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        android.support.v7.widget.Toolbar mtoolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbarFee);
        mtoolbar.setNavigationIcon(R.drawable.ic_navigation);
        setSupportActionBar(mtoolbar);

        /*getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        android.support.v7.app.ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#148388")));*/

        NavigationView navigationView = (NavigationView) findViewById(R.id.navSideBar);
        navigationView.setItemIconTintList(null);
        View nav = navigationView.getHeaderView(0);

        tvSidename = (TextView)nav.findViewById(R.id.tvSideName);
        tvSidenumb = (TextView)nav.findViewById(R.id.tvSideNumb);

        /*tvSidename.setText(globalname);
        tvSidenumb.setText(globalnumb);

        ivsprofile = (ImageView)findViewById(R.id.iv_sProfile);
        ImageLoader.getInstance().displayImage("https://classes.classguru.in/"+globalurl, ivsprofile);
        */
        Intent data = getIntent();
        String globalname1 = data.getStringExtra("globalname");
        String globalnumb1 = data.getStringExtra("globalnumb");
        String globalurl1 = data.getStringExtra("globalurl");
        tvSidename.setText(globalname1);
        tvSidenumb.setText(globalnumb1);
        ivsprofile = (ImageView)nav.findViewById(R.id.iv_sProfile);
        ImageLoader.getInstance().displayImage("http://206.189.231.53/admin/"+globalurl1, ivsprofile);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(this,Home_activity.class);
        intent.putExtra("id",globalid);
        intent.putExtra("permission",globalpermissin);
        intent.putExtra("dbname",globaldbname);
        startActivity(intent);
        finish();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_fee_tabed, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (position) {
                case 0:

                    fragment = Fee_Frag1.newInstance(globalid,globaldbname);
                    break;
                case 1:
                    fragment = Fee_Frag2.newInstance(globalid,globaldbname);
                    break;
            }
            return  fragment;
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }
    }
}
