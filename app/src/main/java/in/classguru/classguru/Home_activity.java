package in.classguru.classguru;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class Home_activity extends AppCompatActivity {

    String globalid;
    String globalpermissin;
    String globaldbname;
    String globalname;
    String globalnumb;
    String globalurl;


    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;


    private TextView tvname;
    private TextView tvbatch;
    private TextView tvtiming;
    private TextView tvyear;
    private TextView tvfather;
    private TextView tvmother;
    private TextView tvcontact;
    private TextView tvcourse;
    private TextView tvfullname;
    private TextView tvScontact;
    private TextView tvgender;
    private TextView tvdob;
    private TextView tvcouType;
    private TextView tvstandard;
    private TextView tvSidename;
    private TextView tvSidenumb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // Create default options which will be used for every
        //  displayImage(...) call if no options will be passed to this method
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
           .cacheInMemory(true)
                .cacheOnDisk(true)
           .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
           .defaultDisplayImageOptions(defaultOptions)
           .build();
        ImageLoader.getInstance().init(config); // Do it on Application start


        setContentView(R.layout.activity_home_activity);
        Intent data = getIntent();
        String id = data.getStringExtra("id");
            globalid = id;
        String permission = data.getStringExtra("permission");
            globalpermissin = permission;
        String dbname = data.getStringExtra("dbname");
            globaldbname = dbname;
        Profile_Activity profile_activity = new Profile_Activity(this);

        tvname = (TextView)findViewById(R.id.tvName);
        tvbatch = (TextView)findViewById(R.id.tvBatch);
        tvtiming = (TextView)findViewById(R.id.tvTiming);
        tvyear = (TextView)findViewById(R.id.tvYear);
        tvfather = (TextView)findViewById(R.id.tvFather);
        tvmother = (TextView)findViewById(R.id.tvMother);
        tvcontact = (TextView)findViewById(R.id.tvContact);
        tvcourse = (TextView)findViewById(R.id.tvCourse);
        tvgender = (TextView)findViewById(R.id.tvGender);
        tvdob = (TextView)findViewById(R.id.tvBirth);
        tvcouType = (TextView)findViewById(R.id.tvCourseType);
        tvstandard = (TextView)findViewById(R.id.tvStandard);

        tvfullname = (TextView)findViewById(R.id.tv_name);
        tvScontact = (TextView)findViewById(R.id.tv_numb);

        profile_activity.execute("student",id,permission,dbname);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navSideBar);
        View nav = navigationView.getHeaderView(0);
        tvSidename = (TextView)nav.findViewById(R.id.tvSideName);
        tvSidenumb = (TextView)nav.findViewById(R.id.tvSideNumb);

        ImageView img = (ImageView)findViewById(R.id.iv_nav);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer);
                mDrawerLayout.openDrawer(Gravity.LEFT);
            }
        });


    }


    /**
     * Created by a2z on 1/25/2018.
     */
    public class Profile_Activity extends AsyncTask<String,Void,String> {
        Context context;
        android.app.AlertDialog alertDialog;
        public ImageView im_profile;
        public ImageView iv_sProfile;

        Profile_Activity (Context ctx){
            context = ctx;
        }

        private ProgressDialog dialog = new ProgressDialog(Home_activity.this);
        @Override
        protected String doInBackground(String... params) {
            String type = params[0];
            String id = params[1];
            String permission = params[2];
            String dbname = params[3];
            String login_url = "https://www.classguru.in/class/api/student_details.php";
            if(permission.equals("student")){
                try {
                    URL profile_url = new URL(login_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) profile_url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String post_data = URLEncoder.encode("userId","UTF-8")+"="+URLEncoder.encode(id,"UTF-8")+"&"
                            +URLEncoder.encode("dbname","UTF-8")+"="+URLEncoder.encode(dbname,"UTF-8");
                    bufferedWriter.write(post_data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                    String result="";
                    String line="";
                    while ((line = bufferedReader.readLine()) != null){
                        result += line;
                    }
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                    return result;

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }else{
                return "faculty";
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            alertDialog = new android.app.AlertDialog.Builder(context).create();
            this.dialog.setMessage("Please wait");
            this.dialog.show();
            super.onPreExecute();
        }
        /*private String name;*/
        @Override
        protected void onPostExecute(String result) {
            try {
                if(!result.equals("faculty")) {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    JSONObject reader = new JSONObject(result);
                    String checkResult = reader.getString("student_details");

                    JSONObject finalresult = new JSONObject(checkResult);
                    String surname = finalresult.getString("stud_surname");
                    String stud_name = finalresult.getString("stud_name");
                    tvname.setText(stud_name);
                    String father = finalresult.getString("father_name");
                    tvfather.setText(father);
                    String mother = finalresult.getString("mother_name");
                    tvmother.setText(mother);
                    String pcontact = finalresult.getString("pcontactnumber");
                    tvcontact.setText(pcontact);
                    String year = finalresult.getString("admission_year");
                    tvyear.setText(year);
                    String scontact = finalresult.getString("stud_contact");
                    tvScontact.setText(scontact);
                    tvSidenumb.setText(scontact);
                    globalnumb = scontact;
                    String fullname = stud_name + " " + surname;
                    tvfullname.setText(fullname);
                    tvSidename.setText(fullname);
                    globalname = fullname;

                    String gender = finalresult.getString("stud_gender");
                    tvgender.setText(gender);
                    String dob = finalresult.getString("stud_dob");
                    String[] date = dob.split("-");
                    tvdob.setText(date[2]+"-"+date[1]+"-"+date[0]);
                    String couType = finalresult.getString("course_type");
                    tvcouType.setText(couType);
                    String standard = finalresult.getString("standard_name");
                    tvstandard.setText(standard);

                    String profile = finalresult.getString("stud_profile");
                    globalurl = profile;

                    String batch_details = reader.getString("batch_details");
                    JSONObject batch = new JSONObject(batch_details);
                    String batchname = batch.getString("batch_name");
                    tvbatch.setText(batchname);
                    String batchtime = batch.getString("batch_timing");
                    tvtiming.setText(batchtime);
                    if(!reader.getString("course_details").equals("student batch id not found")){
                        String course_details = reader.getString("course_details");
                        JSONObject course = new JSONObject(course_details);
                        String coursename = course.getString("course_name");
                        tvcourse.setText(coursename);
                    }else{

                        tvcourse.setText("Not Assigned Yet");
                    }

                    im_profile = (ImageView)findViewById(R.id.iv_profile);
                    iv_sProfile = (ImageView)findViewById(R.id.iv_sProfile);
                    if (im_profile != null ){

                        // Then later, when you want to display image
                        ImageLoader.getInstance().displayImage("https://classes.classguru.in/"+profile, im_profile);
                        ImageLoader.getInstance().displayImage("https://classes.classguru.in/"+profile, iv_sProfile);
                    }


                }
                else{
                       alertDialog.setMessage("Please Login Correctly Student");
                       alertDialog.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }



        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

    }

    public void OnLogout(MenuItem item) {
        AlertDialog.Builder builder=new AlertDialog.Builder(Home_activity.this); //Home is name of the activity
        builder.setMessage("Do you want to exit?");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {

                finish();
                Intent i=new Intent();
                i.putExtra("finish", true);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // To clean up all activities
                finish();

            }
        });

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        AlertDialog alert=builder.create();
        alert.show();
    }
    public void OnFee(MenuItem item){
        Intent intent = new Intent(Home_activity.this,Fee_tabedActivity.class);
        intent.putExtra("id",globalid);
        intent.putExtra("permission",globalpermissin);
        intent.putExtra("dbname",globaldbname);
        intent.putExtra("globalname",globalname);
        intent.putExtra("globalnumb",globalnumb);
        intent.putExtra("globalurl",globalurl);
        startActivity(intent);
        finish();
    }
    public void OnProfile(MenuItem item){
        Intent intent = new Intent(Home_activity.this,Home_activity.class);
        intent.putExtra("id",globalid);
        intent.putExtra("permission",globalpermissin);
        intent.putExtra("dbname",globaldbname);
        startActivity(intent);
        finish();
    }
    public void OnAttend(MenuItem item){
        Intent intent = new Intent(Home_activity.this,Attendance_activity.class);
        intent.putExtra("id",globalid);
        intent.putExtra("permission",globalpermissin);
        intent.putExtra("dbname",globaldbname);
        startActivity(intent);
        finish();
    }
    public void OnTest(MenuItem item){
        Intent intent = new Intent(Home_activity.this,Test_activity.class);
        intent.putExtra("id",globalid);
        intent.putExtra("permission",globalpermissin);
        intent.putExtra("dbname",globaldbname);
        startActivity(intent);
        finish();
    }
    public void OnAssign(MenuItem item){
        Intent intent = new Intent(Home_activity.this,Assignment_activity.class);
        intent.putExtra("id",globalid);
        intent.putExtra("permission",globalpermissin);
        intent.putExtra("dbname",globaldbname);
        startActivity(intent);
        finish();
    }
    public void OnPortion(MenuItem item){
        Intent intent = new Intent(Home_activity.this,Portion_activity.class);
        intent.putExtra("id",globalid);
        intent.putExtra("permission",globalpermissin);
        intent.putExtra("dbname",globaldbname);
        startActivity(intent);
        finish();
    }
    public void OnAnnounc(MenuItem item){
        Intent intent = new Intent(Home_activity.this,Announc_Activity.class);
        intent.putExtra("id",globalid);
        intent.putExtra("permission",globalpermissin);
        intent.putExtra("dbname",globaldbname);
        startActivity(intent);
        finish();
    }


    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press Back Again to Logout", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}
