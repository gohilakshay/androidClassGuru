package in.classguru.classguru;

import android.app.AlertDialog;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

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

/**
 * Created by a2z on 2/21/2018.
 */

public class portionDialog extends AppCompatDialogFragment {
    private EditText et_portionName;
    private EditText et_portionDate;
    private PortionAddListener listener;
    public Spinner portionBatchSpinner;
    public Spinner portionSubjSpinner;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
       // return super.onCreateDialog(savedInstanceState);
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view =inflater.inflate(R.layout.layout_dialog,null);

        builder.setView(view)
                .setTitle("Add Portion")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String portiontotal = et_portionName.getText().toString();
                        String portionremain = et_portionDate.getText().toString();
                        String portionBatchSelect = portionBatchSpinner.getSelectedItem().toString();
                        String portionSubjSelect = portionSubjSpinner.getSelectedItem().toString();
                        listener.applyTexts(portiontotal,portionremain,portionBatchSelect,portionSubjSelect);
                    }
                });

        String globaldbname = getArguments().getString("globaldbname");
        String globalid = getArguments().getString("globalid");
        String globalpermissin = getArguments().getString("globalpermissin");

        portionBatchSpinner = (Spinner)view.findViewById(R.id.portionBatchSpinner);
        portionSubjSpinner = (Spinner)view.findViewById(R.id.portionSubjSpinner);
        et_portionName = view.findViewById(R.id.et_portionName);
        et_portionDate = view.findViewById(R.id.et_portionDate);

        FacPortionbatch_fetch facPortionbatch_fetch = new FacPortionbatch_fetch(getContext());
        facPortionbatch_fetch.execute("faculty",globalid,globalpermissin,globaldbname);

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (PortionAddListener) context;
        } catch (ClassCastException e) {
            throw  new ClassCastException(context.toString() + "Must Implement PortionAddListener");
        }
    }

    public interface PortionAddListener{
        void applyTexts(String portiontotal,String portionremain,String portionBatchSelect,String portionSubjSelect);
    }




    public class FacPortionbatch_fetch extends AsyncTask<String,Void,String> {
        Context context;
        android.app.AlertDialog alertDialog;
        FacPortionbatch_fetch (Context ctx){
            context = ctx;
        }

        @Override
        protected String doInBackground(String... params) {
            String type = params[0];
            String id = params[1];
            String permission = params[2];
            String dbname = params[3];
            String login_url = "https://classes.classguru.in/api/teacher_details.php";
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
            JSONObject reader = null;
            try {
                reader = new JSONObject(result);
                JSONArray fulltest = reader.getJSONArray("batch_details");
                List<String> facStudAttendModelList = new ArrayList<>();
                facStudAttendModelList.add("Select batch");
                for(int i =0;i<fulltest.length();i++){

                    JSONObject reader1 = new JSONObject(fulltest.getString(i));
                    facStudAttendModelList.add(reader1.getString("batch_ID") +" "+reader1.getString("batch_name") );
                    //  facStudAttendModelList.add(reader1.getString("batch_name"));
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,R.layout.support_simple_spinner_dropdown_item,facStudAttendModelList);
                adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                portionBatchSpinner.setAdapter(adapter);

                String fulltest1 = reader.getString("teacher_subject_details");
                String[] seperated = fulltest1.split(",");

                ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(context,R.layout.support_simple_spinner_dropdown_item,seperated);
                adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                portionSubjSpinner.setAdapter(adapter1);
                /*alertDialog.setMessage(reader1.getString("batch_name"));
                alertDialog.show();*/
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


    }
}
