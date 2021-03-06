package in.classguru.classguru;

import android.app.ActionBar;
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
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

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

import in.classguru.classguru.models.FacSalaryModel;

public class FacSalaryActivity extends Faculty_Home_Activity {

    public DrawerLayout mDrawerLayout;
    public ActionBarDrawerToggle mToggle;
    public ListView lv_facSalary;
    public TextView tv_sName;
    public TextView tv_sNumb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fac_salary);

        lv_facSalary = (ListView)findViewById(R.id.lv_facSal);

        mDrawerLayout = (DrawerLayout)findViewById(R.id.FaSal);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        android.support.v7.app.ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#148388")));


        Fac_Salary_fetch fac_salary_fetch = new Fac_Salary_fetch(this);
        fac_salary_fetch.execute("faculty",globalid,globalpermissin,globaldbname);


        NavigationView navigationView = (NavigationView) findViewById(R.id.navSideBar);
        View nav = navigationView.getHeaderView(0);

        tv_sName = (TextView)nav.findViewById(R.id.tvSideName);
        tv_sNumb = (TextView)nav.findViewById(R.id.tvSideNumb);

        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .build();
        ImageLoader.getInstance().init(config); // Do it on Application start

        ImageView im_sFacProfile = (ImageView)findViewById(R.id.iv_sProfile);





        if (im_sFacProfile != null ){
            // Then later, when you want to display image
            ImageLoader.getInstance().displayImage("https://classes.classguru.in/"+globalurl, im_sFacProfile);
        }

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
    public class Fac_Salary_fetch extends AsyncTask<String,Void,String> {

        Context context;
        android.app.AlertDialog alertDialog;

        Fac_Salary_fetch (Context ctx){
            context = ctx;
        }

        @Override
        protected String doInBackground(String... params) {
            String type = params[0];
            String id = params[1];
            String permission = params[2];
            String dbname = params[3];
            String login_url = "http://206.189.231.53/admin/api/teacher_details.php";
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
            try {
                JSONObject reader = new JSONObject(result);
                if(!reader.getString("t_expense").equals("teacher id not found")){
                    JSONArray fulltest = reader.getJSONArray("t_expense");

                    List<FacSalaryModel> facSalaryModelList = new ArrayList<>();

                    for (int i=0;i<fulltest.length();i++){
                        JSONObject finalresult = fulltest.getJSONObject(i);
                        FacSalaryModel facSalaryModel = new FacSalaryModel();

                        facSalaryModel.setAmt(finalresult.getString("salary"));
                        if(!finalresult.getString("bank_name").equals(""))
                            facSalaryModel.setBankNo(finalresult.getString("bank_name"));
                        else
                            facSalaryModel.setBankNo("-");
                        if(!finalresult.getString("chq_date").equals(""))
                            facSalaryModel.setChqDate(finalresult.getString("chq_date"));
                        else
                            facSalaryModel.setChqDate("-");
                        if(!finalresult.getString("chq_no").equals(""))
                            facSalaryModel.setChqNo(finalresult.getString("chq_no"));
                        else
                            facSalaryModel.setChqNo("-");
                        facSalaryModel.setPayDate(finalresult.getString("payment_date"));
                        facSalaryModel.setPayMode(finalresult.getString("payment_mode"));
                        if(!finalresult.getString("transc_id").equals(""))
                            facSalaryModel.setTrancID(finalresult.getString("transc_id"));
                        else
                            facSalaryModel.setTrancID("-");
                        facSalaryModelList.add(facSalaryModel);
                    }
                    FacSalaryActivity.SalaryAdapter salaryAdapter = new FacSalaryActivity.SalaryAdapter(getApplicationContext(),R.layout.facsal_layout,facSalaryModelList);
                    lv_facSalary.setAdapter(salaryAdapter);
                }


                tv_sName.setText(globalname);
                tv_sNumb.setText(globalnumb);


                //String salary = finalresult.getString("salary");
                //alertDialog.setMessage(salary);
              //  alertDialog.show();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


    }
    public class SalaryAdapter extends ArrayAdapter {
        private List<FacSalaryModel> facSalaryModelList;
        private  int resource;
        private LayoutInflater inflater;
        public SalaryAdapter (@NonNull Context context, int resource, @NonNull List<FacSalaryModel> objects){
            super(context,resource,objects);
            facSalaryModelList = objects;
            this.resource = resource;
            inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        }
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if(convertView == null){
                convertView = inflater.inflate(resource,null);
            }

            TextView tv_amt = (TextView)convertView.findViewById(R.id.tv_FacAmt);
            TextView tv_paymode = (TextView)convertView.findViewById(R.id.tv_FacPayMode);
            TextView tv_paydate = (TextView)convertView.findViewById(R.id.tv_FacpayDate);
            TextView tv_chqno = (TextView)convertView.findViewById(R.id.tv_FacChqNo);
            TextView tv_chqdate = (TextView)convertView.findViewById(R.id.tv_FacChqDate);
            TextView tv_bankname = (TextView)convertView.findViewById(R.id.tv_FacBankName);
            TextView tv_trancid = (TextView)convertView.findViewById(R.id.tv_FacTrancID);

            tv_amt.setText(facSalaryModelList.get(position).getAmt());
            tv_paymode.setText(facSalaryModelList.get(position).getPayMode());

            String[] date = facSalaryModelList.get(position).getPayDate().split("-");
            tv_paydate.setText(date[2]+"-"+date[1]+"-"+date[0]);

            tv_chqno.setText(facSalaryModelList.get(position).getChqNo());

            tv_chqdate.setText(facSalaryModelList.get(position).getChqDate());
            tv_bankname.setText(facSalaryModelList.get(position).getBankNo());
            tv_trancid.setText(facSalaryModelList.get(position).getTrancID());

            return convertView;

        }
    }


}
