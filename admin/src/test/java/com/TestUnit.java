package com;

import com.fasterxml.jackson.core.JsonProcessingException;

import com.songlingadmin.util.baidu.AccessToken;
import com.songlingadmin.util.baidu.Base64Util;
import com.songlingadmin.util.baidu.FileUtil;
import com.songlingadmin.util.baidu.HttpUtil;
import org.json.JSONObject;
import org.junit.Test;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
public class TestUnit {

    @Test
    public void abc() throws JsonProcessingException {

        System.out.println(34243);

    }


    @Test//测百度ai
    public  void licensePlate() {
        // 请求url
        String url = "https://aip.baidubce.com/rest/2.0/ocr/v1/license_plate";
        try {
            // 本地文件路径
            String filePath = "E:\\tmp\\car.jpg";
            byte[] imgData = FileUtil.readFileByBytes(filePath);
            String imgStr = Base64Util.encode(imgData);
            String imgParam = URLEncoder.encode(imgStr, "UTF-8");

            String param = "image=" + imgParam;

            // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
            String accessToken = AccessToken.get();

            String result = HttpUtil.post(url, accessToken, param);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
