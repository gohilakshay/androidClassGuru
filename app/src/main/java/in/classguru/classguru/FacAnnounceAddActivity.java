package in.classguru.classguru;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FacAnnounceAddActivity extends Faculty_Home_Activity {

    TextView et_datepicker1;
    int year_x,month_x,day_x;
    static final int DILOG_ID = 0;
    public Spinner spinner2;
    public String batch_id = "9";
    public Button btn_facAddAnnounc;
    public EditText tv_title;
    public EditText tv_description;
    public TextView tv_sName;
    public TextView tv_sNumb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fac_announce_add);

        tv_title = (EditText)findViewById(R.id.et_NotTitle);
        tv_description = (EditText)findViewById(R.id.et_notDesc);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navSideBar);
        View nav = navigationView.getHeaderView(0);

        tv_sName = (TextView)nav.findViewById(R.id.tvSideName);
        tv_sNumb = (TextView)nav.findViewById(R.id.tvSideNumb);

        mDrawerLayout = (DrawerLayout)findViewById(R.id.FaAnAdd);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Calendar cal = Calendar.getInstance();
        year_x = cal.get(Calendar.YEAR);
        month_x = cal.get(Calendar.MONTH);
        day_x = cal.get(Calendar.DAY_OF_MONTH);
        showDialogOnClick();

        spinner2 = (Spinner)findViewById(R.id.spinner2);
        /*FacAnnouncActivity.FacAnnouncbatch_fetch facAnnouncbatch_fetch = new FacAnnouncActivity.FacAnnouncbatch_fetch(this);
        facAnnouncbatch_fetch.execute("faculty",globalid,globalpermissin,globaldbname);*/
        FacAnnounceAddActivity.FacAnnouncbatch_fetch facAnnouncbatch_fetch = new FacAnnounceAddActivity.FacAnnouncbatch_fetch(this);
        facAnnouncbatch_fetch.execute("faculty",globalid,globalpermissin,globaldbname);

        btn_facAddAnnounc = (Button)findViewById(R.id.btn_FacAddAnnounc);
        btn_facAddAnnounc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                batch_id = spinner2.getSelectedItem().toString();
                String[] batchSeperate = batch_id.split(" ");
                String batch_newId = batchSeperate[0];
                // new AlertDialog.Builder( FacAnnouncActivity.this ).setTitle("Hi").setMessage(batch_newId).show();
                String title = tv_title.getText().toString();
                String description = tv_description.getText().toString();
                String date = year_x+"-"+month_x+"-"+day_x;
                FacAnnounceAddActivity.AddAnnoc_post addAnnoc_post = new FacAnnounceAddActivity.AddAnnoc_post(FacAnnounceAddActivity.this);
                addAnnoc_post.execute("FacAnnouncAdd",title,description,date,batch_newId,globaldbname);
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showDialogOnClick(){
        et_datepicker1 = (TextView)findViewById(R.id.et_datepicker1);

        et_datepicker1.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showDialog(DILOG_ID);

                    }
                }
        );
    }
    @Override
    protected Dialog onCreateDialog(int id){
        if(id == DILOG_ID){
            return new DatePickerDialog(this, dpickerListner ,year_x,month_x,day_x);
        }
        else{
            return null;
        }
    }
    private DatePickerDialog.OnDateSetListener dpickerListner = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayofMonth) {
            year_x = year;
            month_x = monthOfYear + 1;
            day_x = dayofMonth;
            // et_datepicker1.setText(year_x +"/"+month_x+"/"+day_x);
            et_datepicker1.setText(day_x +"/"+month_x+"/"+year_x);
        }
    };



    public class FacAnnouncbatch_fetch extends AsyncTask<String,Void,String> {
        Context context;
        android.app.AlertDialog alertDialog;
        FacAnnouncbatch_fetch (Context ctx){
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
                return "faculty";
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPreExecute() {
            alertDialog = new android.app.AlertDialog.Builder(context).create();
            super.onPreExecute();
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            JSONObject reader = null;
            try {
                reader = new JSONObject(result);
                JSONArray fulltest = reader.getJSONArray("batch_details");
                List<String> facStudAttendModelList = new ArrayList<>();
                facStudAttendModelList.add("Select batch");
                for(int i =0;i<fulltest.length();i++){

                    JSONObject reader1 = new JSONObject(fulltest.getString(i));
                    facStudAttendModelList.add(reader1.getString("batch_ID") +" "+reader1.getString("batch_name") );
                    //  facStudAttendModelList.add(reader1.getString("batch_name"));
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.support_simple_spinner_dropdown_item,facStudAttendModelList);
                adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                spinner2.setAdapter(adapter);
                /*alertDialog.setMessage(reader1.getString("batch_name"));
                alertDialog.show();*/

                tv_sName.setText(globalname);
                tv_sNumb.setText(globalnumb);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


    }
    public class AddAnnoc_post extends AsyncTask<String,Void,String>{

        Context context;
        android.app.AlertDialog alertDialog;
        AddAnnoc_post (Context ctx){
            context = ctx;
        }

        @Override
        protected String doInBackground(String... params) {

            String type = params[0];
            String title = params[1];
            String description = params[2];
            String date = params[3];
            String batch_id = params[4];
            String dbname = params[5];
            String login_url = "https://classes.classguru.in/api/FacmarkNotice.php";
            if(type.equals("FacAnnouncAdd")){
                try {
                    URL profile_url = new URL(login_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) profile_url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String post_data = URLEncoder.encode("batch_id","UTF-8")+"="+URLEncoder.encode(batch_id,"UTF-8")+"&"
                            +URLEncoder.encode("dbname","UTF-8")+"="+URLEncoder.encode(dbname,"UTF-8")+"&"
                            +URLEncoder.encode("date","UTF-8")+"="+URLEncoder.encode(date,"UTF-8")+"&"
                            +URLEncoder.encode("description","UTF-8")+"="+URLEncoder.encode(description,"UTF-8")+"&"
                            +URLEncoder.encode("title","UTF-8")+"="+URLEncoder.encode(title,"UTF-8")+"&"
                            +URLEncoder.encode("t_ID","UTF-8")+"="+URLEncoder.encode(globalid,"UTF-8");
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
                return "Stud";
            }else{
                return "faculty";
            }
            // return null;
        }
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPreExecute() {
            alertDialog = new android.app.AlertDialog.Builder(context).create();
            super.onPreExecute();
        }
        @Override
        protected void onPostExecute(String result) {

            if(result.equals("  New record created successfully")){
                alertDialog.setMessage("Notice Added successfully");
                alertDialog.show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        startActivity(getIntent());

                    }
                }, 500);
            }else{
                alertDialog.setMessage("Error Occured"+result);
                alertDialog.show();
            }

            super.onPostExecute(result);
        }

    }
}