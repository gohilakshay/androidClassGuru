package in.classguru.classguru;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
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

/**
 * Created by a2z on 2/24/2018.
 */

public class FacPortionViewDialog extends AppCompatDialogFragment {

    public TextView tv_total;
    public TextView tv_remain;
    public TextView tv_all_portion;
    public TextView tv_rem_portion;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
         View view =inflater.inflate(R.layout.portion_view_d_layout,null);

        builder.setView(view)
                .setTitle("View Portion")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

        String globaldbname = getArguments().getString("globaldbname");
        String globalid = getArguments().getString("globalid");
        String globalpermissin = getArguments().getString("globalpermissin");
        String portion_ID = getArguments().getString("portion_ID");

        FacPortion_fetch facPortion_fetch = new FacPortion_fetch(getContext());
        facPortion_fetch.execute("FacPortionView",globalid,globalpermissin,globaldbname,portion_ID);
        tv_total = (TextView)view.findViewById(R.id.tv_total_portion);
        tv_remain = (TextView)view.findViewById(R.id.tv_remain_portion);
        tv_all_portion = (TextView)view.findViewById(R.id.tv_all_portion);
        tv_rem_portion = (TextView)view.findViewById(R.id.tv_rem_portion);

        return builder.create();
    }

    public class FacPortion_fetch extends AsyncTask<String,Void,String>{

        Context context;
        android.app.AlertDialog alertDialog;
        FacPortion_fetch (Context ctx){
            context = ctx;
        }

        @Override
        protected String doInBackground(String... params) {
            String type = params[0];
            String portion_ID = params[4];
            String permission = params[2];
            String dbname = params[3];
            String portion_view = "Now";
            String login_url = "https://classes.classguru.in/api/addFacPortion.php";
            if(type.equals("FacPortionView")){
                try {
                    URL profile_url = new URL(login_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) profile_url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String post_data = URLEncoder.encode("portion_view","UTF-8")+"="+URLEncoder.encode(portion_view,"UTF-8")+"&"
                            +URLEncoder.encode("dbname","UTF-8")+"="+URLEncoder.encode(dbname,"UTF-8")+"&"
                            +URLEncoder.encode("portion_ID","UTF-8")+"="+URLEncoder.encode(portion_ID,"UTF-8");
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
            /*alertDialog.setMessage(result);
            alertDialog.show();*/
            try {
                JSONObject reader = new JSONObject(result);
                String checkResult = reader.getString("portion_details");
                JSONObject finalresult = new JSONObject(checkResult);

                String no_of_topics = finalresult.getString("no_of_topics");
                tv_total.setText(no_of_topics);

                String remained_topics = finalresult.getString("remained_topics");
                tv_remain.setText(remained_topics);

                String tv_all_por = finalresult.getString("syllabus");
                tv_all_portion.setText(tv_all_por);

                String tv_remain_por = finalresult.getString("complete_syllabus");
                tv_rem_portion.setText(tv_remain_por);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            super.onPostExecute(result);
        }
    }
}
