package in.classguru.classguru;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.util.ArrayList;
import java.util.List;

public class FacPortionUpdateActivity extends Faculty_Home_Activity {
    String globalid;
    String globalpermissin;
    String globaldbname;
    String globalname;
    String globalnumb;
    String globalurl;
    String portion_id;
    int total_topics;
    public DrawerLayout mDrawerLayout;
    public ActionBarDrawerToggle mToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fac_portion_update);

        mDrawerLayout = (DrawerLayout)findViewById(R.id.FacPortiondrawer);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent data = getIntent();
        String id = data.getStringExtra("id");
        globalid = id;
        String permission = data.getStringExtra("permission");
        globalpermissin = permission;
        String dbname = data.getStringExtra("dbname");
        globaldbname = dbname;
        String port_id = data.getStringExtra("portion_id");
        portion_id = port_id;
        String total_top = data.getStringExtra("total_topics");
        total_topics = Integer.parseInt(total_top);

        TableLayout Tl_portionView = (TableLayout)findViewById(R.id.Ll_portionUpdateView);
        //TableLayout.LayoutParams lp = new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        //Tl_portionView.setLayoutParams(lp);
        for(int i=1;i < total_topics+1 ; i++){
            TableRow Tr = new TableRow(this);
            EditText t = new EditText(this);
            t.setId(i);
            t.setHint("Enter Topic "+ i);
            int j = i+1;
            i++;
            CheckBox c = new CheckBox(this);
            c.setId(j);
            c.getTag(i);
            Tr.addView(t);
            Tr.addView(c);
            Tl_portionView.addView(Tr);
        }
        LinearLayout Ll_portionUpdateView = (LinearLayout)findViewById(R.id.Ll_portionUpdateView);
        Button btn = new Button(this);
        btn.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        btn.setText("Update Portion");
        Ll_portionUpdateView.addView(btn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alert = new AlertDialog.Builder(FacPortionUpdateActivity.this).create();
                /*int i =2;
                alert.setTitle("Test");
                alert.setMessage(((EditText)findViewById(i)).getText().toString());
                alert.show();*/

                List<String> portionTitleList = new ArrayList<>();
                List<String> portionCompleteList = new ArrayList<>();
                for(int i=1;i < total_topics+1 ; i++){
                    String topics = ((EditText)findViewById(i)).getText().toString();
                    portionTitleList.add(topics);
                    int j= i+1;
                    i++;
                    if(((CheckBox)findViewById(j)).isChecked()){
                        portionCompleteList.add(topics);
                    }
                }

                String allTopic = android.text.TextUtils.join(",",portionTitleList);
                String selectedTopic = android.text.TextUtils.join(",",portionCompleteList);
                /*alert.setTitle("Test");
                alert.setMessage( android.text.TextUtils.join(",",portionCompleteList));
                alert.show();*/
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
