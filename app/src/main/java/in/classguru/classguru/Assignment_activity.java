package in.classguru.classguru;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
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

import in.classguru.classguru.models.AssignModel;
import in.classguru.classguru.models.TestModel;

public class Assignment_activity extends Home_activity {

    public ListView lv_assign;
    public DrawerLayout mDrawerLayout;
    public ActionBarDrawerToggle mToggle;
    public TextView tvSidenumb;
    public TextView tvSidename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment_activity);
        Assign_fetch assign_fetch = new Assign_fetch(this);
        assign_fetch.execute("student",globalid,globalpermissin,globaldbname);
        lv_assign = (ListView)findViewById(R.id.lv_assign);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.assignDrawer);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navSideBar);
        View nav = navigationView.getHeaderView(0);

        tvSidename = (TextView)nav.findViewById(R.id.tvSideName);
        tvSidenumb = (TextView)nav.findViewById(R.id.tvSideNumb);



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
    public class Assign_fetch extends AsyncTask<String,Void,List<AssignModel>> {

        Context context;
        android.app.AlertDialog alertDialog;
        Assign_fetch (Context ctx){
            context = ctx;
        }

        @Override
        protected List<AssignModel> doInBackground(String... params) {
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
                    JSONObject reader = new JSONObject(result);

                    List<AssignModel> assignList = new ArrayList<>();
                    JSONArray fulltest = reader.getJSONArray("assignment");

                    for (int i=0;i<fulltest.length();i++){
                        JSONObject finalObject = fulltest.getJSONObject(i);
                        AssignModel assignModel = new AssignModel();
                        assignModel.setTv_uploadId(finalObject.getString("upload_ID"));
                        assignModel.setTv_fileName(finalObject.getString("filename"));
                        assignModel.setTv_Desc(finalObject.getString("discription"));
                        assignModel.setTv_UploadDate(finalObject.getString("date"));
                        assignModel.setTv_Faculty(finalObject.getString("facultyname"));
                        assignList.add(assignModel);
                    }

                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                    return assignList;

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }else{
                List<AssignModel> assignList = new ArrayList<>();
                return assignList;
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(List<AssignModel> result) {
            super.onPostExecute(result);
            Assignment_activity.AssignAdapter assignAdapter = new Assignment_activity.AssignAdapter(getApplicationContext(),R.layout.assignt_viewlayout,result);
            lv_assign.setAdapter(assignAdapter);

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
    public class AssignAdapter extends ArrayAdapter {
        private  List<AssignModel> assignModelList;
        private  int resource;
        private LayoutInflater inflater;
        public AssignAdapter(@NonNull Context context, int resource, @NonNull List<AssignModel> objects) {
            super(context, resource, objects);
            assignModelList = objects;
            this.resource = resource;
            inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if(convertView == null){
                convertView = inflater.inflate(resource,null);
            }

            TextView tv_uploadId = (TextView)convertView.findViewById(R.id.tv_uploadId);
            TextView tv_fileName = (TextView)convertView.findViewById(R.id.tv_fileName);
            TextView tv_desc = (TextView)convertView.findViewById(R.id.tv_Desc);
            TextView tv_facultyName = (TextView)convertView.findViewById(R.id.tv_Faculty);
            TextView tv_update = (TextView)convertView.findViewById(R.id.tv_UploadDate);

            tv_uploadId.setText(assignModelList.get(position).getTv_uploadId());
            tv_fileName.setText(assignModelList.get(position).getTv_fileName());
            tv_desc.setText(assignModelList.get(position).getTv_Desc());
            tv_facultyName.setText(assignModelList.get(position).getTv_Faculty());
            tv_update.setText(assignModelList.get(position).getTv_UploadDate());
            return convertView;

        }
    }
}
