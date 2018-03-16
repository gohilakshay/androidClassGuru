package in.classguru.classguru;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import in.classguru.classguru.models.PortionListModel;

public class Fac_PortionListView_Activity extends AppCompatActivity {

    public ListView lv_portionList;
    public Button btn_updatePortionTab1;
    public String portionId;
    public String globaldbname;
    public String globalid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fac__portion_list_view_);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        android.support.v7.app.ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#148388")));
        Intent intent = getIntent();
        String portionList = intent.getStringExtra("portionList");
        String portionComplete = intent.getStringExtra("portionComplete");
        portionId = intent.getStringExtra("portionId");
        globaldbname = intent.getStringExtra("globaldbname");
        globalid = intent.getStringExtra("globalid");

        lv_portionList = (ListView)findViewById(R.id.lv_portionList);
        btn_updatePortionTab1 = (Button)findViewById(R.id.btn_updatePortionTab1);

        String[] portionSplit = portionList.split(",");
        String[] portionCompleteSplit = portionComplete.split(",");

        int i=0;
        List<PortionListModel> portionListModelList = new ArrayList<>();
        for (String s : portionSplit){

            /*portion.put(s,"Name is"+i);*/
            PortionListModel portionListModel = new PortionListModel();
            portionListModel.setTopics(s);
            if(Arrays.asList(portionCompleteSplit).contains(s)){
                portionListModel.setCompleted(1);
            }else{
                portionListModel.setCompleted(0);
            }
            portionListModelList.add(portionListModel);
        }
        FacPortionAdapter facPortionAdapter = new FacPortionAdapter(getApplicationContext(),R.layout.portion_viewlistrow_layout,portionListModelList);
        lv_portionList.setAdapter(facPortionAdapter);

        btn_updatePortionTab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> completePortion = new ArrayList<>();
                List<String> allPortion = new ArrayList<>();
                for(int i=0; i<lv_portionList.getChildCount() ;i++){
                    View view1 = (View)lv_portionList.getChildAt(i);
                    CheckBox check = (CheckBox)view1.findViewById(R.id.text2);
                    if(check.isChecked()){
                        completePortion.add(check.getTag().toString());
                        allPortion.add(check.getTag().toString());
                    }else{
                        allPortion.add(check.getTag().toString());
                    }
                }
                String completePor = android.text.TextUtils.join(",", completePortion);
                String allPor = android.text.TextUtils.join(",", allPortion);

                Fac_PortionUpdate fac_portionUpdate = new Fac_PortionUpdate(Fac_PortionListView_Activity.this);
                fac_portionUpdate.execute("PortionUpdate",portionId,completePor,allPor);

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return true;
    }


    public class FacPortionAdapter extends ArrayAdapter {

        List<PortionListModel> PortionListModelList;
        private  int resource;
        private LayoutInflater inflater;
        public FacPortionAdapter(@NonNull Context context, int resource, @NonNull List<PortionListModel> objects) {
            super(context, resource, objects);
            PortionListModelList = objects;
            this.resource = resource;
            inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        }
        @SuppressLint("ResourceType")
        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if(convertView == null){
                convertView = inflater.inflate(resource,null);
            }

            TextView text1 = (TextView)convertView.findViewById(R.id.text1);
            CheckBox check = (CheckBox)convertView.findViewById(R.id.text2) ;
            /*TextView tv_subject = (TextView)convertView.findViewById(R.id.tv_PortionSubjView);
            TextView tv_allPortion = (TextView)convertView.findViewById(R.id.tv_allPortion);
            TextView tv_remPortion = (TextView)convertView.findViewById(R.id.tv_remPortion);*/

            text1.setText(PortionListModelList.get(position).getTopics());
            if(PortionListModelList.get(position).getCompleted() == 1){
                check.setChecked(true);
            }
            check.setTag(PortionListModelList.get(position).getTopics());
            //tv_totalTopicsPortion.setText(portionModelList.get(position).getTopic());
            // tv_completedPortionView.setText(portionModelList.get(position).getCompleted());


            return convertView;
        }
    }
    public class Fac_PortionUpdate extends AsyncTask<String,Void,String> {
        Context context;
        android.app.AlertDialog alertDialog;

        Fac_PortionUpdate(Context ctx) {
            context = ctx;
        }

        private ProgressDialog dialog = new ProgressDialog(Fac_PortionListView_Activity.this);
        @Override
        protected String doInBackground(String... params) {

            String type = params[0];
            String id = params[1];
            String syllabus = params[3];
            String completeTopics = params[2];
            Log.i("TageeErroro","id = "+id+" syllabus = "+syllabus+" completeTopics = "+ completeTopics);
            String login_url = "https://classes.classguru.in/api/addFacPortion.php";
            if (type.equals("PortionUpdate")) {
                try {
                    URL profile_url = new URL(login_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) profile_url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String post_data = URLEncoder.encode("portionId", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8") + "&"
                            + URLEncoder.encode("dbname", "UTF-8") + "=" + URLEncoder.encode(globaldbname, "UTF-8")+ "&"
                            + URLEncoder.encode("update", "UTF-8") + "=" + URLEncoder.encode("123", "UTF-8")+ "&"
                            + URLEncoder.encode("syllabus", "UTF-8") + "=" + URLEncoder.encode(syllabus, "UTF-8")+ "&"
                            + URLEncoder.encode("completeTopics", "UTF-8") + "=" + URLEncoder.encode(completeTopics, "UTF-8");
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
            /*alertDialog = new android.app.AlertDialog.Builder(context).create();
            this.dialog.setMessage("Please wait");
            this.dialog.show();*/
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            if(result.equals(" updated info  ")){
                /*alertDialog.setMessage("Portion Updated successfully");
                alertDialog.show();*/
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Intent intent = new Intent(Fac_PortionListView_Activity.this,FacPortionTabActivity.class);
                        intent.putExtra("id",globalid);
                        intent.putExtra("dbname",globaldbname);
                        intent.putExtra("permission","faculty");
                        startActivity(intent);
                        finish();

                    }
                }, 500);
            }else{
                alertDialog.setMessage("Error Occured"+result);
                alertDialog.show();
            }
            super.onPostExecute(result);
        }
    }
}
