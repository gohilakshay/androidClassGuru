package in.classguru.classguru;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

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
        Fee_Activity_work fee_activity = new Fee_Activity_work(this);
        fee_activity.execute("student",globalid,globalpermissin,globaldbname);

    }

    public class Fee_Activity_work extends AsyncTask<String,Void,String> {
        Context context;
        android.app.AlertDialog alertDialog;
        Fee_Activity_work (Context ctx){
            context = ctx;
        }

        @Override
        protected String doInBackground(String... params) {
            String type = params[0];
            String id = params[1];
            String permission = params[2];
            String dbname = params[3];
            String login_url = "https://classes.classguru.in/class/api/student_details.php";
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
                    String checkResult = reader.getString("student_fees");

                    JSONObject finalresult = new JSONObject(checkResult);

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

}
