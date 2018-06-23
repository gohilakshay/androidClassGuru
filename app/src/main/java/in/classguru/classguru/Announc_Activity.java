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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
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

import in.classguru.classguru.models.FacNoticeModel;

public class Announc_Activity extends Home_activity {

    public DrawerLayout mDrawerLayout;
    public ActionBarDrawerToggle mToggle;
    public TextView tvSidenumb;
    public TextView tvSidename;
    private ListView announceViewList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announc_);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.announcDrawer);

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

        Announc_fetch fac_announc_fetch = new Announc_fetch(this);
        fac_announc_fetch.execute("facultyAnnounce",globalid,globaldbname);
        announceViewList = (ListView)findViewById(R.id.announceViewList);
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

    public class Announc_fetch extends AsyncTask<String,Void,String> {
        Context context;
        android.app.AlertDialog alertDialog;
        Announc_fetch (Context ctx){
            context = ctx;
        }

        @Override
        protected String doInBackground(String... params) {
            String type = params[0];
            String id = params[1];
            String dbname = params[2];
            String login_url = "http://206.189.231.53/admin/api/student_details.php";
            if(type.equals("facultyAnnounce")){
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
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
        @Override
        protected void onPostExecute(String result) {
            /*alertDialog.setMessage(result);
            alertDialog.show();
            Log.i("Tag", "onPostExecute: "+ result);*/
            super.onPostExecute(result);

            try {
                JSONObject reader = new JSONObject(result);
                if(!reader.getString("announc_details").equals("student batch id not found")){
                    JSONArray fulltest = reader.getJSONArray("announc_details");

                    List<FacNoticeModel> facNoticeModelList = new ArrayList<>();
                    for (int i=0;i<fulltest.length();i++){
                        JSONObject finalresult = fulltest.getJSONObject(i);
                        FacNoticeModel facNoticeModel = new FacNoticeModel();

                        facNoticeModel.setBatch(finalresult.getString("batch"));
                        facNoticeModel.setDate(finalresult.getString("date"));
                        facNoticeModel.setDescription(finalresult.getString("Description"));
                        facNoticeModel.setTitle(finalresult.getString("title"));
                        facNoticeModelList.add(facNoticeModel);
                    }
                    AnnounceAdapter announceAdapter = new AnnounceAdapter(getApplicationContext(),R.layout.facannounc_view_layout,facNoticeModelList);
                    announceViewList.setAdapter(announceAdapter);

                }
                tvSidename.setText(globalname);
                tvSidenumb.setText(globalnumb);
                ImageView ivsprofile = (ImageView)findViewById(R.id.iv_sProfile);

                // Then later, when you want to display image
                ImageLoader.getInstance().displayImage("https://classes.classguru.in/"+globalurl, ivsprofile);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
    public class AnnounceAdapter extends ArrayAdapter {

        List<FacNoticeModel> facNoticeModelList;
        private  int resource;
        private LayoutInflater inflater;

        public AnnounceAdapter(@NonNull Context context, int resource, @NonNull List<FacNoticeModel> objects) {
            super(context, resource, objects);
            facNoticeModelList = objects;
            this.resource = resource;
            inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        }
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if(convertView == null){
                convertView = inflater.inflate(resource,null);
            }
            TextView tv_Batch = (TextView)convertView.findViewById(R.id.tv_facNotBatch);
            TextView tv_Date = (TextView)convertView.findViewById(R.id.tv_facNotDate1);
            final TextView tv_Desc = (TextView)convertView.findViewById(R.id.tv_facNotDesc);
            TextView tv_Title = (TextView)convertView.findViewById(R.id.tv_facNotTitle1);
            final TextView plus = (TextView)convertView.findViewById(R.id.tv_readMore);
            final TextView minus = (TextView)convertView.findViewById(R.id.tv_readLess);
            RelativeLayout relativeLayout = (RelativeLayout)convertView.findViewById(R.id.Rl_anncView);

            tv_Batch.setText(facNoticeModelList.get(position).getBatch());
            Log.i("Tag12",facNoticeModelList.get(position).getDate());
            tv_Date.setText(facNoticeModelList.get(position).getDate());
            tv_Desc.setText(facNoticeModelList.get(position).getDescription());
            tv_Title.setText(facNoticeModelList.get(position).getTitle());

            minus.setVisibility(View.GONE);
            plus.setVisibility(View.GONE);
            tv_Desc.post(new Runnable() {
                @Override
                public void run() {
                    int lineCount = tv_Desc.getLineCount();
                    if(lineCount > 2){
                        minus.setVisibility(View.GONE);
                        plus.setVisibility(View.VISIBLE);
                        tv_Desc.setMaxLines(2);

                    }
                }
            });
            plus.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    plus.setVisibility(View.GONE);
                    minus.setVisibility(View.VISIBLE);
                    tv_Desc.setMaxLines(Integer.MAX_VALUE);

                }
            });

            minus.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    minus.setVisibility(View.GONE);
                    plus.setVisibility(View.VISIBLE);
                    tv_Desc.setMaxLines(2);

                }
            });

            return convertView;

        }
    }
}
