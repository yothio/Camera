package yothio.camera.Util;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import yothio.camera.App;

/**
 * Created by 2150177 on 2017/06/07.
 */

public class PermissionUtil {
    /**
     *
     * @param activity call by activity
     * @param requestCode it use, need permission
     * @param permissions need permissions
     * @return if you allowed it [true] otherwise [false]
     */
    public static boolean requestPermission(Activity activity,int requestCode,String... permissions){
        boolean granted = true;
        List<String> needPermissions = new ArrayList<>();

        for (String permission:permissions){
            int permissionCheck = ContextCompat.checkSelfPermission(activity,permission);
            boolean hasPermission = (permissionCheck == PackageManager.PERMISSION_GRANTED);
            granted &= hasPermission;
            if(!hasPermission){
                needPermissions.add(permission);
            }
        }


        if (granted == true) {
            return true;
        }else{
            ActivityCompat.requestPermissions(activity,
                    needPermissions.toArray(new String[needPermissions.size()]),requestCode);
            return false;
        }
    }


    public static boolean grantedPermission(int requestCode, int permissionCode, int[] grantResults) {

        if(requestCode == permissionCode){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                return true;
            }else{
                return false;
            }
        }
        return false;
    }
}
