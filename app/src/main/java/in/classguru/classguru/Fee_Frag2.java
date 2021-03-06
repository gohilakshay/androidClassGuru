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
import android.widget.ImageView;
import android.widget.ListView;
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

import in.classguru.classguru.models.FeeModal;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Fee_Frag2.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Fee_Frag2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fee_Frag2 extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    public ListView lv_feeShow;

    public Fee_Frag2() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fee_Frag1.
     */
    // TODO: Rename and change types and number of parameters
    public static Fee_Frag2 newInstance(String param1, String param2) {
        Fee_Frag2 fragment = new Fee_Frag2();
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


        View rootView = inflater.inflate(R.layout.fragment_fee__frag2, container, false);

        lv_feeShow = (ListView)rootView.findViewById(R.id.lv_feeShow);

        Fee_Activity_work fee_activity = new Fee_Activity_work(getActivity());
        fee_activity.execute("student",mParam1,mParam2);
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

    public class Fee_Activity_work extends AsyncTask<String,Void,String> {
        Context context;
        android.app.AlertDialog alertDialog;
        public ImageView ivsprofile;

        Fee_Activity_work (Context ctx){
            context = ctx;
        }

        @Override
        protected String doInBackground(String... params) {
            String type = params[0];
            String id = params[1];
            //String permission = params[2];
            String dbname = params[2];
            String login_url = "http://206.189.231.53/admin/api/student_details.php";
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
            try {
                if(!result.equals("faculty")) {
                    JSONObject reader = new JSONObject(result);

                    JSONArray fulltest = reader.getJSONArray("student_fees");
                    JSONObject finalresult = fulltest.getJSONObject(0);

                    List<FeeModal> feeModalList = new ArrayList<>();


                    for (int i=0;i<fulltest.length();i++){
                        JSONObject finalresult1 = fulltest.getJSONObject(i);
                        FeeModal feeModal = new FeeModal();
                        feeModal.setPay_mode(finalresult1.getString("payment_mode"));
                        if(!finalresult1.getString("paydate").equals("")){
                            String[] date = finalresult1.getString("paydate").split("-");
                            feeModal.setPay_date(date[2]+"-"+date[1]+"-"+date[0]);
                        }
                        else
                            feeModal.setPay_date("-");
                        if(!finalresult1.getString("chq_date").equals("")){
                            String[] date = finalresult1.getString("chq_date").split("-");
                            feeModal.setChq_date(date[2]+"-"+date[1]+"-"+date[0]);
                        }
                        else
                            feeModal.setChq_date("-");

                        if(!finalresult1.getString("bank_name").equals(""))
                            feeModal.setBank_name(finalresult1.getString("bank_name"));
                        else
                            feeModal.setBank_name("-");

                        if(!finalresult1.getString("chq_no").equals(""))
                            feeModal.setChq_no(finalresult1.getString("chq_no"));
                        else
                            feeModal.setChq_no("-");

                        if(!finalresult1.getString("transc_id").equals(""))
                            feeModal.setTransc_id(finalresult1.getString("transc_id"));
                        else
                            feeModal.setTransc_id("-");

                        if(!finalresult1.getString("recieved_fee").equals(""))
                            feeModal.setReceived(finalresult1.getString("recieved_fee"));
                        else
                            feeModal.setReceived("-");
                        if(finalresult1.has(("paid_fee"))) {
                            feeModal.setPaid_rec(finalresult1.getString("paid_fee"));
                        }else{
                            feeModal.setPaid_rec("-");
                        }

                        if(!finalresult1.getString("balance_fee").equals(""))
                            feeModal.setBalance(finalresult1.getString("balance_fee"));
                        else
                            feeModal.setBalance("-");
                        feeModalList.add(feeModal);
                    }
                    FeeAdapter feeAdapter = new FeeAdapter(getActivity().getApplicationContext(),R.layout.fee_detail_layout,feeModalList);
                    lv_feeShow.setAdapter(feeAdapter);
                    // Then later, when you want to display image
                    // ImageLoader.getInstance().displayImage("https://classes.classguru.in/"+globalurl, ivsprofile);

                }
                else{
                    alertDialog.setMessage("faculty Work in Progress");
                    alertDialog.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    public class FeeAdapter extends ArrayAdapter {
        private List<FeeModal> feeModelList;
        private  int resource;
        private LayoutInflater inflater;
        public FeeAdapter(@NonNull Context context, int resource, @NonNull List<FeeModal> objects) {
            super(context, resource, objects);
            feeModelList = objects;
            this.resource = resource;
            inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if(convertView == null){
                convertView = inflater.inflate(resource,null);
            }


            TextView tv_paymode = (TextView)convertView.findViewById(R.id.tv_payMode);
            TextView tv_paydate = (TextView)convertView.findViewById(R.id.tv_payDate);
            TextView tv_chqdate = (TextView)convertView.findViewById(R.id.tv_chqPayDate);
            TextView tv_bankname = (TextView)convertView.findViewById(R.id.tv_bankName);
            TextView tv_chq_no = (TextView)convertView.findViewById(R.id.tv_chqNo);
            TextView tv_trancId = (TextView)convertView.findViewById(R.id.tv_trancId);
            TextView tv_received = (TextView)convertView.findViewById(R.id.tv_recievedPay);
            TextView tv_recent = (TextView)convertView.findViewById(R.id.tv_recentPay);
            TextView tv_balance = (TextView)convertView.findViewById(R.id.tv_balance_fee);

            tv_paymode.setText(feeModelList.get(position).getPay_mode());
            tv_paydate.setText(feeModelList.get(position).getPay_date());
            tv_chqdate.setText(feeModelList.get(position).getChq_date());
            tv_bankname.setText(feeModelList.get(position).getBank_name());
            tv_chq_no.setText(feeModelList.get(position).getChq_no());
            tv_trancId.setText(feeModelList.get(position).getTransc_id());
            tv_received.setText(feeModelList.get(position).getReceived());
            tv_recent.setText(feeModelList.get(position).getPaid_rec());
            tv_balance.setText(feeModelList.get(position).getBalance());
            return convertView;

        }
    }
}
