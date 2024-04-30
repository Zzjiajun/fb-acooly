package com.acooly.showcase.daliy.Utils;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import org.springframework.scheduling.annotation.Async;

import java.io.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ModifyIndexHtml {

    private static final String HOST = "47.236.172.187";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "123456Qwe";

    private final ExecutorService executorService = Executors.newFixedThreadPool(10); // 创建一个固定大小的线程池
    public CompletableFuture<String> future;


    @Async
   public  void createDirectory(String filePath,String originalLink,String newLink)  {

        executorService.submit(() -> {


       try {
           // 建立 SSH 连接
           JSch jsch = new JSch();
           Session session = jsch.getSession(USERNAME, HOST, 22);
           session.setPassword(PASSWORD);
           session.setConfig("StrictHostKeyChecking", "no");
           session.connect();

           // 创建 SFTP 通道
           ChannelSftp channelSftp = (ChannelSftp) session.openChannel("sftp");
           channelSftp.connect();

           // 获取文件输入流
           InputStream inputStream = channelSftp.get(filePath);

           // 读取 index.html 文件内容
           BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
           StringBuilder content = new StringBuilder();
           String line;
           while ((line = reader.readLine()) != null) {
               // 替换链接
               line = line.replace(originalLink, newLink);
               content.append(line).append("\n");
           }
           reader.close();

           // 将修改后的内容写回到 index.html 文件中
           OutputStream outputStream = channelSftp.put(filePath);
           BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
           writer.write(content.toString());
           writer.flush();

           // 关闭流和连接
           writer.close();
           inputStream.close();
           outputStream.close();
           channelSftp.disconnect();
           session.disconnect();
           System.out.println("Link in index.html has been modified successfully.");
       } catch (Exception e) {
           throw new RuntimeException(e);
       }
        });
   }
    public void shutdown() {
        executorService.shutdown(); // 关闭线程池
    }

    public static void main(String[] args) {

        // 指定 index.html 文件路径
        String filePath = "/www/wwwroot/jdxcn.top/jqhgld/index.html";

        // 原始链接和替换链接
        String originalLink = "http://acooly.top/manage/index.html";
        String newLink = "https://chat.whatsapp.com/Ej1goT3sq949DwtkUH7KfL";
//        createDirectory(filePath,originalLink,newLink);
    }
}
