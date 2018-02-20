package in.classguru.classguru;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.provider.Settings;

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
 * Created by Akshay on 1/21/2018.
 */

public class backgroundWorker extends AsyncTask<String,Void,String> {


    Context context;
    AlertDialog alertDialog;
    backgroundWorker (Context ctx){
        context = ctx;

    }
    @Override
    protected String doInBackground(String... params) {
        String type = params[0];
        String login_url = "https://classes.classguru.in/api/select_user.php";
        if(type.equals("login")){
            try {
                String user_name = params[1];
                String pass_word = params[2];
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("username","UTF-8")+"="+URLEncoder.encode(user_name,"UTF-8")+"&"
                        +URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(pass_word,"UTF-8");
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
        alertDialog.setTitle("Login Status");
    }

    @Override
    protected void onPostExecute(String result) {
        try {
            String id;
            JSONObject reader = new JSONObject(result);
            String checkResult = reader.getString("result");

            if(checkResult!=null && !checkResult.equals("Not Valid User") && !checkResult.equals("Database not Selected") && !checkResult.equals("No Input Found")){
                String dbname = reader.getString("dbname");
                JSONObject reader1 = new JSONObject(checkResult);
                String permission = reader1.getString("permission");
                if(permission.equals("student")){
                    if(reader1.getString("stud_ID").matches("")){
                        id = reader1.getString("t_ID");
                    }
                    else if(reader1.getString("t_ID").matches("")){
                        id = reader1.getString("stud_ID");
                    }else{
                        id = "Not Found";
                    }

                    Intent intent = new Intent(context, Home_activity.class);
                    intent.putExtra("id",id);
                    intent.putExtra("permission",permission);
                    intent.putExtra("dbname",dbname);
                    context.startActivity(intent);
                }
                else{
                    if(reader1.getString("stud_ID").matches("")){
                        id = reader1.getString("t_ID");
                    }
                    else if(reader1.getString("t_ID").matches("")){
                        id = reader1.getString("stud_ID");
                    }else{
                        id = "Not Found";
                    }

                    Intent intent = new Intent(context, Faculty_Home_Activity.class);
                    intent.putExtra("id",id);
                    intent.putExtra("permission",permission);
                    intent.putExtra("dbname",dbname);
                    context.startActivity(intent);
                }
            }else{
                alertDialog.setMessage("Sorry Enter a valid username and password !!");
                alertDialog.show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}
