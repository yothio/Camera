package yothio.camera.network;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import java.io.IOException;

/**
 * Created by 2150177 on 2017/06/07.
 */

public class CloudVisionManager {

    private final String TAG = getClass().getName();
    private static CloudVisionManager cloudVisionManager = new CloudVisionManager();
    private CloudVision cloudVision;

    public CloudVisionManager(){
        cloudVision = new CloudVision();
    }

    public static CloudVisionManager getInstance(){
        return cloudVisionManager;
    }

    public void getCloudVisionData(Bitmap bitmap, Context context, final Callback callback) throws IOException {
        Log.d("テスト","A");
        cloudVision.callCloudVision(bitmap, context, new CloudVision.CloudCallBack() {
            @Override
            public void callback(String str) {
                callback.callback(str);
            }

        });
    }

    public interface Callback{
        void callback(String str);
    }

}
