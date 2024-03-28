package com.acooly.showcase.daliy.Utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;


import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


@Slf4j
public class FTPUploader {
//    private static final String SERVER = "47.245.40.246";
    private static final String SERVER = "47.236.70.169";
    private static final int PORT = 21;
    private static final String USERNAME = "user";
    private static final String PASSWORD = "123";
    private static final String LOCAL_TEMP_FILE_PATH = "C:\\Users\\Administrator\\Desktop\\link\\index.html";

    public static String ftpUp(InputStream inputStream, String regname) {
        String remoteDirPath = "";
        String remoteFileName = "index.html";
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(SERVER, PORT);
            ftpClient.login(USERNAME, PASSWORD);
            ftpClient.enterLocalPassiveMode();
            int reply = ftpClient.getReplyCode();
            System.out.println(reply);
            log.info(String.valueOf(reply));
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            // 设置被动模式
            ftpClient.enterLocalPassiveMode();
            // 切换工作目录
            boolean b = ftpClient.changeWorkingDirectory(regname);
            // 上传文件
            try {
                boolean uploaded = ftpClient.storeFile(remoteFileName, inputStream);
                if (uploaded) {
                    remoteDirPath = "文件上传成功";
                    log.info("文件上传成功");
                } else {
                    remoteDirPath = "文件上传失败";
                    log.info("文件上传失败");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
//            boolean rename = ftpClient.rename("index.html", "newproject.top/1/index.html");
            log.info(String.valueOf(b));
            System.out.println(b);
            log.info(ftpClient.printWorkingDirectory());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 断开FTP连接
            try {
                ftpClient.logout();
                ftpClient.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return remoteDirPath;
    }


    public static String createFtp(String remoteDir) {
        FTPClient ftpClient = new FTPClient();
        String str = "";
        try {
            // 连接到FTP服务器
            ftpClient.connect(SERVER, PORT);
            ftpClient.login(USERNAME, PASSWORD);

            // 检查文件夹是否存在
            boolean folderExists = ftpClient.changeWorkingDirectory(remoteDir);
            if (!folderExists) {
                // 创建文件夹
                folderExists = ftpClient.makeDirectory(remoteDir);
                if (folderExists) {
                    log.info("文件夹创建成功： " + remoteDir);
                    str = "文件夹创建成功";
                } else {
                    str = "文件夹创建失败";
                    log.info("文件夹创建失败： " + remoteDir);
                }
            } else {
                str = "文件夹已存在";
                log.info("文件夹已存在： " + remoteDir);
            }
            // 退出并断开连接
            ftpClient.logout();
            ftpClient.disconnect();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return str;
    }

    public static String deleteFTp(String remoteDir) {
        String s = "";
        FTPClient ftpClient = new FTPClient();
        try {
            // 连接到FTP服务器
            ftpClient.connect(SERVER, PORT);
            ftpClient.login(USERNAME, PASSWORD);

            // 检查文件夹是否存在
            boolean folderExists = ftpClient.changeWorkingDirectory(remoteDir);
            if (folderExists) {
                log.info("文件夹存在，正在删除...");
                // 删除文件夹及其内容
                boolean b = ftpClient.removeDirectory(remoteDir);
                if (b) {
                    s = "文件夹已删除";
                } else {
                    s = "文件夹删除失败";
                }
            } else {
                s = "文件夹不存在";
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.logout();
                    ftpClient.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        log.info(s);
        return s;
    }


    public static String updateFTp(String remoteDir, String newRemoteDir) {
        String s = "";

        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(SERVER, PORT);
            ftpClient.login(USERNAME, PASSWORD);
            ftpClient.enterLocalPassiveMode();
            boolean isExist = ftpClient.changeWorkingDirectory(remoteDir);
            if (isExist) {
                boolean isSuccess = ftpClient.rename(remoteDir, newRemoteDir);
                if (isSuccess) {
                    s = "文件夹名称修改成功";
                } else {
                    s = "文件夹名称修改失败";
                }
            } else {
                s = "文件夹不存在";
            }
            log.info(s);
            ftpClient.logout();
            ftpClient.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }

    public static String formatPhoneNumber(String phoneNumber) {
        ;
        char[][] operations = {{'+', 0}, {'(', 2}, {')', 6}, {'-', 10}};
        StringBuilder sb = new StringBuilder(phoneNumber);
        for (char[] operation : operations) {
            sb.insert(operation[1], operation[0]);
        }
        return sb.toString();
    }
    //不需要本地文件
    public static boolean updateLinkNotLocal(String oldLink, String newLink, String path) {
        long start = System.currentTimeMillis();
        String pathFile = path + "/index.html";
        boolean uploaded = false;
        try {
            FTPClient ftpClient = new FTPClient();
            ftpClient.connect(SERVER, PORT);
            ftpClient.login(USERNAME, PASSWORD);
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            List<String> lines1 = new ArrayList<>();
            //ftp 读取当前目录的文件 形成reader流
            BufferedReader reader1 = new BufferedReader(new InputStreamReader(ftpClient.retrieveFileStream(pathFile), StandardCharsets.UTF_8));
            String line1;
            while ((line1 = reader1.readLine()) != null) {
                if (line1.trim().contains(oldLink)) {
                    line1 = line1.replace(oldLink, newLink);
                    lines1.add(line1);
                } else {
                    lines1.add(line1);
                }
            }
            reader1.close();
            ftpClient.logout();
            ftpClient.disconnect();



            FTPClient ftpClient1 = new FTPClient();
            ftpClient1.connect(SERVER, PORT);
            ftpClient1.login(USERNAME, PASSWORD);
            ftpClient1.enterLocalPassiveMode();
            ftpClient1.setFileType(FTP.BINARY_FILE_TYPE);

            ftpClient1.changeWorkingDirectory(path);
            // 上传文件
            try (InputStream inputStream = convertListToInputStream(lines1)) {
                uploaded = ftpClient1.storeFile("index.html", inputStream);
                if (uploaded) {
                    System.out.println("文件上传成功");
                } else {
                    log.info("落地页链接上传失败");
                }
            }
            ftpClient1.logout();
            ftpClient1.disconnect();



        } catch (IOException e) {
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();
        System.out.println("消耗时间为：" + (end - start));
        log.info("不需要本地文件消耗时间为：" + (end - start));
        return uploaded;
    }


    //需要本地文件 耗时较大
    public static boolean updateLink(String oldLink, String newLink, String path) {
        long start = System.currentTimeMillis();
        boolean uploaded = false;
        String pathFile = path + "/index.html";
        try {
            FTPClient ftpClient = new FTPClient();
            ftpClient.connect(SERVER, PORT);
            ftpClient.login(USERNAME, PASSWORD);
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            BufferedReader reader1 = new BufferedReader(new InputStreamReader(ftpClient.retrieveFileStream(pathFile), StandardCharsets.UTF_8));
            FileOutputStream fos = new FileOutputStream(LOCAL_TEMP_FILE_PATH);
            String line1;
            while ((line1 = reader1.readLine()) != null) {
                fos.write((line1 + System.lineSeparator()).getBytes(StandardCharsets.UTF_8));
            }
            fos.close();
            reader1.close();
            ftpClient.logout();
            ftpClient.disconnect();
            FTPClient ftpClient1 = new FTPClient();
            ftpClient1.connect(SERVER, PORT);
            ftpClient1.login(USERNAME, PASSWORD);
            ftpClient1.enterLocalPassiveMode();
            ftpClient1.setFileType(FTP.BINARY_FILE_TYPE);
            // Modify the specified line in the local temp file
            List<String> lines = new ArrayList<>();
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(LOCAL_TEMP_FILE_PATH), StandardCharsets.UTF_8));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().contains(oldLink)) {
                    line = line.replace(oldLink, newLink);
                    lines.add(line);
                } else {
                    lines.add(line);
                }
            }
            reader.close();
//             切换工作目录
            ftpClient1.changeWorkingDirectory(path);
            // 上传文件
            try (InputStream inputStream = convertListToInputStream(lines)) {
                uploaded = ftpClient1.storeFile("index.html", inputStream);
                if (uploaded) {
                    System.out.println("文件上传成功");
                } else {
                    log.info("落地页链接上传失败");
                }
            }
            ftpClient1.logout();
            ftpClient1.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();
        System.out.println("消耗时间为：" + (end - start));
        log.info("消耗时间为：" + (end - start));
        File file = new File(LOCAL_TEMP_FILE_PATH);
        file.delete();
        return uploaded;
    }

    public static InputStream convertListToInputStream(List<String> lines) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String line : lines) {
            stringBuilder.append(line).append("\n");
        }
        return new ByteArrayInputStream(stringBuilder.toString().getBytes(StandardCharsets.UTF_8));
    }


    public static void main(String[] args) {
        String server = "47.245.40.246";
        int port = 21;
        String user = "user";
        String password = "123";
        String remoteDirPath = "C:/wwwroot/127.0.0.1";
        String localFilePath = "C:\\Users\\Administrator\\Desktop\\index.html";

        String remoteFileName = "index.html";

        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(server, port);
            ftpClient.login(user, password);
            ftpClient.enterLocalPassiveMode();
            int reply = ftpClient.getReplyCode();
            System.out.println(reply);
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            // 设置被动模式
            ftpClient.enterLocalPassiveMode();

            // 切换工作目录
            boolean b = ftpClient.changeWorkingDirectory("newproject.top/gua2");

            // 上传文件
            try (InputStream inputStream = new FileInputStream(localFilePath)) {
                boolean uploaded = ftpClient.storeFile(remoteFileName, inputStream);
                if (uploaded) {
                    System.out.println("文件上传成功");
                } else {
                    System.out.println("文件上传失败");
                }
            }
//            boolean rename = ftpClient.rename("index.html", "newproject.top/1/index.html");
            System.out.println(b);
//            System.out.println(rename);
            System.out.println(ftpClient.printWorkingDirectory());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 断开FTP连接
            try {
                ftpClient.logout();
                ftpClient.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
