package yothio.camera;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.FileDescriptor;
import java.io.IOException;

import yothio.camera.Util.PermissionUtil;
import yothio.camera.network.CloudVisionManager;

public class MainActivity extends AppCompatActivity {

    Button cameraBtn, getPicBtn, cloudVisionBtn;
    final int CAMERA_RESULT_CODE = 1234;
    final private int GET_PIC_RESULT_CODE = 4321;
    private ImageView imageView;
    private Bitmap mBitmap;
    private CloudVisionManager cloudVisionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cameraBtn = (Button) findViewById(R.id.camera_start_button);
        getPicBtn = (Button) findViewById(R.id.get_pic_button);
        cloudVisionBtn = (Button)findViewById(R.id.cloud_vision_button);
        imageView = (ImageView) findViewById(R.id.imageView);
        cloudVisionManager = new CloudVisionManager();

        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCamera();
            }
        });
        getPicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGalleryChoose();
            }
        });
        cloudVisionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("メイン","開始");
                try {
                    cloudVisionManager.getCloudVisionData(mBitmap, getApplicationContext(), new CloudVisionManager.Callback() {
                        @Override
                        public void callback(String str) {
                            Log.d("メイン",str);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.d("メイン","終わり");
            }
        });
    }

    private void startCamera() {
        if (PermissionUtil.requestPermission(this, CAMERA_RESULT_CODE,
                Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA)) {
            //カメラ起動
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, CAMERA_RESULT_CODE);
        }
    }

    private void startGalleryChoose(){
        if(PermissionUtil.requestPermission(this,GET_PIC_RESULT_CODE,Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            startActivityForResult(intent, GET_PIC_RESULT_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case CAMERA_RESULT_CODE:
                if (PermissionUtil.grantedPermission(requestCode,CAMERA_RESULT_CODE,grantResults)){
                    startCamera();
                }
                break;
            case GET_PIC_RESULT_CODE:
                if(PermissionUtil.grantedPermission(requestCode,GET_PIC_RESULT_CODE,grantResults)){
                    startGalleryChoose();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //キャンセルした時
        if (data == null) {
            return;
        }
        //カメラ起動したとき
        if (requestCode == CAMERA_RESULT_CODE) {
            mBitmap = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(mBitmap);
        }
        //ギャラリーから画像を取ってくるとき
        if (requestCode == GET_PIC_RESULT_CODE && data != null) {
            Log.d("確認", data.getDataString());
            try {
                mBitmap = getBitmapFromUri(data.getData());
                imageView.setImageBitmap(mBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param uri storage path
     * @return Uriのimage
     * @throws IOException
     */
    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        //contentResolverのインスタンスを獲得し、uri読み取りモード起動
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        //ファイルの拡張子を取得
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        //取得した拡張子をBitmapに変換し、返す
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }
}