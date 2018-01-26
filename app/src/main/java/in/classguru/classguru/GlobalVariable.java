package in.classguru.classguru;

import android.app.Application;
import android.provider.Settings;

/**
 * Created by a2z on 1/25/2018.
 */

public class GlobalVariable extends Application {
     String permission ;
    String id ;
    public String getPermission() {
        return permission;
    }

    public void setPermission(String permi) {
        this.permission = permi;
    }

    public String getId() {
        return id;
    }

    public void setId(String id1) {
        id = id1;
    }


}
