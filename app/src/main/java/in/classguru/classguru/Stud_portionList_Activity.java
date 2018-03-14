package in.classguru.classguru;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import in.classguru.classguru.models.PortionListModel;

public class Stud_portionList_Activity extends AppCompatActivity {

    public String portionId;
    public String globaldbname;
    public String globalid;
    public ListView lv_portionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stud_portion_list_);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        android.support.v7.app.ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#148388")));

        Intent intent = getIntent();
        String portionList = intent.getStringExtra("portionList");
        String portionComplete = intent.getStringExtra("portionComplete");
        portionId = intent.getStringExtra("portionId");
        globaldbname = intent.getStringExtra("globaldbname");
        globalid = intent.getStringExtra("globalid");

        lv_portionList = (ListView)findViewById(R.id.lv_portionList1);
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
            check.setClickable(false);

            text1.setText(PortionListModelList.get(position).getTopics());
            if(PortionListModelList.get(position).getCompleted() == 1){
                check.setChecked(true);
            }
            check.setTag(PortionListModelList.get(position).getTopics());

            return convertView;
        }
    }
}
