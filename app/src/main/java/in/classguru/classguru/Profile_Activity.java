package in.classguru.classguru;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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
 * Created by a2z on 1/25/2018.
 */

public class Profile_Activity extends AsyncTask<String,Void,String> {

    Context context;
    AlertDialog alertDialog;
    Profile_Activity (Context ctx){
        context = ctx;
    }
    @Override
    protected String doInBackground(String... params) {
        String type = params[0];
        String id = params[1];
        String permission = params[2];
        String dbname = params[3];
        String login_url = "https://classes.classguru.in/class/api/student_details.php";
        if(type.equals("student")){
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

        }

        return null;
    }

    @Override
    protected void onPreExecute() {
        alertDialog = new AlertDialog.Builder(context).create();
        super.onPreExecute();
    }
    /*private String name;*/
    @Override
    protected void onPostExecute(String result) {

        try {
            JSONObject reader = new JSONObject(result);
            String checkResult = reader.getString("student_details");
            /*if(checkResult!=null && !checkResult.equals("Not Valid User") && !checkResult.equals("Database not Selected") && !checkResult.equals("No Input Found")){
                Intent intent = new Intent(context, Home_activity.class);
                context.startActivity(intent);
            }else{
                alertDialog.setMessage("Sorry Enter a valid username and password !!");
                alertDialog.show();
            }*/

            alertDialog.setMessage(checkResult);
            alertDialog.show();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}
