package in.classguru.classguru;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

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
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AnnouncAddFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AnnouncAddFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AnnouncAddFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TextView et_datepicker1;
    int year_x,month_x,day_x;
    static final int DILOG_ID = 0;
    public Spinner spinner2;
    public String batch_id = "9";
    public Button btn_facAddAnnounc;
    public EditText tv_title;
    public EditText tv_description;
    public TextView tv_sName;
    public TextView tv_sNumb;

    private OnFragmentInteractionListener mListener;
    private HashMap<String, String> spinnerMap;

    public AnnouncAddFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AnnouncViewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AnnouncAddFragment newInstance(String param1, String param2) {
        AnnouncAddFragment fragment = new AnnouncAddFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fac_announc_add_layout, container, false);
        final Calendar cal = Calendar.getInstance();
        year_x = cal.get(Calendar.YEAR);
        month_x = cal.get(Calendar.MONTH);
        day_x = cal.get(Calendar.DAY_OF_MONTH);
        et_datepicker1 = (TextView)rootView.findViewById(R.id.et_datepicker1);
        tv_title = (EditText)rootView.findViewById(R.id.et_NotTitle);
        tv_description = (EditText)rootView.findViewById(R.id.et_notDesc);
        spinner2 = (Spinner)rootView.findViewById(R.id.spinner2);
        showDialogOnClick();

        FacAnnouncbatch_fetch facAnnouncbatch_fetch = new FacAnnouncbatch_fetch(getContext());
        facAnnouncbatch_fetch.execute("faculty",mParam1,mParam2);
        btn_facAddAnnounc = (Button)rootView.findViewById(R.id.btn_FacAddAnnounc);
        btn_facAddAnnounc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                batch_id = spinnerMap.get(spinner2.getSelectedItem().toString());

                String title = tv_title.getText().toString();
                String description = tv_description.getText().toString();
                String date = year_x+"-"+month_x+"-"+day_x;
                if(!title.equals(" ") && !title.equals("") && !description.equals(" ") && !description.equals("") && !date.equals("Click to select Text") && !batch_id.equals("Select batch")){
                    AddAnnoc_post addAnnoc_post = new AddAnnoc_post(getContext());
                    addAnnoc_post.execute("FacAnnouncAdd",title,description,date,batch_id,mParam1);
                }else{
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                    alertDialogBuilder.setMessage("Enter all Fields");
                    alertDialogBuilder.show();
                }

            }
        });
        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void showDialogOnClick(){


        et_datepicker1.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getActivity().showDialog(DILOG_ID);

                    }
                }
        );
    }

    public class FacAnnouncbatch_fetch extends AsyncTask<String,Void,String> {
        Context context;
        android.app.AlertDialog alertDialog;
        FacAnnouncbatch_fetch (Context ctx){
            context = ctx;
        }

        @Override
        protected String doInBackground(String... params) {
            String type = params[0];
            String id = params[1];
            String dbname = params[2];
            String login_url = "https://classes.classguru.in/api/teacher_details.php";
            if(type.equals("faculty")){
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
                spinnerMap = new HashMap<String, String>();
                for(int i =0;i<fulltest.length();i++){

                    JSONObject reader1 = new JSONObject(fulltest.getString(i));
                    facStudAttendModelList.add(reader1.getString("batch_name") );
                    spinnerMap.put(reader1.getString("batch_name"),reader1.getString("batch_ID"));
                    //  facStudAttendModelList.add(reader1.getString("batch_name"));
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext().getApplicationContext(),R.layout.support_simple_spinner_dropdown_item,facStudAttendModelList);
                adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                spinner2.setAdapter(adapter);
                /*alertDialog.setMessage(reader1.getString("batch_name"));
                alertDialog.show();*/



            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


    }
    public class AddAnnoc_post extends AsyncTask<String,Void,String>{

        Context context;
        android.app.AlertDialog alertDialog;
        AddAnnoc_post (Context ctx){
            context = ctx;
        }

        @Override
        protected String doInBackground(String... params) {

            String type = params[0];
            String title = params[1];
            String description = params[2];
            String date = params[3];
            String batch_id = params[4];
            String dbname = params[5];
            String login_url = "https://classes.classguru.in/api/FacmarkNotice.php";
            if(type.equals("FacAnnouncAdd")){
                try {
                    URL profile_url = new URL(login_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) profile_url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String post_data = URLEncoder.encode("batch_id","UTF-8")+"="+URLEncoder.encode(batch_id,"UTF-8")+"&"
                            +URLEncoder.encode("dbname","UTF-8")+"="+URLEncoder.encode(mParam2,"UTF-8")+"&"
                            +URLEncoder.encode("date","UTF-8")+"="+URLEncoder.encode(date,"UTF-8")+"&"
                            +URLEncoder.encode("description","UTF-8")+"="+URLEncoder.encode(description,"UTF-8")+"&"
                            +URLEncoder.encode("title","UTF-8")+"="+URLEncoder.encode(title,"UTF-8")+"&"
                            +URLEncoder.encode("t_ID","UTF-8")+"="+URLEncoder.encode(mParam1,"UTF-8");
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
                return "Stud";
            }else{
                return "faculty";
            }
            // return null;
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

            if(result.equals("  New record created successfully")){
                alertDialog.setMessage("Notice Added successfully");
                alertDialog.show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getActivity().finish();
                        startActivity(getActivity().getIntent());

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
