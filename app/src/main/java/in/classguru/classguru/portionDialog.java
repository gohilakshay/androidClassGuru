package in.classguru.classguru;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

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

import in.classguru.classguru.models.PortionModel;

/**
 * Created by a2z on 2/21/2018.
 */

public class portionDialog extends AppCompatDialogFragment {
    private EditText et_totalTopics;
    private EditText et_remainTopics;
    private EditText et_totalTopicName;
    private EditText et_remainTopicName;
    private PortionAddListener listener;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
       // return super.onCreateDialog(savedInstanceState);
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view =inflater.inflate(R.layout.layout_dialog,null);

        String globaldbname = getArguments().getString("globaldbname");
        final String globalPortionid = getArguments().getString("globalid");
        String globalpermissin = getArguments().getString("globalpermissin");

        builder.setView(view)
                .setTitle("Update Portion")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String et_totalTopics1 = et_totalTopics.getText().toString();
                        String et_remainTopics1 = et_remainTopics.getText().toString();
                        String et_totalTopicName1 = et_totalTopicName.getText().toString();
                        String et_remainTopicName1 = et_remainTopicName.getText().toString();

                        /*String portionBatchSelect = portionBatchSpinner.getSelectedItem().toString();
                        String portionSubjSelect = portionSubjSpinner.getSelectedItem().toString();*/
                        listener.applyTexts(et_totalTopics1,et_remainTopics1,et_totalTopicName1,et_remainTopicName1,globalPortionid);
                    }
                });



        /*portionBatchSpinner = (Spinner)view.findViewById(R.id.portionBatchSpinner);
        portionSubjSpinner = (Spinner)view.findViewById(R.id.portionSubjSpinner);*/


        et_totalTopics = view.findViewById(R.id.et_totalTopics);
        et_remainTopics = view.findViewById(R.id.et_remainTopics);
        et_totalTopicName = view.findViewById(R.id.et_totalTopicName);
        et_remainTopicName = view.findViewById(R.id.et_remainTopicName);

        /*FacPortionbatch_fetch facPortionbatch_fetch = new FacPortionbatch_fetch(getContext());
        facPortionbatch_fetch.execute("faculty",globalPortionid,globalpermissin,globaldbname);*/
        Fac_Portion_fetch fac_portion_fetch = new Fac_Portion_fetch(getContext());
        fac_portion_fetch.execute("PortionView",globalPortionid,globaldbname);
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
        void applyTexts(String portiontotal, String portionremain, String portionBatchSelect, String portionSubjSelect, String globalPortionid);
    }




    public class Fac_Portion_fetch extends AsyncTask<String,Void,String> {
        Context context;
        android.app.AlertDialog alertDialog;

        Fac_Portion_fetch(Context ctx) {
            context = ctx;
        }

        @Override
        protected String doInBackground(String... params) {
            String type = params[0];
            String id = params[1];
            String db = params[2];
            String login_url = "https://classes.classguru.in/api/addFacPortion.php";
            if (type.equals("PortionView")) {
                try {
                    URL profile_url = new URL(login_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) profile_url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String post_data = URLEncoder.encode("portionId", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8") + "&"
                            + URLEncoder.encode("dbname", "UTF-8") + "=" + URLEncoder.encode(db, "UTF-8")+ "&"
                            + URLEncoder.encode("view", "UTF-8") + "=" + URLEncoder.encode("123", "UTF-8");
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

            } else {
                return "faculty";
            }
            return "Fail";
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            //Log.i("TAG",result);
            try {
                JSONObject reader = new JSONObject(result);
                JSONObject test = new JSONObject(reader.getString("portion_details"));
                Log.i("TAG","heya"+test.getString("portion_ID"));
                et_totalTopics.setText(test.getString("no_of_topics"));
                et_remainTopics.setText(test.getString("remained_topics"));
                et_totalTopicName.setText(test.getString("syllabus"));
                et_remainTopicName.setText(test.getString("complete_syllabus"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
            super.onPostExecute(result);
        }
    }
}
