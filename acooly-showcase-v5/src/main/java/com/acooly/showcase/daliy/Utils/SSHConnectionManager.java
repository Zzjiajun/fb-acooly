package com.acooly.showcase.daliy.Utils;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * SSH 连接管理器 - 复用连接执行多个命令
 */
public class SSHConnectionManager {
    private JSch jsch;
    private Session session;
    private String username;
    private String password;
    private String ip;
    private boolean isConnected = false;
    
    public SSHConnectionManager(String username, String password, String ip) {
        this.username = username;
        this.password = password;
        this.ip = ip;
        this.jsch = new JSch();
    }
    
    /**
     * 建立 SSH 连接
     */
    public void connect() throws Exception {
        if (isConnected) {
            return; // 已经连接，直接返回
        }
        
        session = jsch.getSession(username, ip, 22);
        session.setPassword(password);
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect();
        isConnected = true;
    }
    
    /**
     * 执行命令并返回结果
     */
    public String executeCommand(String command) throws Exception {
        if (!isConnected) {
            throw new RuntimeException("SSH 连接未建立");
        }
        
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
        
        // 如果退出码不为0，抛出异常
        if (exitStatus != 0) {
            throw new RuntimeException("命令执行失败，退出码: " + exitStatus + ", 命令: " + command + ", 输出: " + output.toString());
        }
        
        return output.toString();
    }
    
    /**
     * 执行命令不检查退出码（用于某些特殊命令）
     */
    public String executeCommandIgnoreExitCode(String command) throws Exception {
        if (!isConnected) {
            throw new RuntimeException("SSH 连接未建立");
        }
        
        ChannelExec channelExec = (ChannelExec) session.openChannel("exec");
        channelExec.setCommand(command);
        
        InputStream in = channelExec.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        
        InputStream err = channelExec.getErrStream();
        BufferedReader errReader = new BufferedReader(new InputStreamReader(err));
        
        channelExec.connect();
        
        StringBuilder output = new StringBuilder();
        String line;
        
        while ((line = reader.readLine()) != null) {
            output.append(line).append("\n");
        }
        
        while ((line = errReader.readLine()) != null) {
            output.append("ERROR: ").append(line).append("\n");
        }
        
        while (!channelExec.isClosed()) {
            Thread.sleep(100);
        }
        
        channelExec.disconnect();
        return output.toString();
    }
    
    /**
     * 关闭 SSH 连接
     */
    public void disconnect() {
        if (session != null && session.isConnected()) {
            session.disconnect();
        }
        isConnected = false;
    }
    
    /**
     * 检查连接状态
     */
    public boolean isConnected() {
        return isConnected && session != null && session.isConnected();
    }
} 