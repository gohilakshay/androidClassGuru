package in.classguru.classguru;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import in.classguru.classguru.models.PortionModel;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ViewFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    public ListView viewFacPortion;

    public ViewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ViewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ViewFragment newInstance(String param1, String param2) {
        ViewFragment fragment = new ViewFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_view, container, false);
        viewFacPortion = (ListView)rootView.findViewById(R.id.viewFacPortion);
        // Inflate the layout for this fragment
        Fac_Portion_fetch fac_portion_fetch = new Fac_Portion_fetch(getContext());
        fac_portion_fetch.execute("PortionView",mParam1,mParam2);
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
            String login_url = "https://classes.classguru.in/api/teacher_details.php";
            if (type.equals("PortionView")) {
                try {
                    URL profile_url = new URL(login_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) profile_url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String post_data = URLEncoder.encode("userId", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8") + "&"
                            + URLEncoder.encode("dbname", "UTF-8") + "=" + URLEncoder.encode(db, "UTF-8");
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
                JSONArray fulltest = reader.getJSONArray("teacher_portion");
                List<PortionModel> portionModelList = new ArrayList<>();
                for (int i=0;i<fulltest.length();i++){
                    JSONObject finalresult = fulltest.getJSONObject(i);
                    PortionModel portionModel = new PortionModel();
                    portionModel.setBatch(finalresult.getString("batch"));
                    portionModel.setSubject(finalresult.getString("subject_name"));
                    portionModel.setPortionId(finalresult.getString("portion_ID"));
                    portionModel.setTotalTopics(finalresult.getString("no_of_topics"));
                    portionModel.setRemainTopics(finalresult.getString("remained_topics"));
                    portionModel.setTopic(finalresult.getString("syllabus"));
                    portionModel.setCompleted(finalresult.getString("complete_syllabus"));
                    portionModelList.add(portionModel);
                }
                FacPortionAdapter facPortionAdapter = new FacPortionAdapter (getActivity().getApplicationContext(),R.layout.fac_portionview_layout,portionModelList);
                viewFacPortion.setAdapter(facPortionAdapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            super.onPostExecute(result);
        }
    }
    public class FacPortionAdapter extends ArrayAdapter {

        List<PortionModel> portionModelList;
        private  int resource;
        private LayoutInflater inflater;
        public FacPortionAdapter(@NonNull Context context, int resource, @NonNull List<PortionModel> objects) {
            super(context, resource, objects);
            portionModelList = objects;
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

            TextView tv_batch = (TextView)convertView.findViewById(R.id.tv_PortionBatchView);
            TextView tv_subject = (TextView)convertView.findViewById(R.id.tv_PortionSubjView);
            TextView tv_allPortion = (TextView)convertView.findViewById(R.id.tv_allPortion);
            TextView tv_remPortion = (TextView)convertView.findViewById(R.id.tv_remPortion);
            //TextView tv_totalTopicsPortion = (TextView)convertView.findViewById(R.id.tv_totalTopicsPortion);
            //TextView tv_completedPortionView = (TextView)convertView.findViewById(R.id.tv_completedPortionView);
            //ListView lv_portionView = (ListView)convertView.findViewById(R.id.lv_portionView);

            RelativeLayout Rl_PortionView = convertView.findViewById(R.id.Rl_PortionView);
            RelativeLayout.LayoutParams buttonParam = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            Button myButton1 = new Button(getContext());  // create a new Button
            myButton1.setText("View"); // set Text in the Button
            myButton1.setId(1);
            myButton1.setTextColor(Color.WHITE);
            myButton1.setBackgroundColor(Color.parseColor("#09B0B9"));
            myButton1.setLayoutParams(buttonParam); // set defined layout params to Button
            buttonParam.addRule(RelativeLayout.ALIGN_PARENT_END);
            buttonParam.addRule(RelativeLayout.BELOW,R.id.tv_remPortion);
           // buttonParam.addRule(RelativeLayout.ALIGN_BASELINE,R.id.tv_tot);
            buttonParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            Rl_PortionView.addView(myButton1); // add Button in RelativeLayout




            tv_batch.setText(portionModelList.get(position).getBatch());
            tv_subject.setText(portionModelList.get(position).getSubject());
            tv_allPortion.setText(portionModelList.get(position).getTotalTopics());
            tv_remPortion.setText(portionModelList.get(position).getRemainTopics());
            //tv_totalTopicsPortion.setText(portionModelList.get(position).getTopic());
           // tv_completedPortionView.setText(portionModelList.get(position).getCompleted());

            final String s = portionModelList.get(position).getTopic();
            final String s1 = portionModelList.get(position).getCompleted();
            final String s2 = portionModelList.get(position).getPortionId();
            myButton1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /*AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage(s);
                    builder.show();*/
                    Intent intent = new Intent(getContext(),Fac_PortionListView_Activity.class);
                    intent.putExtra("portionList",s);
                    intent.putExtra("portionComplete",s1);
                    intent.putExtra("portionId",s2);
                    intent.putExtra("globaldbname",mParam2);
                    intent.putExtra("globalid",mParam1);
                    startActivity(intent);
                }
            });

             return convertView;
        }
    }
}
