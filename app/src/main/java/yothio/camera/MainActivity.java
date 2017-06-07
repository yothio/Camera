package yothio.camera;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.FileDescriptor;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    Button cameraBtn, getPicBtn;
    final int CAMERA_RESULT_CODE = 1234;
    final private int GET_PIC_RESULT_CODE = 4321;
    private ImageView imageView;
    private Bitmap mBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cameraBtn = (Button) findViewById(R.id.camera_start_button);
        getPicBtn = (Button) findViewById(R.id.get_pic_button);
        imageView = (ImageView) findViewById(R.id.imageView);

        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //カメラ起動
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAMERA_RESULT_CODE);
            }
        });

        getPicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(intent, GET_PIC_RESULT_CODE);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //キャンセルした時
        if(data != null){
            return;
        }
        //カメラ起動したとき
        if (requestCode == CAMERA_RESULT_CODE) {
            mBitmap = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(mBitmap);
        }
        //ギャラリーから画像を取ってくるとき
        if (requestCode == GET_PIC_RESULT_CODE && data != null) {
            Log.d("確認",data.getDataString());
            try {
                mBitmap = getBitmapFromUri(data.getData());
                imageView.setImageBitmap(mBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *
     * @param uri
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