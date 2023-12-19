package com.acooly.showcase.daliy.Utils;

import java.io.*;
import java.util.Properties;
import java.util.logging.Logger;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

public class FTPTools {
    /**
     * 通过FTP上传文件
     *
     * @Author lvhaibao
     * @Date 2018/2/11 21:43
     */

    //用于打印日志
//    private static final Logger log = Logger.getLogger(FTPUtils.class);

    //设置私有不能实例化
    private FTPTools() {

    }

    /**
     * 上传
     *
     * @param hostname    ip或域名地址
     * @param port        端口
     * @param username    用户名
     * @param password    密码
     * @param workingPath 服务器的工作目
     * @param inputStream 要上传文件的输入流
     * @param saveName    设置上传之后的文件名
     * @return
     */
    public static  boolean upload(String hostname, int port, String username, String password, String workingPath, InputStream inputStream, String saveName) {
        boolean flag = false;
        FTPClient ftpClient = new FTPClient();
        //1 测试连接
        if (connect(ftpClient, hostname, port, username, password)) {
            try {
                //2 检查工作目录是否存在
                if (ftpClient.changeWorkingDirectory(workingPath)) {
                    // 3 检查是否上传成功
                    if (storeFile(ftpClient, saveName, inputStream)) {
                        flag = true;
                        disconnect(ftpClient);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                disconnect(ftpClient);
            }
        }
        return flag;
    }

    /**
     * 断开连接
     *
     * @param ftpClient
     * @throws Exception
     */
    public static void disconnect(FTPClient ftpClient) {
        if (ftpClient.isConnected()) {
            try {
                ftpClient.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 测试是否能连接
     *
     * @param ftpClient
     * @param hostname  ip或域名地址
     * @param port      端口
     * @param username  用户名
     * @param password  密码
     * @return 返回真则能连接
     */
    public static boolean connect(FTPClient ftpClient, String hostname, int port, String username, String password) {
        boolean flag = false;
        try {
            //ftp初始化的一些参数
            ftpClient.connect(hostname, port);
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            ftpClient.setControlEncoding("UTF-8");
            if (ftpClient.login(username, password)) {

                flag = true;
            } else {
                try {
                    disconnect(ftpClient);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 上传文件
     *
     * @param ftpClient
     * @param saveName        全路径。如/home/public/a.txt
     * @param fileInputStream 要上传的文件流
     * @return
     */
    public static boolean storeFile(FTPClient ftpClient, String saveName, InputStream fileInputStream) {
        boolean flag = false;
        try {
            if (ftpClient.storeFile(saveName, fileInputStream)) {
                flag = true;
                disconnect(ftpClient);
            }
        } catch (IOException e) {
            disconnect(ftpClient);
            e.printStackTrace();
        }
        return flag;
    }


    public static void main(String[] args) {
        String input = "13800138000138001380011380013800213800138003";
        int step = 11;
        int length = input.length();
        int count = (length + step - 1) / step;

        for (int i = 0; i < count; i++) {
            int startIndex = i * step;
            int endIndex = Math.min(startIndex + step, length);
            StringBuilder sb = new StringBuilder();
            for (int j = startIndex; j < endIndex; j++) {
                sb.append(input.charAt(j));
            }
            System.out.println(sb.toString());
        }
    }



}
