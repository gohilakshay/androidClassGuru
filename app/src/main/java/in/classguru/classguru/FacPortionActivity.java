package in.classguru.classguru;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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

public class FacPortionActivity extends Faculty_Home_Activity implements portionDialog.PortionAddListener {

    private Button btn_addPortion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fac_portion);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navSideBar);
        View nav = navigationView.getHeaderView(0);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.FaPor);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        btn_addPortion = (Button)findViewById(R.id.btn_addPortion);
        btn_addPortion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });

    }

    public void openDialog(){

        portionDialog portionD = new portionDialog();
        Bundle args = new Bundle();
        args.putString("globaldbname", globaldbname);
        args.putString("globalid", globalid);
        args.putString("globalpermissin", globalpermissin);
        portionD.setArguments(args);
        portionD.show(getSupportFragmentManager(),"Add Portion");
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void applyTexts(String portiontotal, String portionremain,String portionBatchSelect,String portionSubjSelect) {

        String[] splitBatch = portionBatchSelect.split(" ");
        AddPortion_post addPortion_post = new AddPortion_post(this);

        addPortion_post.execute("FacPortionAdd",portiontotal,portionremain,splitBatch[0],portionSubjSelect);

    }


    public class AddPortion_post extends AsyncTask<String,Void,String> {

        Context context;
        android.app.AlertDialog alertDialog;
        AddPortion_post (Context ctx){
            context = ctx;
        }

        @Override
        protected String doInBackground(String... params) {

            String type = params[0];
            String totalTopics = params[1];
            String remainTopics = params[2];
            String batch = params[3];
            String subject = params[4];
            String login_url = "https://classes.classguru.in/api/addFacPortion.php";
            if(type.equals("FacPortionAdd")){
                try {
                    URL profile_url = new URL(login_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) profile_url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String post_data = URLEncoder.encode("batch","UTF-8")+"="+URLEncoder.encode(batch,"UTF-8")+"&"
                            +URLEncoder.encode("dbname","UTF-8")+"="+URLEncoder.encode(globaldbname,"UTF-8")+"&"
                            +URLEncoder.encode("subject","UTF-8")+"="+URLEncoder.encode(subject,"UTF-8")+"&"
                            +URLEncoder.encode("remainTopics","UTF-8")+"="+URLEncoder.encode(remainTopics,"UTF-8")+"&"
                            +URLEncoder.encode("totalTopics","UTF-8")+"="+URLEncoder.encode(totalTopics,"UTF-8")+"&"
                            +URLEncoder.encode("userId","UTF-8")+"="+URLEncoder.encode(globalid,"UTF-8");
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

            if(result.equals(" added info  ")){
                alertDialog.setMessage("Portion Added successfully");
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
