package in.classguru.classguru;

import android.content.Context;
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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import in.classguru.classguru.models.FacNoticeModel;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AnnouncViewFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AnnouncViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AnnouncViewFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    public ListView lv_facnotice;

    public AnnouncViewFragment() {
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
    public static AnnouncViewFragment newInstance(String param1, String param2) {
        AnnouncViewFragment fragment = new AnnouncViewFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_announc_view, container, false);
        lv_facnotice = (ListView)rootView.findViewById(R.id.lv_annoinView);
        Fac_announc_fetch fac_announc_fetch = new Fac_announc_fetch(getContext());
        fac_announc_fetch.execute("facultyAnnounce",mParam1,mParam2);
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

    public class Fac_announc_fetch extends AsyncTask<String,Void,String> {
        Context context;
        android.app.AlertDialog alertDialog;
        Fac_announc_fetch (Context ctx){
            context = ctx;
        }

        @Override
        protected String doInBackground(String... params) {
            String type = params[0];
            String id = params[1];
            String dbname = params[2];
            String login_url = "http://206.189.231.53/admin/api/teacher_details.php";
            if(type.equals("facultyAnnounce")){
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
        protected void onPreExecute() {
            alertDialog = new android.app.AlertDialog.Builder(context).create();
            super.onPreExecute();
        }
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
        @Override
        protected void onPostExecute(String result) {
            /*alertDialog.setMessage(result);
            alertDialog.show();*/
            super.onPostExecute(result);

            try {
                JSONObject reader = new JSONObject(result);
                if(!reader.getString("teacher_notice").equals("teacher id not found")){
                    JSONArray fulltest = reader.getJSONArray("teacher_notice");

                    List<FacNoticeModel> facNoticeModelList = new ArrayList<>();
                    for (int i=0;i<fulltest.length();i++){
                        JSONObject finalresult = fulltest.getJSONObject(i);
                        FacNoticeModel facNoticeModel = new FacNoticeModel();

                        facNoticeModel.setBatch(finalresult.getString("batch"));
                        facNoticeModel.setDate(finalresult.getString("date"));
                        facNoticeModel.setDescription(finalresult.getString("Description"));
                        facNoticeModel.setTitle(finalresult.getString("title"));
                        facNoticeModelList.add(facNoticeModel);
                    }
                    AnnounceAdapter announceAdapter = new AnnounceAdapter(getActivity().getApplicationContext(),R.layout.facannounc_view_layout,facNoticeModelList);
                    lv_facnotice.setAdapter(announceAdapter);

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
    public class AnnounceAdapter extends ArrayAdapter {

        List<FacNoticeModel> facNoticeModelList;
        private  int resource;
        private LayoutInflater inflater;

        public AnnounceAdapter(@NonNull Context context, int resource, @NonNull List<FacNoticeModel> objects) {
            super(context, resource, objects);
            facNoticeModelList = objects;
            this.resource = resource;
            inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        }
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if(convertView == null){
                convertView = inflater.inflate(resource,null);
            }
            TextView tv_Batch = (TextView)convertView.findViewById(R.id.tv_facNotBatch);
            TextView tv_Date = (TextView)convertView.findViewById(R.id.tv_facNotDate1);
            final TextView tv_Desc = (TextView)convertView.findViewById(R.id.tv_facNotDesc);
            TextView tv_Title = (TextView)convertView.findViewById(R.id.tv_facNotTitle1);
            final TextView plus = (TextView)convertView.findViewById(R.id.tv_readMore);
            final TextView minus = (TextView)convertView.findViewById(R.id.tv_readLess);
            RelativeLayout relativeLayout = (RelativeLayout)convertView.findViewById(R.id.Rl_anncView);

            tv_Batch.setText(facNoticeModelList.get(position).getBatch());
            Log.i("Tag12",facNoticeModelList.get(position).getDate());
            tv_Date.setText(facNoticeModelList.get(position).getDate());
            tv_Desc.setText(facNoticeModelList.get(position).getDescription());
            tv_Title.setText(facNoticeModelList.get(position).getTitle());

            minus.setVisibility(View.GONE);
            plus.setVisibility(View.GONE);
            tv_Desc.post(new Runnable() {
                @Override
                public void run() {
                    int lineCount = tv_Desc.getLineCount();
                    if(lineCount > 2){
                        minus.setVisibility(View.GONE);
                        plus.setVisibility(View.VISIBLE);
                        tv_Desc.setMaxLines(2);

                    }
                }
            });
            plus.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    plus.setVisibility(View.GONE);
                    minus.setVisibility(View.VISIBLE);
                    tv_Desc.setMaxLines(Integer.MAX_VALUE);

                }
            });

            minus.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    minus.setVisibility(View.GONE);
                    plus.setVisibility(View.VISIBLE);
                    tv_Desc.setMaxLines(2);

                }
            });

            return convertView;

        }
    }
}
