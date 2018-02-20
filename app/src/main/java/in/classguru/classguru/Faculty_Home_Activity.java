package in.classguru.classguru;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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

public class Faculty_Home_Activity extends AppCompatActivity {

    String globalid;
    String globalpermissin;
    String globaldbname;
    String globalname;
    String globalnumb;
    String globalurl;

    public DrawerLayout mDrawerLayout;
    public ActionBarDrawerToggle mToggle;
    public TextView tv_fullname;
    public TextView tv_facnumb;
    public TextView tv_facname;
    public TextView tv_facfather;
    public TextView tv_facgender;
    public TextView tv_facdob;
    public TextView tv_facqualification;
    public TextView tv_facjoin;
    public TextView tv_facsalary;
    public TextView tv_facpaystatus;
    public TextView tvFacSideName;
    public TextView tvFacSideNumb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty__home_);

        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .build();
        ImageLoader.getInstance().init(config); // Do it on Application start


        Intent data = getIntent();
        String id = data.getStringExtra("id");
        globalid = id;
        String permission = data.getStringExtra("permission");
        globalpermissin = permission;
        String dbname = data.getStringExtra("dbname");
        globaldbname = dbname;
        FacultyProfile_fetch facultyProfile_fetch = new FacultyProfile_fetch(this);
        facultyProfile_fetch.execute("student",id,permission,dbname);

        tv_fullname = (TextView)findViewById(R.id.tv_FacFullname);
        tv_facnumb = (TextView)findViewById(R.id.tv_FacNumb);
        tv_facname = (TextView)findViewById(R.id.tv_FacName);
        tv_facfather = (TextView)findViewById(R.id.tv_FacFather);
        tv_facgender = (TextView)findViewById(R.id.tv_FacGender);
        tv_facdob = (TextView)findViewById(R.id.tv_FacDob);
        tv_facqualification = (TextView)findViewById(R.id.tv_FacQuali);
        tv_facjoin = (TextView)findViewById(R.id.tv_FacJoin);
        tv_facsalary = (TextView)findViewById(R.id.tv_FacSalary);
        tv_facpaystatus = (TextView)findViewById(R.id.tv_FacPayStatus);


        NavigationView navigationView = (NavigationView) findViewById(R.id.navSideBar);
        View nav = navigationView.getHeaderView(0);
        tvFacSideName = (TextView)nav.findViewById(R.id.tvSideName);
        tvFacSideNumb = (TextView)nav.findViewById(R.id.tvSideNumb);


        mDrawerLayout = (DrawerLayout)findViewById(R.id.Facdrawer);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public class FacultyProfile_fetch extends AsyncTask<String,Void,String>{

        Context context;
        android.app.AlertDialog alertDialog;
        /*public ImageView im_profile;
        public ImageView iv_sProfile;*/
        public ImageView im_sFacProfile;
        public ImageView im_facProfile;

        FacultyProfile_fetch (Context ctx){
            context = ctx;
        }

        @Override
        protected String doInBackground(String... params) {
            String type = params[0];
            String id = params[1];
            String permission = params[2];
            String dbname = params[3];
            String login_url = "https://classes.classguru.in/api/teacher_details.php";
            if(permission.equals("faculty")){
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
                return "student";
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            alertDialog = new android.app.AlertDialog.Builder(context).create();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                if(!result.equals("student")) {
                    JSONObject reader = new JSONObject(result);
                    String checkResult = reader.getString("t_details");

                    JSONObject finalresult = new JSONObject(checkResult);
                    String t_name = finalresult.getString("t_name");
                    tv_facname.setText(t_name);
                    String t_fathername = finalresult.getString("t_fathername");
                    tv_facfather.setText(t_fathername);
                    String t_surname = finalresult.getString("t_surname");
                    String t_fullName = t_name+" "+t_surname;
                    tv_fullname.setText(t_fullName);
                    String  t_numb = finalresult.getString("t_contact");
                    tv_facnumb.setText(t_numb);
                    String t_gender = finalresult.getString("t_gender");
                    tv_facgender.setText(t_gender);
                    String t_dob = finalresult.getString("t_dob");
                    tv_facdob.setText(t_dob);
                    String t_quali = finalresult.getString("qualification");
                    tv_facqualification.setText(t_quali);
                    String t_join = finalresult.getString("join_date");
                    tv_facjoin.setText(t_join);
                    String t_salary = finalresult.getString("salary");
                    tv_facsalary.setText(t_salary);
                    String t_paystatus = finalresult.getString("salary_status");
                    tv_facpaystatus.setText(t_paystatus);

                    tvFacSideName.setText(t_fullName);
                    globalname = t_fullName;
                    tvFacSideNumb.setText(t_numb);
                    globalnumb = t_numb;
                    String facultyProfile = finalresult.getString("t_profile");
                    globalurl=facultyProfile;

                    im_sFacProfile = (ImageView)findViewById(R.id.iv_sProfile);
                    im_facProfile = (ImageView)findViewById(R.id.iv_FacProfile);

                    /*if (im_sFacProfile != null ){

                        // Then later, when you want to display image
                        ImageLoader.getInstance().displayImage("https://classes.classguru.in/class/"+facultyProfile, im_sFacProfile);
                        ImageLoader.getInstance().displayImage("https://classes.classguru.in/class/"+facultyProfile, im_facProfile);
                    }*/
                }else{
                    alertDialog.setMessage("Please Login Correctly Faculty");
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
    public void OnFacLogout(MenuItem item) {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }
    public void OnFacProfile(MenuItem item) {
        Intent intent = new Intent(this,Faculty_Home_Activity.class);
        intent.putExtra("id",globalid);
        intent.putExtra("permission",globalpermissin);
        intent.putExtra("dbname",globaldbname);
        startActivity(intent);
        finish();
    }
    public void OnFacSal(MenuItem item) {
        Intent intent = new Intent(this,FacSalaryActivity.class);
        intent.putExtra("id",globalid);
        intent.putExtra("permission",globalpermissin);
        intent.putExtra("dbname",globaldbname);
        startActivity(intent);
        finish();
    }
    public void OnFacAttend(MenuItem item) {
        Intent intent = new Intent(this,FacAttendActivity.class);
        intent.putExtra("id",globalid);
        intent.putExtra("permission",globalpermissin);
        intent.putExtra("dbname",globaldbname);
        startActivity(intent);
        finish();
    }
    public void OnFacStudAttend(MenuItem item) {
        Intent intent = new Intent(this,FacStudAttendActivity.class);
        intent.putExtra("id",globalid);
        intent.putExtra("permission",globalpermissin);
        intent.putExtra("dbname",globaldbname);
        startActivity(intent);
        finish();
    }
    public void OnFacAnnounc(MenuItem item) {
        Intent intent = new Intent(this,FacAnnouncActivity.class);
        intent.putExtra("id",globalid);
        intent.putExtra("permission",globalpermissin);
        intent.putExtra("dbname",globaldbname);
        startActivity(intent);
        finish();
    }
    public void OnFacPortion(MenuItem item) {
        Intent intent = new Intent(this,FacPortionActivity.class);
        intent.putExtra("id",globalid);
        intent.putExtra("permission",globalpermissin);
        intent.putExtra("dbname",globaldbname);
        startActivity(intent);
        finish();
    }

}
