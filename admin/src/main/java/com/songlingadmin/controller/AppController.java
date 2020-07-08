package com.songlingadmin.controller;



import com.alibaba.fastjson.JSONObject;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.songlingadmin.util.baidu.AccessToken;
import com.songlingadmin.util.baidu.Base64Util;
import com.songlingadmin.util.baidu.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.net.URLEncoder;
@Slf4j
@RestController
@RequestMapping("/app" )
public class AppController {
    @PostMapping("/carnumber/uploadPhoto" )
    public AjaxResult importData(MultipartFile file)  {
        try {
            String url = "https://aip.baidubce.com/rest/2.0/ocr/v1/license_plate";
            String imgStr = Base64Util.encode(file.getBytes());
            String imgParam = URLEncoder.encode(imgStr, "UTF-8");
            String param = "image=" + imgParam;
            // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
            String accessToken = AccessToken.get();
            String result = HttpUtil.post(url, accessToken, param);
            log.info("识别结果" + result);
            String number = "";
            JSONObject jsonObject = JSONObject.parseObject(result);
            jsonObject = jsonObject.getJSONObject("words_result");
            if (jsonObject != null) {
                number = jsonObject.getString("number");
                return AjaxResult.success("",number);
            }
        }catch(Exception e){
            return  AjaxResult.error("",e.getMessage());

        }

        return AjaxResult.error("","未识别出,请人工查询");
    }
}
