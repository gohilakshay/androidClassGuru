package in.classguru.classguru;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

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


       // TableLayout Ll_portionView = (TableLayout)findViewById(R.id.Ll_portionUpdateView);
        for(int i=1;i < total_topics+1 ; i++){
            LinearLayout Ll_portionView = (LinearLayout)findViewById(R.id.Ll_portionUpdateView);
            EditText t = new EditText(this);
            t.setId(i);
            t.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            t.setHint("Enter Topic "+ i);
            Ll_portionView.addView(t);
            CheckBox c = new CheckBox(this);
            c.getTag(i);
            c.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            Ll_portionView.addView(c);
        }
        LinearLayout Ll_portionUpdateView = (LinearLayout)findViewById(R.id.Ll_portionUpdateView);
        Button btn = new Button(this);
        btn.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        btn.setText("Update Portion");
        Ll_portionUpdateView.addView(btn);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
