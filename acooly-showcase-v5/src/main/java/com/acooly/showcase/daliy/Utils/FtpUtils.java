package com.acooly.showcase.daliy.Utils;


import com.jcraft.jsch.Channel;
import com.jcraft.jsch.*;
import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.*;

import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class FtpUtils {
    private static final String SERVER = "47.245.40.246";
    private static final int PORT = 21;
    private static final String USERNAME = "user";
    private static final String PASSWORD = "123";
    private static final String REMOTE_FILE_PATH = "/xxtest.xyz/uk/index.html";
    private static final String LOCAL_TEMP_FILE_PATH = "C:\\Users\\Administrator\\Desktop\\index.html";
    private static final String REPLACE_CONTENT = "3333333333333333333333333333";
    private static final String REPLACE_CONTENT1 ="44444444444444444444444444444444";

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        try {
            FTPClient ftpClient = new FTPClient();
            ftpClient.connect(SERVER, PORT);
            ftpClient.login(USERNAME, PASSWORD);
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            // Download the remote file to a local temp file
            List<String> lines1 = new ArrayList<>();
            BufferedReader reader1 = new BufferedReader(new InputStreamReader(ftpClient.retrieveFileStream(REMOTE_FILE_PATH), StandardCharsets.UTF_8));
            String line1;
            while ((line1 = reader1.readLine()) != null) {
                if (line1.trim().contains(REPLACE_CONTENT)) {
                    line1 = line1.replace(REPLACE_CONTENT, REPLACE_CONTENT1);
                    lines1.add(line1);
                } else {
                    lines1.add(line1);
                }
            }
//            FileOutputStream fos = new FileOutputStream(LOCAL_TEMP_FILE_PATH);
//            String line1;
//            while ((line1 = reader1.readLine()) != null) {
//                fos.write((line1 + System.lineSeparator()).getBytes(StandardCharsets.UTF_8));
//            }
//            reader1.close();
//
//
            ftpClient.logout();
            ftpClient.disconnect();



            FTPClient ftpClient1 = new FTPClient();
            ftpClient1.connect(SERVER, PORT);
            ftpClient1.login(USERNAME, PASSWORD);
            ftpClient1.enterLocalPassiveMode();
            ftpClient1.setFileType(FTP.BINARY_FILE_TYPE);


            // Modify the specified line in the local temp file
//            List<String> lines = new ArrayList<>();
//            try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(LOCAL_TEMP_FILE_PATH), StandardCharsets.UTF_8))) {
//                String line;
//                while ((line = reader.readLine()) != null) {
//                    if (line.trim().contains(REPLACE_CONTENT)) {
//                        line = line.replace(REPLACE_CONTENT, REPLACE_CONTENT1);
//                        lines.add(line);
//                    } else {
//                        lines.add(line);
//                    }
//                }
//            }

//             切换工作目录
            boolean b = ftpClient1.changeWorkingDirectory("xxtest.xyz/uk");
            // 上传文件
            try (InputStream inputStream = convertListToInputStream(lines1)) {
                boolean uploaded = ftpClient1.storeFile("index.html", inputStream);
                if (uploaded) {
                    System.out.println("文件上传成功");
                } else {
                    System.out.println("文件上传失败");
                }
            }
//             Remove the local temp file
//            Files.delete(Paths.get(LOCAL_TEMP_FILE_PATH));

            ftpClient1.logout();
            ftpClient1.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();
        System.out.println("消耗时间为："+(end-start));
    }

    public static InputStream convertListToInputStream(List<String> lines) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String line : lines) {
            stringBuilder.append(line).append("\n");
        }
        return new ByteArrayInputStream(stringBuilder.toString().getBytes(StandardCharsets.UTF_8));
    }


    public static void sshSftp1(byte[] bytes, String fileName) throws IOException, JSchException, SftpException {
        JSch jsch = new JSch();
        Session session = null;
        ChannelSftp channelSftp = null;
        String path = "C:\\wwwroot\\127.0.0.1";
        //指定的服务器地址
        String ip = "47.236.70.169";
        //用户名
        String user = "user";
        //密码
        String password = "FTBsZJ5kzAt3LTfA";
        //服务器端口 默认22
        int port = 21;
        session = jsch.getSession(user, ip, port);
        session.setPassword(password);
        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        session.connect(30000);

        channelSftp = (ChannelSftp) session.openChannel("sftp");
        channelSftp.connect();

//        String remoteFilePath = path + "/" + fileName;
//        FileInputStream fis = new FileInputStream(bytes);
//        channelSftp.put(fis, remoteFilePath);
//        fis.close();
        channelSftp.disconnect();
        session.disconnect();

//        System.out.println("文件上传成功： " + remoteFilePath);
    }


    /**
     * 利用JSch包实现SFTP上传文件
     *
     * @param bytes    文件字节流
     * @param fileName 文件名
     * @throws Exception
     */
    public static void sshSftp(byte[] bytes, String fileName) throws Exception {
        //指定的服务器地址
        String ip = "47.236.70.169";
        //用户名
        String user = "admin";
        //密码
        String password = "dx.123QUTI3BMUIDS72FNOQV7MF333";
        //服务器端口 默认22
        int port = 21;
        //上传文件到指定服务器的指定目录 自行修改
        String path = "C:\\wwwroot\\127.0.0.1";

        Session session = null;
        Channel channel = null;


        JSch jsch = new JSch();


        if (port <= 0) {
            //连接服务器，采用默认端口
            session = jsch.getSession(user, ip);
        } else {
            //采用指定的端口连接服务器
            session = jsch.getSession(user, ip, port);
        }

        //如果服务器连接不上，则抛出异常
        if (session == null) {
            throw new Exception("session is null");
        }

        //设置登陆主机的密码
        session.setPassword(password);//设置密码
        //设置第一次登陆的时候提示，可选值：(ask | yes | no)
        session.setConfig("StrictHostKeyChecking", "no");
        //设置登陆超时时间
        session.connect(30000000);


        OutputStream outstream = null;
        try {
            //创建sftp通信通道
            channel = (Channel) session.openChannel("sftp");
            channel.connect(1000);
            ChannelSftp sftp = (ChannelSftp) channel;


            //进入服务器指定的文件夹
            sftp.cd(path);

            //列出服务器指定的文件列表
//            Vector v = sftp.ls("*");
//            for(int i=0;i<v.size();i++){
//                System.out.println(v.get(i));
//            }

            //以下代码实现从本地上传一个文件到服务器，如果要实现下载，对换以下流就可以了
            outstream = sftp.put(fileName);
            outstream.write(bytes);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //关流操作
            if (outstream != null) {
                outstream.flush();
                outstream.close();
            }
            if (session != null) {
                session.disconnect();
            }
            if (channel != null) {
                channel.disconnect();
            }
        }
    }
}
