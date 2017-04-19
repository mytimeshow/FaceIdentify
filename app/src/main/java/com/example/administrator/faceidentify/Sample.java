package com.example.administrator.faceidentify;

import com.baidu.aip.face.AipFace;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/4/18 0018.
 */

public class Sample {
    //设置APPID/AK/SK
    public static final String APP_ID = "你的 App ID";
    public static final String API_KEY = "你的 Api ID";
    public static final String SECRET_KEY = "你的 Secret Key";

    public static void main(String[] args) {

        // 初始化一个FaceClient
        AipFace client = new AipFace(APP_ID, API_KEY, SECRET_KEY);

        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);

        // 调用API
        String image = "test.jpg";
        JSONObject a = client.detect(image, new HashMap<String, String>());
        System.out.println(a.toString());
    }
}