package in.classguru.classguru;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
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
import java.util.ArrayList;
import java.util.List;

public class FacPortionUpdateActivity extends Faculty_Home_Activity {
    String globalid;
    String globalpermissin;
    String globaldbname;
    /*String batch_id;*/
    String subject;
    String remainTopics;
    String globalname;
    String globalnumb;
    String globalurl;
    String portion_id;
    int total_topics;
    String total_topicsString;
    public DrawerLayout mDrawerLayout;
    public ActionBarDrawerToggle mToggle;
    public TextView tv_sName;
    public TextView tv_sNumb;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fac_portion_update);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navSideBar);
        View nav = navigationView.getHeaderView(0);

        tv_sName = (TextView)nav.findViewById(R.id.tvSideName);
        tv_sNumb = (TextView)nav.findViewById(R.id.tvSideNumb);

        tv_sName.setText(globalname);
        tv_sNumb.setText(globalnumb);

        mDrawerLayout = (DrawerLayout)findViewById(R.id.FacPortiondrawer);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        android.support.v7.app.ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#148388")));

        Intent data = getIntent();
        String id = data.getStringExtra("id");
        globalid = id;
        String permission = data.getStringExtra("permission");
        globalpermissin = permission;
        String dbname = data.getStringExtra("dbname");
        globaldbname = dbname;
        String port_id = data.getStringExtra("portion_id");
        portion_id = port_id;
        String total_top = data.getStringExtra("total_topics");
        total_topics = Integer.parseInt(total_top);
        total_topicsString =total_top;
        String remainTop = data.getStringExtra("remainTopics");
        remainTopics = remainTop;
        String subj = data.getStringExtra("subject");
        subject = subj;


        TableLayout Tl_portionView = (TableLayout)findViewById(R.id.Ll_portionUpdateView);
        for(int i=1;i < total_topics*2 ; i++){
            TableRow Tr = new TableRow(this);
            EditText t = new EditText(this);
            t.setId(i);
            t.setHint("Enter Topic ");
            int j = i+1;
            i++;
            CheckBox c = new CheckBox(this);
            c.setId(j);
            c.getTag(i);
            Tr.addView(t);
            Tr.addView(c);
            Tl_portionView.addView(Tr);
        }
        LinearLayout Ll_portionUpdateView = (LinearLayout)findViewById(R.id.Ll_portionUpdateView);
        Button btn = new Button(this);
        btn.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        btn.setText("Update Portion");
        Ll_portionUpdateView.addView(btn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alert = new AlertDialog.Builder(FacPortionUpdateActivity.this).create();
                /*int i =2;
                alert.setTitle("Test");
                alert.setMessage(((EditText)findViewById(i)).getText().toString());
                alert.show();*/

                List<String> portionTitleList = new ArrayList<>();
                List<String> portionCompleteList = new ArrayList<>();
                for(int i=1;i < total_topics*2 ; i++){
                    String topics = ((EditText)findViewById(i)).getText().toString();
                    portionTitleList.add(topics);
                    int j= i+1;
                    i++;
                    if(((CheckBox)findViewById(j)).isChecked()){
                        portionCompleteList.add(topics);
                    }
                }

                String allTopic = android.text.TextUtils.join(",",portionTitleList);
                String selectedTopic = android.text.TextUtils.join(",",portionCompleteList);

                UpdatePortion_post updatePortion_post = new UpdatePortion_post(FacPortionUpdateActivity.this);
                updatePortion_post.execute("FacPortionUpdate",portion_id,allTopic,selectedTopic,total_topicsString,remainTopics,subject);
                /*alert.setTitle("Test");
                alert.setMessage( android.text.TextUtils.join(",",portionCompleteList));
                alert.show();*/
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


    public class UpdatePortion_post extends AsyncTask<String,Void,String> {

        Context context;
        android.app.AlertDialog alertDialog;

        UpdatePortion_post(Context ctx) {
            context = ctx;
        }

        @Override
        protected String doInBackground(String... params) {

            String type = params[0];
            String totalTopics = params[4];
            String remainTopics = params[5];

            String subject = params[6];
            String portion_id = params[1];
            String alltopic = params[2];
            String selectedtopic = params[3];
            String login_url = "http://206.189.231.53/admin/api/addFacPortion.php";
            if (type.equals("FacPortionUpdate")) {
                try {
                    URL profile_url = new URL(login_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) profile_url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String post_data = URLEncoder.encode("dbname", "UTF-8") + "=" + URLEncoder.encode(globaldbname, "UTF-8") + "&"
                            + URLEncoder.encode("subject", "UTF-8") + "=" + URLEncoder.encode(subject, "UTF-8") + "&"
                            + URLEncoder.encode("remainTopics", "UTF-8") + "=" + URLEncoder.encode(remainTopics, "UTF-8") + "&"
                            + URLEncoder.encode("totalTopics", "UTF-8") + "=" + URLEncoder.encode(totalTopics, "UTF-8") + "&"
                            + URLEncoder.encode("userId", "UTF-8") + "=" + URLEncoder.encode(globalid, "UTF-8")+ "&"
                            + URLEncoder.encode("portion_ID", "UTF-8") + "=" + URLEncoder.encode(portion_id, "UTF-8")+ "&"
                            + URLEncoder.encode("syllabus", "UTF-8") + "=" + URLEncoder.encode(alltopic, "UTF-8")+ "&"
                            + URLEncoder.encode("completeTopics", "UTF-8") + "=" + URLEncoder.encode(selectedtopic, "UTF-8");
                    bufferedWriter.write(post_data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                    String result = "";
                    String line = "";
                    while ((line = bufferedReader.readLine()) != null) {
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
            } else {
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

            if (result.equals(" updated info  ")) {
                alertDialog.setMessage("Portion Updated successfully");
                alertDialog.show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        startActivity(getIntent());

                    }
                }, 500);
            } else {
                alertDialog.setMessage("Error Occured" + result);
                alertDialog.show();
            }

            super.onPostExecute(result);
        }
    }
}
