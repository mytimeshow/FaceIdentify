package com.example.administrator.faceidentify;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.baidu.aip.face.AipFace;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private Button button;
    private FaceView img;

    private static final int NUB=101;
    public static final String APP_ID = "9537819";
    public static final String API_KEY = "TXGZersV0TWlWTu4G4bbP63t";
    public static final String SECRET_KEY = "s89uABh7uFTYOQLGBLSVcR1oiP28D1DF";

    Handler h=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            Rect rect = (Rect) msg.obj;
            img.drawFace(rect);
            return  true;
        }
    });
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button= (Button) findViewById(R.id.button);
        img= (FaceView) findViewById(R.id.textview);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 初始化一个FaceClient
                final AipFace client = new AipFace(APP_ID, API_KEY, SECRET_KEY);
                // 可选：设置网络连接参数
                client.setConnectionTimeoutInMillis(2000);
                client.setSocketTimeoutInMillis(60000);
                // 调用API

        /*String image = "test.jpg";
        JSONObject res = client.detect(path, new HashMap<String, String>());
        System.out.println(res.toString(2));*/
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.dili3);
                final byte[] imgByte = Bitmap2Bytes(bitmap);
                final HashMap<String, String> paraMap = new HashMap<String, String>();
                paraMap.put("face_fields", "age,beauty,expression,faceshape,gender,glasses,landmark,race,qualities");
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        JSONObject res = client.detect(imgByte,paraMap );
//                Log.e("MainAcitity", res.toString());
                        // left":117,"top":127,"width":207,"height":194
                        // 差json解析
                        Rect r = new Rect((int)(117/1.5f),(int)(127/1.5f),(int)((117+207)/1.5f),(int)((127+194)/1.5f));
                        Message msg = Message.obtain();
                        msg.obj = r;


                        h.sendMessage(msg);

                    }
                }).start();
            }
        });

    }
    private byte[] Bitmap2Bytes(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] datas = baos.toByteArray();
        return datas;
    }
    private String getRealPathFromURI(Uri contentUri) { //传入图片uri地址
        String[] proj = { MediaStore.Images.Media.DATA };
        CursorLoader loader = new CursorLoader(MainActivity.this, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
}
