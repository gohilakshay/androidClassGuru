package in.classguru.classguru;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import in.classguru.classguru.models.FacStudAttendModel;

public class FacStudAttendActivity extends Faculty_Home_Activity {

    public Spinner spinner;
    public TextView et_datepicker;
    int year_x,month_x,day_x;
    static final int DILOG_ID = 0;
    //Button btn_markStud;
    LinearLayout Ll_StudDetails;
    public ListView lv_facStudMark;
    public Button btn_markAbsent;
    public CheckBox cb_studAttend;
    public String batch_id;
    public TextView tv_sName;
    public TextView tv_sNumb;
    private HashMap<String, String> spinnerMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fac_stud_attend);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navSideBar);
        View nav = navigationView.getHeaderView(0);

        tv_sName = (TextView)nav.findViewById(R.id.tvSideName);
        tv_sNumb = (TextView)nav.findViewById(R.id.tvSideNumb);

        mDrawerLayout = (DrawerLayout)findViewById(R.id.FaStudAt);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        android.support.v7.app.ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#148388")));

        spinner = (Spinner) findViewById(R.id.spinner);
        //btn_markStud = (Button)findViewById(R.id.btn_markStud);
        btn_markAbsent = (Button)findViewById(R.id.btn_markAbsent);
        Ll_StudDetails = (LinearLayout)findViewById(R.id.Ll_StudDetails);

        Ll_StudDetails.setVisibility(Ll_StudDetails.INVISIBLE);

        lv_facStudMark = (ListView)findViewById(R.id.lv_facStudMark);
        /*btn_markStud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Ll_StudDetails.setVisibility(Ll_StudDetails.VISIBLE);
                FacStudAttend_fetchbatch facStudAttend_fetchbatch = new FacStudAttend_fetchbatch(FacStudAttendActivity.this);
                batch_id = spinner.getSelectedItem().toString();
                facStudAttend_fetchbatch.execute("facultyBatch", batch_id,globaldbname);
            }
        });*/

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                if(!spinner.getSelectedItem().toString().equals("Select batch")){
                    Ll_StudDetails.setVisibility(Ll_StudDetails.VISIBLE);
                    FacStudAttend_fetchbatch facStudAttend_fetchbatch = new FacStudAttend_fetchbatch(FacStudAttendActivity.this);
                    batch_id = spinnerMap.get(spinner.getSelectedItem().toString());
                    facStudAttend_fetchbatch.execute("facultyBatch", batch_id,globaldbname);

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });


        FacStudAttend_fetch facStudAttend_fetch = new FacStudAttend_fetch(this);
        facStudAttend_fetch.execute("faculty",globalid,globalpermissin,globaldbname);

        final Calendar cal = Calendar.getInstance();
        year_x = cal.get(Calendar.YEAR);
        month_x = cal.get(Calendar.MONTH);
        day_x = cal.get(Calendar.DAY_OF_MONTH);
        TextView et_datePicker = findViewById(R.id.et_datePicker);
        //et_datePicker.setText(year_x+"-"+month_x+"-"+day_x);
        et_datePicker.setText(day_x+"/"+month_x+"/"+year_x);
        showDialogOnClick();

        btn_markAbsent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // MarkAbset_post markAbset_post = new MarkAbset_post(FacStudAttendActivity.this);
               // markAbset_post.execute("hello");
                List<String> studAbsentList = new ArrayList<>();
                List<String> studlist = new ArrayList<>();
                for(int i=0;i<lv_facStudMark.getChildCount();i++){
                    View view1 = (View)lv_facStudMark.getChildAt(i);
                    CheckBox check = (CheckBox) view1.findViewById(R.id.cb_StudAttend);
                    if(check.isChecked()){
                        studAbsentList.add(check.getTag().toString());
                        studlist.add(check.getTag().toString());
                    }else{
                        studlist.add(check.getTag().toString());
                    }
                }
                String idsAbsent = android.text.TextUtils.join(",", studAbsentList);
                String studIds = android.text.TextUtils.join(",",studlist);
                String attendDate = year_x+"-"+month_x+"-"+day_x;
                 MarkAbset_post markAbset_post = new MarkAbset_post(FacStudAttendActivity.this);
                 markAbset_post.execute("markAttend",batch_id,attendDate,studIds,idsAbsent,globaldbname);

                /*new AlertDialog.Builder( FacStudAttendActivity.this )
                .setMessage(attendDate)
                .show();*/

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
    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(this,Faculty_Home_Activity.class);
        intent.putExtra("id",globalid);
        intent.putExtra("permission",globalpermissin);
        intent.putExtra("dbname",globaldbname);
        startActivity(intent);
        finish();
    }
    public void showDialogOnClick(){
        et_datepicker = (TextView)findViewById(R.id.et_datePicker);

        et_datepicker.setOnClickListener(
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
           // et_datepicker.setText(year_x +"/"+month_x+"/"+day_x);
            et_datepicker.setText(day_x +"/"+month_x+"/"+year_x);
        }
    };

    public class FacStudAttend_fetch extends AsyncTask<String,Void,String>{
        Context context;
        android.app.AlertDialog alertDialog;
        FacStudAttend_fetch (Context ctx){
            context = ctx;
        }

        @Override
        protected String doInBackground(String... params) {
            String type = params[0];
            String id = params[1];
            String permission = params[2];
            String dbname = params[3];
            String login_url = "https://www.classguru.in/class/api/teacher_details.php";
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
                ArrayList newBatch = new ArrayList();
                List<String> facStudAttendModelList = new ArrayList<>();
                facStudAttendModelList.add("Select batch");
                spinnerMap = new HashMap<String, String>();
                for(int i =0;i<fulltest.length();i++){

                    JSONObject reader1 = new JSONObject(fulltest.getString(i));
                    facStudAttendModelList.add(reader1.getString("batch_name"));
                    spinnerMap.put(reader1.getString("batch_name"),reader1.getString("batch_ID"));
                  //  facStudAttendModelList.add(reader1.getString("batch_name"));
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,R.layout.support_simple_spinner_dropdown_item,facStudAttendModelList);
                adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);

                tv_sName.setText(globalname);
                tv_sNumb.setText(globalnumb);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


    }
    public class FacStudAttend_fetchbatch extends AsyncTask<String,Void,String>{

        Context context;
        android.app.AlertDialog alertDialog;
        FacStudAttend_fetchbatch (Context ctx){
            context = ctx;
        }

        @Override
        protected String doInBackground(String... params) {
            String type = params[0];
            String id = params[1];
           // String permission = params[2];
            String dbname = params[2];
            String login_url = "https://www.classguru.in/class/api/student_batch_mapping.php";
            if(type.equals("facultyBatch")){
                try {
                    URL profile_url = new URL(login_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) profile_url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String post_data = URLEncoder.encode("batch_id","UTF-8")+"="+URLEncoder.encode(id,"UTF-8")+"&"
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
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);

        }

        @Override
        protected void onPostExecute(String result) {
            //super.onPostExecute(result);
            String dateSelect = day_x +"/"+month_x+"/"+year_x;
            try {
                JSONObject reader = new JSONObject(result);
                String reader1 = reader.getString("stud_details");
                if(reader1.equals("student batch id not found")){
                    alertDialog.setMessage("No students in selected Batch");
                    alertDialog.show();
                    Ll_StudDetails.setVisibility(Ll_StudDetails.INVISIBLE);
                }else{
                    JSONArray fulltest = reader.getJSONArray("stud_details");
                    List<FacStudAttendModel> facStudAttendModelList = new ArrayList<>();

                    for (int i=0;i<fulltest.length();i++){
                        JSONObject finalresult = fulltest.getJSONObject(i);
                        FacStudAttendModel facStudAttendModel = new FacStudAttendModel();
                        facStudAttendModel.setStudid(finalresult.getString("stud_ID"));
                        facStudAttendModel.setStudName(finalresult.getString("stud_name") +" "+ finalresult.getString("stud_surname"));
                        facStudAttendModelList.add(facStudAttendModel);
                    }

                    FacStudAttendActivity.FacStudAttendMarkAdapter facStudAttendMarkAdapter = new FacStudAttendActivity.FacStudAttendMarkAdapter(getApplicationContext(),R.layout.facstudattend_mark_layout,facStudAttendModelList);
                    lv_facStudMark.setAdapter(facStudAttendMarkAdapter);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    public class FacStudAttendMarkAdapter extends ArrayAdapter{
        private List<FacStudAttendModel> facStudAttendModelList;
        private  int resource;
        private LayoutInflater inflater;

        public FacStudAttendMarkAdapter(@NonNull Context context, int resource, @NonNull List<FacStudAttendModel> objects) {
            super(context, resource, objects);
            facStudAttendModelList = objects;
            this.resource = resource;
            inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(resource, null);
            }
            TextView tv_stud_id = (TextView)convertView.findViewById(R.id.tv_studMark_id);
            TextView tv_stud_name = (TextView)convertView.findViewById(R.id.tv_studMark_name);

            cb_studAttend = (CheckBox)convertView.findViewById(R.id.cb_StudAttend);

            tv_stud_id.setText(facStudAttendModelList.get(position).getStudid());
            tv_stud_name.setText(facStudAttendModelList.get(position).getStudName());
            cb_studAttend.setTag(facStudAttendModelList.get(position).getStudid());

            return convertView;
        }
    }


    public class MarkAbset_post extends AsyncTask<String,Void,String>{
        Context context;
        android.app.AlertDialog alertDialog;
        MarkAbset_post (Context ctx){
            context = ctx;
        }

        @Override
        protected String doInBackground(String... params) {
            String type = params[0];
            String batch_id = params[1];
             String attendDate = params[2];
             String studIds = params[3];
             String idsAbsent = params[4];
             String dbname = params[5];
            String login_url = "https://www.classguru.in/class/api/FacmarkStudAttend.php";
            if(!attendDate.equals("Select Date")){
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
                            +URLEncoder.encode("attendDate","UTF-8")+"="+URLEncoder.encode(attendDate,"UTF-8")+"&"
                            +URLEncoder.encode("studIds","UTF-8")+"="+URLEncoder.encode(studIds,"UTF-8")+"&"
                            +URLEncoder.encode("idsAbsent","UTF-8")+"="+URLEncoder.encode(idsAbsent,"UTF-8");
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
                return "enterdate";
            }
            //return null;
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
                alertDialog.setMessage("Attendance marked successfully");
                alertDialog.show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                        startActivity(getIntent());

                    }
                }, 500);
            }else if(result.equals("enterdate")){
                alertDialog.setMessage("Enter Date to Mark");
                alertDialog.show();
            }else{
                alertDialog.setMessage("Error Occured");
                alertDialog.show();
            }

            super.onPostExecute(result);
        }
    }
}
