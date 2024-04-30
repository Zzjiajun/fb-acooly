package com.acooly.showcase.base;

import com.acooly.module.event.EventHandler;
import com.acooly.showcase.event.CreateCustomerEvent;
import com.jcraft.jsch.*;
import net.engio.mbassy.listener.Handler;
import net.engio.mbassy.listener.Invoke;


import java.io.*;

@EventHandler
public class CustomerEventHandler {
    //同步事件处理器
//    @Handler
//    public void handleCreateCustomerEvent(CreateCustomerEvent event) {
//
//    }
    //异步事件处理器
    @Handler(delivery = Invoke.Asynchronously)
    public void handleCreateCustomerEventAsyn(CreateCustomerEvent event) throws Exception {
        StringBuilder htmlContent = new StringBuilder();
        JSch jsch = new JSch();
        Session session = jsch.getSession(event.getUsername(), event.getHost(), 22);
        session.setPassword(event.getPassword());
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect();

        // 创建 SFTP 通道
        ChannelSftp channelSftp = (ChannelSftp) session.openChannel("sftp");
        channelSftp.connect();

        // 获取文件输入流
        InputStream inputStream = channelSftp.get(event.getFilePath());

        // 读取 index.html 文件内容
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = reader.readLine()) != null) {
            htmlContent.append(line).append("\n");
        }
        reader.close();

        // 检查 HTML 文件中是否包含 JavaScript 函数 showline
        String jsCode = htmlContent.toString();
        if (jsCode.contains("function showline()")) {
            String newUrl = event.getNewLink();
            String modifiedJsCode = jsCode.replaceAll("var url\\s*=\\s*\".*?\";", "var url = \"" + newUrl + "\";");
//            String modifiedJsCode = jsCode.replaceAll("var url\\s*=\\s*\".*?\";", "var url = \"" + newUrl + "\";");
            htmlContent = new StringBuilder(modifiedJsCode); // 使用替换后的内容更新htmlContent
        }

            // 将修改后的内容写回到 index.html 文件中
        OutputStream outputStream = channelSftp.put(event.getFilePath());
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
        writer.write(htmlContent.toString());
        writer.flush();
//
        // 关闭流和连接
        writer.close();
        inputStream.close();
        outputStream.close();
        channelSftp.disconnect();
        session.disconnect();
    }

//        // 设置响应内容类型为 HTML
//        response.setContentType("text/html");
//
//        // 将修改后的 HTML 内容返回给客户端
//        PrintWriter out = response.getWriter();
//        out.println(htmlContent.toString());


//            JSch jsch = new JSch();
//            Session session = jsch.getSession(event.getUsername(), event.getHost(), 22);
//            session.setPassword(event.getPassword());
//            session.setConfig("StrictHostKeyChecking", "no");
//            session.connect();
//
//            // 创建 SFTP 通道
//            ChannelSftp channelSftp = (ChannelSftp) session.openChannel("sftp");
//            channelSftp.connect();
//
//            // 获取文件输入流
//            InputStream inputStream = channelSftp.get(event.getFilePath());
//
//            // 读取 index.html 文件内容
//            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
//            StringBuilder content = new StringBuilder();
//            String line;
//            while ((line = reader.readLine()) != null) {
//                // 替换链接
//                line = line.replace(event.getOriginalLink(), event.getNewLink());
//                content.append(line).append("\n");
//            }
//            reader.close();
        //do what you like

}
