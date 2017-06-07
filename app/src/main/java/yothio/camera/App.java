package yothio.camera;

import android.app.Application;
import android.content.Context;

/**
 * Created by 2150177 on 2017/06/05.
 */

public class App extends Application {
    private static Context sContext;

    public static Context getAppContext(){
        return sContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
    }
}
