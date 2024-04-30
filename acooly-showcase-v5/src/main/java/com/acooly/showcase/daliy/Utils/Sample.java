//package com.acooly.showcase.daliy.Utils;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.util.Base64;
//import java.net.URLEncoder;
//import java.io.IOException;
//
//public class Sample {
//
//
//    static final OkHttpClient HTTP_CLIENT = new OkHttpClient().newBuilder().build();
//
//    public static void main(String []args) throws IOException {
//        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
//        // image 可以通过 getFileContentAsBase64("C:\fakepath\1.jpg") 方法获取,如果Content-Type是application/x-www-form-urlencoded时,第二个参数传true
//        RequestBody body = RequestBody.create(mediaType, "image=%2F9j%2F4AAQSkZJRgABAQEAYABgAAD%2F2wBDAAQDAwQDAwQEAwQFBAQFBgoHBgYGBg0JCggKDw0QEA8NDw4RExgUERIXEg4PFR...&detect_direction=false");
//        Request request = new Request.Builder()
//                .url("https://aip.baidubce.com/rest/2.0/ocr/v1/numbers?access_token=24.a35efca5336cbe04e8a8009375183fea.2592000.1712137983.282335-55093663")
//                .method("POST", body)
//                .addHeader("Content-Type", "application/x-www-form-urlencoded")
//                .addHeader("Accept", "application/json")
//                .build();
//        Response response = HTTP_CLIENT.newCall(request).execute();
//        System.out.println(response.body().string());
//
//    }
//
//
//
//
//
//    /**
//     * 获取文件base64编码
//     *
//     * @param path      文件路径
//     * @param urlEncode 如果Content-Type是application/x-www-form-urlencoded时,传true
//     * @return base64编码信息，不带文件头
//     */
//     static String getFileContentAsBase64(String path, boolean urlEncode) throws IOException {
//        byte[] b = Files.readAllBytes(Paths.get(path));
//        String base64 = Base64.getEncoder().encodeToString(b);
//        if (urlEncode) {
//            base64 = URLEncoder.encode(base64, "utf-8");
//        }
//        return base64;
//    }
//}
