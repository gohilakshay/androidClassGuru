package in.classguru.classguru;

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
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    public Spinner portionBatchSpinner;
    public Spinner portionSubjSpinner;
    public EditText et_totalTopicTab;
    public EditText et_remTopicTab;
    public EditText et_totalTopicName;
    public EditText et_remTopicName;

    public AddFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddFragment newInstance(String param1, String param2) {
        AddFragment fragment = new AddFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_add, container, false);
        portionBatchSpinner = (Spinner)rootView.findViewById(R.id.portionBatchSpinner);
        portionSubjSpinner = (Spinner)rootView.findViewById(R.id.portionSubjSpinner);
        et_totalTopicTab = (EditText)rootView.findViewById(R.id.et_totalTopicTab);
        et_remTopicTab = (EditText)rootView.findViewById(R.id.et_remTopicTab);
        et_totalTopicName = (EditText)rootView.findViewById(R.id.et_totalTopicName);
        et_remTopicName = (EditText)rootView.findViewById(R.id.et_remTopicName);

        FacPortionbatch_fetch facPortionbatch_fetch = new FacPortionbatch_fetch(getContext());
        facPortionbatch_fetch.execute("faculty",mParam1,mParam2);
        Button btn_addPortion = (Button)rootView.findViewById(R.id.btn_addportionFrag);
        btn_addPortion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String portionBatchSelect = portionBatchSpinner.getSelectedItem().toString();
                String portionSubjSelect = portionSubjSpinner.getSelectedItem().toString();
                String et_totalTop = et_totalTopicTab.getText().toString();
                String et_remTop = et_remTopicTab.getText().toString();
                String et_totalTopicN = et_totalTopicName.getText().toString();
                String et_remTopicN = et_remTopicName.getText().toString();

                String[] splitBatch = portionBatchSelect.split(" ");
                AddPortion_post addPortion_post = new AddPortion_post(getContext());
                addPortion_post.execute("FacPortionAdd",et_totalTop,et_remTop,splitBatch[0],portionSubjSelect,et_totalTopicN,et_remTopicN);
            }
        });
        // Inflate the layout for this fragment
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

    public class AddPortion_post extends AsyncTask<String,Void,String> {

        Context context;
        android.app.AlertDialog alertDialog;
        AddPortion_post (Context ctx){
            context = ctx;
        }

        @Override
        protected String doInBackground(String... params) {
            String type = params[0];
            String totalTopics = params[1];
            String remainTopics = params[2];
            String batch = params[3];
            String subject = params[4];
            String totalTopicsName = params[5];
            String compTopicsName = params[6];

            String login_url = "https://classes.classguru.in/api/addFacPortion.php";
            if(type.equals("FacPortionAdd")){
                try {
                    URL profile_url = new URL(login_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) profile_url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String post_data = URLEncoder.encode("batch","UTF-8")+"="+URLEncoder.encode(batch,"UTF-8")+"&"
                            +URLEncoder.encode("dbname","UTF-8")+"="+URLEncoder.encode(mParam2,"UTF-8")+"&"
                            +URLEncoder.encode("subject","UTF-8")+"="+URLEncoder.encode(subject,"UTF-8")+"&"
                            +URLEncoder.encode("remainTopics","UTF-8")+"="+URLEncoder.encode(remainTopics,"UTF-8")+"&"
                            +URLEncoder.encode("totalTopics","UTF-8")+"="+URLEncoder.encode(totalTopics,"UTF-8")+"&"
                            +URLEncoder.encode("userId","UTF-8")+"="+URLEncoder.encode(mParam1,"UTF-8")+"&"
                            +URLEncoder.encode("syllabus","UTF-8")+"="+URLEncoder.encode(totalTopicsName,"UTF-8")+"&"
                            +URLEncoder.encode("completeTopics","UTF-8")+"="+URLEncoder.encode(compTopicsName,"UTF-8")+"&"
                            +URLEncoder.encode("add","UTF-8")+"="+URLEncoder.encode("123","UTF-8");
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

            if(result.equals(" added info  ")){
                alertDialog.setMessage("Portion Added successfully");
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
                for(int i =0;i<fulltest.length();i++){

                    JSONObject reader1 = new JSONObject(fulltest.getString(i));
                    facStudAttendModelList.add(reader1.getString("batch_ID") +" "+reader1.getString("batch_name") );
                    //  facStudAttendModelList.add(reader1.getString("batch_name"));
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,R.layout.support_simple_spinner_dropdown_item,facStudAttendModelList);
                adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                portionBatchSpinner.setAdapter(adapter);

                List<String> PortionSubjList = new ArrayList<>();
                String fulltest1 = reader.getString("teacher_subject_details");
                String[] seperated = fulltest1.split(",");
                PortionSubjList.add("Select Subject");
                for(int i=0; i < seperated.length;i++){
                    PortionSubjList.add(seperated[i]);
                }
                ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(context,R.layout.support_simple_spinner_dropdown_item,PortionSubjList);
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
