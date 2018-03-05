package in.classguru.classguru;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import in.classguru.classguru.models.PortionListModel;

public class Fac_PortionListView_Activity extends AppCompatActivity {

    public ListView lv_portionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fac__portion_list_view_);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        String portionList = intent.getStringExtra("portionList");
        String portionComplete = intent.getStringExtra("portionComplete");

        lv_portionList = (ListView)findViewById(R.id.lv_portionList);

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
            //tv_totalTopicsPortion.setText(portionModelList.get(position).getTopic());
            // tv_completedPortionView.setText(portionModelList.get(position).getCompleted());


            return convertView;
        }
    }
}
