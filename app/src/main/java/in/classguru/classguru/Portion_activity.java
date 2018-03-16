package in.classguru.classguru;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
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
import in.classguru.classguru.models.AttendanceModel;
import in.classguru.classguru.models.PortionModel;

public class Portion_activity extends Home_activity {

    public DrawerLayout mDrawerLayout;
    public ActionBarDrawerToggle mToggle;
    public TextView tvSidenumb;
    public TextView tvSidename;
    public ListView lv_portion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portion_activity);

        lv_portion = (ListView)findViewById(R.id.lv_portion);
        /*Attendance_activity.Attendance_fetch attendance_fetch = new Attendance_activity.Attendance_fetch(this);
        attendance_fetch.execute("student",globalid,globalpermissin,globaldbname);*/

        Portion_activity.Portion_fetch portion_fetch = new Portion_activity.Portion_fetch(this);
        portion_fetch.execute("student",globalid,globalpermissin,globaldbname);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navSideBar);
        View nav = navigationView.getHeaderView(0);

        tvSidename = (TextView)nav.findViewById(R.id.tvSideName);
        tvSidenumb = (TextView)nav.findViewById(R.id.tvSideNumb);


        mDrawerLayout = (DrawerLayout)findViewById(R.id.portionDrawer);
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

    public class Portion_fetch extends AsyncTask<String,Void,List<PortionModel>>{

        Context context;
        android.app.AlertDialog alertDialog;
        Portion_fetch (Context ctx){
            context = ctx;
        }

        @Override
        protected List<PortionModel> doInBackground(String... params) {
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
                    List<PortionModel> portionList = new ArrayList<>();
                    JSONObject reader = new JSONObject(result);
                    if(!reader.getString("portion_details").equals("student batch id not found")){
                        JSONArray fullportion = reader.getJSONArray("portion_details");
                        for (int i=0;i<fullportion.length();i++) {
                            JSONObject finalObject = fullportion.getJSONObject(i);
                            PortionModel portionModel = new PortionModel();
                            portionModel.setSubject(finalObject.getString("subject_name"));
                            portionModel.setTopic(finalObject.getString("syllabus"));
                            portionModel.setCompleted(finalObject.getString("complete_syllabus"));
                            portionModel.setPortionId(finalObject.getString("portion_ID"));
                            portionModel.setTotalTopics(finalObject.getString("no_of_topics"));
                            portionModel.setRemainTopics(finalObject.getString("remained_topics"));
                            portionList.add(portionModel);
                        }
                    }

                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                    return portionList;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{
                List<PortionModel> portionList = new ArrayList<>();
                return portionList;
            }
            return null;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(List<PortionModel> result) {
            super.onPostExecute(result);
           // Assignment_activity.AssignAdapter assignAdapter = new Assignment_activity.AssignAdapter(getApplicationContext(),R.layout.assignt_viewlayout,result);
            Portion_activity.PortionAdapter portionAdapter = new Portion_activity.PortionAdapter(getApplicationContext(),R.layout.portion_listview_layout,result);

            lv_portion.setAdapter(portionAdapter);

            tvSidename.setText(globalname);
            tvSidenumb.setText(globalnumb);
            ImageView ivsprofile = (ImageView)findViewById(R.id.iv_sProfile);

            // Then later, when you want to display image
            ImageLoader.getInstance().displayImage("https://classes.classguru.in/"+globalurl, ivsprofile);

        }


    }
    public class PortionAdapter extends ArrayAdapter {
        List<PortionModel> portionModelList;
        private  int resource;
        private LayoutInflater inflater;
        public PortionAdapter(@NonNull Context context, int resource, @NonNull List<PortionModel> objects) {
            super(context, resource, objects);
            portionModelList = objects;
            this.resource = resource;
            inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        }
        @SuppressLint("ResourceType")
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(resource, null);
            }
            TextView tv_subject = (TextView)convertView.findViewById(R.id.tv_Portionsubject);
            TextView tv_topic = (TextView)convertView.findViewById(R.id.tv_topic);
            TextView tv_completed = (TextView)convertView.findViewById(R.id.tv_complete);
            Button btn_portionViewId = (Button)convertView.findViewById(R.id.btn_portionViewId);

            tv_subject.setText(portionModelList.get(position).getSubject());
            tv_topic.setText(portionModelList.get(position).getTotalTopics());
            tv_completed.setText(portionModelList.get(position).getRemainTopics());

            /*LinearLayout ll_portionLayout = (LinearLayout)convertView.findViewById(R.id.ll_portionLayout);
            Button button = new Button(getContext());
            button.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            button.setText("View");
            button.setId(1);
            button.setTextColor(Color.WHITE);
            button.setBackgroundColor(Color.parseColor("#09B0B9"));
            ll_portionLayout.addView(button);*/


            btn_portionViewId.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /*AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage(s);
                    builder.show();*/
                    finish();
                    Intent intent = new Intent(getContext(),Stud_portionList_Activity.class);
                    intent.putExtra("portionList",portionModelList.get(position).getTopic());
                    intent.putExtra("portionComplete",portionModelList.get(position).getCompleted());
                    intent.putExtra("portionId",portionModelList.get(position).getPortionId());
                    intent.putExtra("globaldbname",globaldbname);
                    intent.putExtra("globalid",globalid);
                    startActivity(intent);

                }
            });

            return convertView;
        }

    }


}

