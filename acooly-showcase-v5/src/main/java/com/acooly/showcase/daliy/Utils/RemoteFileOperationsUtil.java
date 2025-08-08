package com.acooly.showcase.daliy.Utils;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class RemoteFileOperationsUtil {

    private static final String HOST = "47.236.172.187";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "123456Qwe";

    //创建
    public static boolean createDirectory(String directoryPath,String username,String password,String ip) {
        try {
            executeCommand("mkdir -p " + directoryPath,username,password,ip);
//            executeCommand("mkdir -p " + directoryPath+"/01",username,password,ip);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 清空
    public static boolean clearDirectory(String directoryPath,String username,String password,String ip) {
        try {
            executeCommand("rm -rf " + directoryPath + "/*",username,password,ip);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 检查文件夹是否存在
    public static boolean directoryExists(String directoryPath,String username,String password,String ip) {
        try {
            String command = "[ -d \"" + directoryPath + "\" ] && echo \"Directory exists\" || echo \"Directory does not exist\"";
             executeCommand(command,username,password,ip);
             return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //复制
    public static boolean copyFiles(String sourceDirectoryPath, String destinationDirectoryPath,String username,String password,String ip) {
        try {
            executeCommand("cp -r " + sourceDirectoryPath + "/* " + destinationDirectoryPath,username,password,ip);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //删除 文件夹
    public static boolean deleteDirectory(String directoryPath,String username,String password,String ip) {
        try {
            executeCommand("rm -rf " + directoryPath,username,password,ip);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 修改文件夹名称
    public static boolean renameDirectory(String oldDirectoryPath,String newDirectoryName, String username,String password,String ip) {
        try {
            executeCommand("mv " + oldDirectoryPath + " " + newDirectoryName,username,password,ip);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void executeCommand(String command,String username,String password,String ip) throws Exception {
        JSch jsch = new JSch();
        Session session = jsch.getSession(username, ip, 22);
        session.setPassword(password);
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect();

        ChannelExec channelExec = (ChannelExec) session.openChannel("exec");
        channelExec.setCommand(command);

        InputStream in = channelExec.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        channelExec.connect();

        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }

        channelExec.disconnect();
        session.disconnect();
    }

    /**
     * 执行命令并返回结果
     * @param command 要执行的命令
     * @param username 用户名
     * @param password 密码
     * @param ip 服务器IP
     * @return 命令执行结果
     * @throws Exception 执行异常
     */
    public static String executeCommandWithResult(String command, String username, String password, String ip) throws Exception {
        JSch jsch = new JSch();
        Session session = jsch.getSession(username, ip, 22);
        session.setPassword(password);
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect();

        ChannelExec channelExec = (ChannelExec) session.openChannel("exec");
        channelExec.setCommand(command);

        // 获取标准输出
        InputStream in = channelExec.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        
        // 获取错误输出
        InputStream err = channelExec.getErrStream();
        BufferedReader errReader = new BufferedReader(new InputStreamReader(err));
        
        channelExec.connect();

        StringBuilder output = new StringBuilder();
        String line;
        
        // 读取标准输出
        while ((line = reader.readLine()) != null) {
            output.append(line).append("\n");
        }
        
        // 读取错误输出
        while ((line = errReader.readLine()) != null) {
            output.append("ERROR: ").append(line).append("\n");
        }
        
        // 等待命令执行完成
        while (!channelExec.isClosed()) {
            Thread.sleep(100);
        }
        
        int exitStatus = channelExec.getExitStatus();
        channelExec.disconnect();
        session.disconnect();
        
        // 如果退出码不为0，抛出异常
        if (exitStatus != 0) {
            throw new RuntimeException("命令执行失败，退出码: " + exitStatus + ", 命令: " + command + ", 输出: " + output.toString());
        }
        
        return output.toString();
    }
}
