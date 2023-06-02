package com.chainup.exchange;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;
import org.apache.commons.codec.digest.DigestUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

        import org.junit.jupiter.api.Test;

public class PlatformApiDemo {
    public void demo() {
        try {
        OkHttpClient client = new OkHttpClient();

        //请求接口文档url
        String url = "https://service.xxx.com/platformapi/chainup/open/auth/token";
        String secret = "***密钥***";
        //请求参数
        HashMap<String,String> dataMap = new HashMap<String,String>();
        dataMap.put("appKey", "appKey");
        dataMap.put("code", "code");
        //生成签名
        String sign = this.getSign(dataMap, secret);
        dataMap.put("sign", sign);

        String jsonStr = JSON.toJSONString(dataMap);
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, jsonStr);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .build();

        Response response = client.newCall(request).execute();
        JSONObject jsonObj = JSONObject.parseObject(response.body().string());
        System.out.println("返回："+jsonObj.toJSONString());
    } catch (Exception e) {
        e.printStackTrace();
    }
}

    /**
     * 生成签名
     */
    private String getSign(HashMap<String,String> resultMap, String secret) throws Exception {
        Set<String> keys = resultMap.keySet();
        String[] paramArr = keys.toArray(new String[keys.size()]);
        Arrays.sort(paramArr);
        StringBuilder sb = new StringBuilder();
        for(String key : paramArr) {
            String value = resultMap.get(key);
            sb.append(key).append(value);
        }
        String lastStr = sb.toString() + secret;
        String digest = DigestUtils.md5Hex(lastStr.getBytes("UTF-8"));

        return digest;
    }
}