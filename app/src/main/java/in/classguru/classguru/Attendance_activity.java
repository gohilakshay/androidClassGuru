package in.classguru.classguru;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

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
import java.util.List;

import in.classguru.classguru.models.AttendanceModel;

public class Attendance_activity extends Home_activity {

    public ListView lv_attend;
    public DrawerLayout mDrawerLayout;
    public ActionBarDrawerToggle mToggle;
    public TextView tvSidenumb;
    public TextView tvSidename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_activity);
        Attendance_fetch attendance_fetch = new Attendance_fetch(this);
        attendance_fetch.execute("student",globalid,globalpermissin,globaldbname);

        lv_attend = (ListView)findViewById(R.id.lv_attend);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.attendDrawer);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navSideBar);
        View nav = navigationView.getHeaderView(0);

        tvSidename = (TextView)nav.findViewById(R.id.tvSideName);
        tvSidenumb = (TextView)nav.findViewById(R.id.tvSideNumb);

        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class Attendance_fetch extends AsyncTask<String,Void,List<AttendanceModel>>{
        Context context;
        android.app.AlertDialog alertDialog;
        Attendance_fetch (Context ctx){
            context = ctx;
        }
        @Override
        protected List<AttendanceModel> doInBackground(String... params) {
            String type = params[0];
            String id = params[1];
            String permission = params[2];
            String dbname = params[3];
            String login_url = "https://classes.classguru.in/api/student_details.php";
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
                    String fullattend="";
                    String[] separated = new String[0];
                    JSONObject reader = new JSONObject(result);
                    //String checkResult = reader.getString("std_attendance");
                    reader.getJSONArray("std_attendance");
                    List<AttendanceModel> attendList = new ArrayList<>();
                    for(int i=0;i<reader.getJSONArray("std_attendance").length();i++){
                        AttendanceModel attendanceModel = new AttendanceModel();

                        fullattend = reader.getJSONArray("std_attendance").getString(i);
                        if(!fullattend.equals("Match not found")) {
                            separated = fullattend.split(",");
                            attendanceModel.setAttendance(separated[0]);
                            attendanceModel.setDate(separated[1]);
                            attendList.add(attendanceModel);
                        }
                        /*attendanceModel.setAttendance();*/
                    }

                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                    return attendList;

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }else{
                List<AttendanceModel> attendList = new ArrayList<>();
                return attendList;
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            alertDialog = new android.app.AlertDialog.Builder(context).create();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(List<AttendanceModel> result) {
            super.onPostExecute(result);
            //TODO need to set data to the list
            AttendAdapter attendAdapter = new AttendAdapter(getApplicationContext(),R.layout.attendance_viewlayout,result);
            lv_attend.setAdapter(attendAdapter);
             /*alertDialog.setMessage(result);
            alertDialog.show();*/

            tvSidename.setText(globalname);
            tvSidenumb.setText(globalnumb);
            ImageView ivsprofile = (ImageView)findViewById(R.id.iv_sProfile);

            // Then later, when you want to display image
            ImageLoader.getInstance().displayImage("https://classes.classguru.in/"+globalurl, ivsprofile);

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }


    public class AttendAdapter extends ArrayAdapter{
        private  List<AttendanceModel> attendanceModelList;
        private  int resource;
        private LayoutInflater inflater;
        public AttendAdapter(@NonNull Context context, int resource, @NonNull List<AttendanceModel> objects) {
            super(context, resource, objects);
            attendanceModelList = objects;
            this.resource = resource;
            inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if(convertView == null){
                convertView = inflater.inflate(resource,null);
            }
            TextView tv_attend;
            TextView tv_date;
            tv_attend = (TextView)convertView.findViewById(R.id.tv_Attend);
            tv_date = (TextView)convertView.findViewById(R.id.tv_Date);
            tv_attend.setText(attendanceModelList.get(position).getAttendance());
            tv_date.setText(attendanceModelList.get(position).getDate());
            return convertView;

        }
    }
}
