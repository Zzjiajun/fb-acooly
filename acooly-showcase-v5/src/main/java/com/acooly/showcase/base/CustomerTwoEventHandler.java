package com.acooly.showcase.base;

import com.acooly.module.event.EventHandler;
import com.acooly.showcase.event.CreateCustomerTwoEvent;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import net.engio.mbassy.listener.Handler;
import net.engio.mbassy.listener.Invoke;

import java.io.*;
@EventHandler
public class CustomerTwoEventHandler {
    @Handler(delivery = Invoke.Asynchronously)
    public void handleCreateCustomerEventAsyn(CreateCustomerTwoEvent event) throws Exception {
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
        if (jsCode.contains("window.onload = function()")) {
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



}
