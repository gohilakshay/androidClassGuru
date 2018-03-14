package in.classguru.classguru;

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

import in.classguru.classguru.models.AttendanceModel;
import in.classguru.classguru.models.TestModel;

public class Test_activity extends Home_activity {


    public ListView lv_test;
    public DrawerLayout mDrawerLayout;
    public ActionBarDrawerToggle mToggle;
    public TextView tvSidenumb;
    public TextView tvSidename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_activity);
        Test_fetch test_fetch = new Test_fetch(this);
        test_fetch.execute("student",globalid,globalpermissin,globaldbname);

        lv_test = (ListView)findViewById(R.id.lv_test);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.testDrawer);

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
    public class Test_fetch extends AsyncTask<String,Void,List<TestModel>>{

        Context context;
        android.app.AlertDialog alertDialog;
        Test_fetch (Context ctx){
            context = ctx;
        }

        @Override
        protected List<TestModel> doInBackground(String... params) {
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

                    String[] separatedStud = new String[0];
                    String[] separatedMarks = new String[0];
                    JSONObject reader = new JSONObject(result);
                    //String checkResult = reader.getString("std_attendance");
                    /*reader.getJSONArray("std_attendance");*/
                    List<TestModel> testList = new ArrayList<>();
                    JSONArray fulltest = reader.getJSONArray("std_test");

                    for (int i=0;i<fulltest.length();i++){
                        JSONObject finalObject = fulltest.getJSONObject(i);
                        TestModel testModel = new TestModel();
                        testModel.setTv_testDate(finalObject.getString("test_date"));
                        testModel.setTv_testBatch(finalObject.getString("batch_id"));

                        String studIds = finalObject.getString("stud_id");
                        String marks = finalObject.getString("marks_obtained");

                        separatedStud = studIds.split(",");
                        separatedMarks = marks.split(",");
                        for(int j=0;j<separatedStud.length;j++){
                            if(separatedStud[j].equals(globalid)){
                                testModel.setTv_Obtain(separatedMarks[j]);
                            }

                        }
                        /*testModel.setTv_Obtain(globalid);*/

                        testModel.setTv_Total(finalObject.getString("total_marks"));
                        testModel.setTv_Passing(finalObject.getString("passing_marks"));
                        testModel.setTv_Subject(finalObject.getString("subject_name"));
                        testList.add(testModel);
                    }

                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                    return testList;

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }else{
                List<TestModel> testList = new ArrayList<>();
                return testList;
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(List<TestModel> result) {
            super.onPostExecute(result);
            TestAdapter testAdapter = new TestAdapter(getApplicationContext(),R.layout.test_viewlayout,result);
            lv_test.setAdapter(testAdapter);

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
    public class TestAdapter extends ArrayAdapter {
        private  List<TestModel> testModelList;
        private  int resource;
        private LayoutInflater inflater;
        public TestAdapter(@NonNull Context context, int resource, @NonNull List<TestModel> objects) {
            super(context, resource, objects);
            testModelList = objects;
            this.resource = resource;
            inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if(convertView == null){
                convertView = inflater.inflate(resource,null);
            }
            TextView tv_testDate;
            TextView tv_testBatch;
            TextView tv_Obtain;
            TextView tv_Total;
            TextView tv_Passing;
            TextView tv_Subject;

            tv_testDate = (TextView)convertView.findViewById(R.id.tv_testDate);
            tv_testBatch = (TextView)convertView.findViewById(R.id.tv_testBatch);
            tv_Obtain = (TextView)convertView.findViewById(R.id.tv_Obtain);
            tv_Total = (TextView)convertView.findViewById(R.id.tv_Total);
            tv_Passing = (TextView)convertView.findViewById(R.id.tv_Passing);
            tv_Subject = (TextView)convertView.findViewById(R.id.tv_Subject);
            tv_testDate.setText(testModelList.get(position).getTv_testDate());
            tv_testBatch.setText(testModelList.get(position).getTv_testBatch());
            tv_Obtain.setText(testModelList.get(position).getTv_Obtain());
            tv_Total.setText(testModelList.get(position).getTv_Total());
            tv_Passing.setText(testModelList.get(position).getTv_Passing());
            tv_Subject.setText(testModelList.get(position).getTv_Subject());
            return convertView;

        }
    }
}
