package in.classguru.classguru;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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
import java.util.List;

import in.classguru.classguru.models.FacAttendModel;

public class FacAttendActivity extends Faculty_Home_Activity {

    public DrawerLayout mDrawerLayout;
    public ActionBarDrawerToggle mToggle;
    public ListView lv_facAttend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fac_attend);

        lv_facAttend = (ListView)findViewById(R.id.lv_facAttend);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navSideBar);
        View nav = navigationView.getHeaderView(0);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.FaAt);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Fac_attend_fetch fac_attend_fetch = new Fac_attend_fetch(this);
        fac_attend_fetch.execute("faculty",globalid,globalpermissin,globaldbname);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class Fac_attend_fetch extends AsyncTask<String,Void,String>{
        Context context;
        android.app.AlertDialog alertDialog;
        Fac_attend_fetch (Context ctx){
            context = ctx;
        }
        @Override
        protected String doInBackground(String... params) {
            String type = params[0];
            String id = params[1];
            String permission = params[2];
            String dbname = params[3];
            String login_url = "https://classes.classguru.in/class/api/teacher_details.php";
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
        protected void onPreExecute() {
            alertDialog = new android.app.AlertDialog.Builder(context).create();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                JSONObject reader = new JSONObject(result);
                JSONArray fulltest = reader.getJSONArray("techrAttd_details");

                List<FacAttendModel> facAttendModelList = new ArrayList<>();

                //String attend = fulltest.getString(0);
                /*alertDialog.setMessage(attend);
                  alertDialog.show();*/
                for(int i=0;i<fulltest.length();i++){
                    FacAttendModel facAttendModel = new FacAttendModel();
                    String attend = fulltest.getString(i);
                    if(!attend.equals("teacher id not found")){
                        String[] separated = attend.split(",");
                        facAttendModel.setAttend(separated[0]);
                        facAttendModel.setAttendDate(separated[1]);
                        facAttendModelList.add(facAttendModel);
                    }

                }
                FacAttendActivity.FacAttendAdapter facAttendAdapter = new FacAttendActivity.FacAttendAdapter(getApplicationContext(),R.layout.facattend_layout,facAttendModelList);
                lv_facAttend.setAdapter(facAttendAdapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }

    public class FacAttendAdapter extends ArrayAdapter{

        private List<FacAttendModel> facAttendModelList;
        private  int resource;
        private LayoutInflater inflater;
        public FacAttendAdapter(@NonNull Context context, int resource, @NonNull List<FacAttendModel> objects) {
            super(context, resource, objects);
            facAttendModelList = objects;
            this.resource =resource;
            inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        }
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if(convertView == null){
                convertView = inflater.inflate(resource,null);
            }
            TextView tv_facdate = (TextView)convertView.findViewById(R.id.tv_FacDate);
            TextView tv_facattend = (TextView)convertView.findViewById(R.id.tv_FacAttend);

            tv_facdate.setText(facAttendModelList.get(position).getAttendDate());
            tv_facattend.setText(facAttendModelList.get(position).getAttend());

            return convertView;

        }
    }
}
