package in.classguru.classguru;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
        android.support.v7.app.ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#148388")));

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
            String login_url = "http://206.189.231.53/admin/api/student_details.php";
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

            //TextView tv_uploadId = (TextView)convertView.findViewById(R.id.tv_uploadId);
            TextView tv_fileName = (TextView)convertView.findViewById(R.id.tv_fileName);
            final TextView tv_desc = (TextView)convertView.findViewById(R.id.tv_Desc);
            TextView tv_facultyName = (TextView)convertView.findViewById(R.id.tv_Faculty);
            TextView tv_update = (TextView)convertView.findViewById(R.id.tv_UploadDate);
            final TextView plus = (TextView)convertView.findViewById(R.id.tv_readMore1);
            final TextView minus = (TextView)convertView.findViewById(R.id.tv_readLess1);

            /*Button btn_download = (Button)convertView.findViewById(R.id.btn_download);
            btn_download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    new DownloadFromUrl(getApplication()).execute("https://www.antennahouse.com/XSLsample/pdf/sample-link_1.pdf");
                }
            });*/

            //tv_uploadId.setText(assignModelList.get(position).getTv_uploadId());
            tv_fileName.setText(assignModelList.get(position).getTv_fileName());
            tv_desc.setText(assignModelList.get(position).getTv_Desc());
            tv_facultyName.setText(assignModelList.get(position).getTv_Faculty());

            String[] date = assignModelList.get(position).getTv_UploadDate().split("-");
            tv_update.setText(date[2]+"-"+date[1]+"-"+date[0]);

            minus.setVisibility(View.GONE);
            plus.setVisibility(View.GONE);
            tv_desc.post(new Runnable() {
                @Override
                public void run() {
                    int lineCount = tv_desc.getLineCount();
                    if(lineCount > 2){
                        minus.setVisibility(View.GONE);
                        plus.setVisibility(View.VISIBLE);
                        tv_desc.setMaxLines(2);

                    }
                }
            });
            plus.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    plus.setVisibility(View.GONE);
                    minus.setVisibility(View.VISIBLE);
                    tv_desc.setMaxLines(Integer.MAX_VALUE);

                }
            });

            minus.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    minus.setVisibility(View.GONE);
                    plus.setVisibility(View.VISIBLE);
                    tv_desc.setMaxLines(2);

                }
            });
            return convertView;

        }
    }

    /*public class DownloadFromUrl extends AsyncTask<String,Void,String>{

        Context context;
        android.app.AlertDialog alertDialog;

        DownloadFromUrl (Context ctx){
            context = ctx;
        }

        @Override
        protected String doInBackground(String... params) {
            String url1 = params[0];
            try {

                URL url = new URL(url1);

                HttpURLConnection c = (HttpURLConnection) url.openConnection();
                c.setRequestMethod("GET");
                c.setDoOutput(true);
                c.connect();

                String Path = Environment.getExternalStorageDirectory() + "/download/";
                Log.v("PortfolioManger", "PATH: " + Path);
                File file = new File(Path);
                file.mkdirs();
                FileOutputStream fos = new FileOutputStream("IMG_4717.JPG");

                InputStream is = c.getInputStream();

                byte[] buffer = new byte[702];
                int len1 = 0;
                while ((len1 = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len1);
                }
                fos.close();
                is.close();
            } catch (IOException e) {
                Log.d("PortfolioManger", "Error: " + e);
            }
            Log.v("PortfolioManger", "Check: ");

            return null;
        }

        @Override
        protected void onPreExecute() {
            alertDialog = new android.app.AlertDialog.Builder(context).create();

            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {


        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }*/
}
