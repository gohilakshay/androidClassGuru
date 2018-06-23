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
import android.support.v7.app.AlertDialog;
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

import in.classguru.classguru.models.AssignModel;
import in.classguru.classguru.models.FeeModal;

public class Fee_activity extends Home_activity {
    AlertDialog alertDialog;
    public TextView tv_option;
    public TextView tv_type;
    public TextView tv_total;
    public TextView tv_dicount;
    public TextView tv_final;
    public TextView tv_received;
    public TextView tv_balance;
    public TextView tv_ampintall;
    public DrawerLayout mDrawerLayout;
    public ActionBarDrawerToggle mToggle;
    public TextView tvSidenumb;
    public TextView tvSidename;
    public ListView lv_feeShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fee_activity);
        tv_option = (TextView)findViewById(R.id.tv_Option);
        tv_type = (TextView)findViewById(R.id.tv_Type);
        tv_total = (TextView)findViewById(R.id.tv_Total);
        tv_dicount = (TextView)findViewById(R.id.tv_Discount);
        tv_final = (TextView)findViewById(R.id.tv_Final);
        tv_received = (TextView)findViewById(R.id.tv_Received);
        tv_balance = (TextView)findViewById(R.id.tv_Balance);
        tv_ampintall = (TextView)findViewById(R.id.tv_AmpInstall);

        lv_feeShow = (ListView)findViewById(R.id.lv_feeShow);

        Fee_Activity_work fee_activity = new Fee_Activity_work(this);
        fee_activity.execute("student",globalid,globalpermissin,globaldbname);

        mDrawerLayout = (DrawerLayout)findViewById(R.id.feeDrawer);

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

    public class Fee_Activity_work extends AsyncTask<String,Void,String> {
        Context context;
        android.app.AlertDialog alertDialog;
        public ImageView ivsprofile;

        Fee_Activity_work (Context ctx){
            context = ctx;
        }

        @Override
        protected String doInBackground(String... params) {
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
                if(!result.equals("faculty")) {
                    JSONObject reader = new JSONObject(result);

                    JSONArray fulltest = reader.getJSONArray("student_fees");
                    JSONObject finalresult = fulltest.getJSONObject(0);


                    String install_option = finalresult.getString("installment_option");
                        tv_option.setText(install_option);
                    String type = finalresult.getString("installment_type");
                        tv_type.setText(type);
                    String total = finalresult.getString("total_fee");
                        tv_total.setText(total);
                    String discount = finalresult.getString("discount");
                        tv_dicount.setText(discount);
                    String finalamt = finalresult.getString("final_fee");
                        tv_final.setText(finalamt);
                    String received = finalresult.getString("recieved_fee");
                        tv_received.setText(received);
                    String balance = finalresult.getString("balance_fee");
                        tv_balance.setText(balance);
                    String amtpinstall = finalresult.getString("amountper_installment");
                        tv_ampintall.setText(amtpinstall);

                    tvSidename.setText(globalname);
                    tvSidenumb.setText(globalnumb);
                    ivsprofile = (ImageView)findViewById(R.id.iv_sProfile);
                    List<FeeModal> feeModalList = new ArrayList<>();


                    for (int i=0;i<fulltest.length();i++){
                        JSONObject finalresult1 = fulltest.getJSONObject(i);
                        FeeModal feeModal = new FeeModal();
                        feeModal.setPay_mode(finalresult1.getString("payment_mode"));
                        feeModal.setPay_date(finalresult1.getString("paydate"));
                        feeModal.setChq_date(finalresult1.getString("chq_date"));
                        feeModal.setBank_name(finalresult1.getString("bank_name"));
                        feeModal.setChq_no(finalresult1.getString("chq_no"));
                        feeModal.setTransc_id(finalresult1.getString("transc_id"));
                        feeModal.setReceived(finalresult1.getString("recieved_fee"));
                        if(finalresult1.has(("paid_fee"))) {
                            feeModal.setPaid_rec(finalresult1.getString("paid_fee"));
                        }else{
                            feeModal.setPaid_rec("-");
                        }
                        feeModal.setBalance(finalresult1.getString("balance_fee"));
                        feeModalList.add(feeModal);
                    }
                    Fee_activity.FeeAdapter feeAdapter = new Fee_activity.FeeAdapter(getApplicationContext(),R.layout.fee_detail_layout,feeModalList);
                    lv_feeShow.setAdapter(feeAdapter);
                        // Then later, when you want to display image
                        ImageLoader.getInstance().displayImage("https://classes.classguru.in/"+globalurl, ivsprofile);

                }
                else{
                    alertDialog.setMessage("faculty Work in Progress");
                    alertDialog.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    public class FeeAdapter extends ArrayAdapter {
        private List<FeeModal> feeModelList;
        private  int resource;
        private LayoutInflater inflater;
        public FeeAdapter(@NonNull Context context, int resource, @NonNull List<FeeModal> objects) {
            super(context, resource, objects);
            feeModelList = objects;
            this.resource = resource;
            inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if(convertView == null){
                convertView = inflater.inflate(resource,null);
            }


            TextView tv_paymode = (TextView)convertView.findViewById(R.id.tv_payMode);
            TextView tv_paydate = (TextView)convertView.findViewById(R.id.tv_payDate);
            TextView tv_chqdate = (TextView)convertView.findViewById(R.id.tv_chqPayDate);
            TextView tv_bankname = (TextView)convertView.findViewById(R.id.tv_bankName);
            TextView tv_chq_no = (TextView)convertView.findViewById(R.id.tv_chqNo);
            TextView tv_trancId = (TextView)convertView.findViewById(R.id.tv_trancId);
            TextView tv_received = (TextView)convertView.findViewById(R.id.tv_recievedPay);
            TextView tv_recent = (TextView)convertView.findViewById(R.id.tv_recentPay);
            TextView tv_balance = (TextView)convertView.findViewById(R.id.tv_balance_fee);

            tv_paymode.setText(feeModelList.get(position).getPay_mode());
            tv_paydate.setText(feeModelList.get(position).getPay_date());
            tv_chqdate.setText(feeModelList.get(position).getChq_date());
            tv_bankname.setText(feeModelList.get(position).getBank_name());
            tv_chq_no.setText(feeModelList.get(position).getChq_no());
            tv_trancId.setText(feeModelList.get(position).getTransc_id());
            tv_received.setText(feeModelList.get(position).getReceived());
            tv_recent.setText(feeModelList.get(position).getPaid_rec());
            tv_balance.setText(feeModelList.get(position).getBalance());
            return convertView;

        }
    }

}
