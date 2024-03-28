package com.acooly.showcase.daliy.Utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baidu.aip.util.Base64Util;

import java.io.File;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AccurateBasic {


    /**
     * 重要提示代码中所需工具类
     * FileUtil,Base64Util,HttpUtil,GsonUtils请从
     * https://ai.baidu.com/file/658A35ABAB2D404FBF903F64D47C1F72
     * https://ai.baidu.com/file/C8D81F3301E24D2892968F09AE1AD6E2
     * https://ai.baidu.com/file/544D677F5D4E4F17B4122FBD60DB82B3
     * https://ai.baidu.com/file/470B3ACCA3FE43788B5A963BF0B625F3
     * 下载
     */
    public static String accurateBasic() {
        // 请求url
        String url = "https://aip.baidubce.com/rest/2.0/ocr/v1/numbers";
        try {
            // 本地文件路径
            String filePath = "D:\\3.jpg";
            byte[] imgData = FileUtil.readFileByBytes(filePath);
            String imgStr = Base64Util.encode(imgData);
            String imgParam = URLEncoder.encode(imgStr, "UTF-8");

            String param = "image=" + imgParam;

            // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
            String accessToken = "24.a35efca5336cbe04e8a8009375183fea.2592000.1712137983.282335-55093663";

            String result = com.acooly.showcase.daliy.Utils.HttpUtil.post(url, accessToken, param);
            JSONObject jsonObject = JSON.parseObject(result);
            JSONArray wordsResult = jsonObject.getJSONArray("words_result");
//            List<String> wordsList = new ArrayList<>();
//            for (int i = 0; i < wordsResult.size(); i++) {
//                JSONObject wordObj = wordsResult.getJSONObject(i);
//                String word = wordObj.getString("words");
//                wordsList.add(word);
//            }
            List<String> wordsList = wordsResult.stream()
                    .map(obj -> ((JSONObject) obj).getString("words"))
                    .collect(Collectors.toList());
            System.out.println(wordsList);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        AccurateBasic.accurateBasic();
    }
}

