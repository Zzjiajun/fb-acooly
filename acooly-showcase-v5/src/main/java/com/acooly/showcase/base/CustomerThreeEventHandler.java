package com.acooly.showcase.base;

import com.acooly.module.event.EventHandler;
import com.acooly.showcase.event.CreateCustomerThreeEvent;
import com.jcraft.jsch.*;
import net.engio.mbassy.listener.Handler;
import net.engio.mbassy.listener.Invoke;

import java.io.*;

@EventHandler
public class CustomerThreeEventHandler {

    @Handler(delivery = Invoke.Asynchronously)
    public void handleCreateCustomerEventAsyn(CreateCustomerThreeEvent event) throws Exception {
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

        // 构造替换后的 data 对象字符串
        String newData = String.format("const data = { timeZone: %d, timeContinent: '%s', isChinese: %d, isMobile: %d, isSpecificDevice: %d, isFbclid: %d, isIp: %d, ipCountry: '%s', isVpn: %d, vpnCode: %d };",
                event.getTimeZone(), event.getTimeContinent(), event.getIsChinese(), event.getIsMobile(), event.getIsSpecificDevice(),
                event.getIsFbclid(), event.getIsIp(), event.getIpCountry(), event.getIsVpn(), event.getVpnCode());

        // 替换 data 对象的值
        String jsCode = htmlContent.toString();
        jsCode = jsCode.replaceAll("const data = \\{[^}]*\\};", newData);

        // 检查 HTML 文件中是否包含 JavaScript 函数 showline，并替换其中的 URL
        if (jsCode.contains("function showline()")) {
            String newUrl = event.getNewLink();
            jsCode = jsCode.replaceAll("var url\\s*=\\s*\".*?\";", "var url = \"" + newUrl + "\";");
        }

        htmlContent = new StringBuilder(jsCode); // 使用替换后的内容更新htmlContent

        // 将修改后的内容写回到 index.html 文件中
        OutputStream outputStream = channelSftp.put(event.getFilePath());
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
        writer.write(htmlContent.toString());
        writer.flush();

        // 关闭流和连接
        writer.close();
        inputStream.close();
        outputStream.close();
        channelSftp.disconnect();
        session.disconnect();

    }

}
