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
    public static boolean createDirectory(String directoryPath) {
        try {
            executeCommand("mkdir -p " + directoryPath);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 清空
    public static boolean clearDirectory(String directoryPath) {
        try {
            executeCommand("rm -rf " + directoryPath + "/*");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 检查文件夹是否存在
    public static boolean directoryExists(String directoryPath) {
        try {
            String command = "[ -d \"" + directoryPath + "\" ] && echo \"Directory exists\" || echo \"Directory does not exist\"";
             executeCommand(command);
             return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //复制
    public static boolean copyFiles(String sourceDirectoryPath, String destinationDirectoryPath) {
        try {
            executeCommand("cp -r " + sourceDirectoryPath + "/* " + destinationDirectoryPath);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //删除 文件夹
    public static boolean deleteDirectory(String directoryPath) {
        try {
            executeCommand("rm -rf " + directoryPath);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 修改文件夹名称
    public static boolean renameDirectory(String oldDirectoryPath, String newDirectoryName) {
        try {
            executeCommand("mv " + oldDirectoryPath + " " + newDirectoryName);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static void executeCommand(String command) throws Exception {
        JSch jsch = new JSch();
        Session session = jsch.getSession(USERNAME, HOST, 22);
        session.setPassword(PASSWORD);
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
     * 向远程服务器 HTML 文件注入 Google Ads gtag 像素脚本
     *
     * @param directoryPath 目标目录路径 (如 /www/wwwroot/domain/secondaryDomain)
     * @param awId          Google Ads AW ID (如 AW-17637822321)
     * @param conversionId  Google Ads Conversion ID (如 JT3WCMXP46YcEPGer9pB)
     * @return 是否注入成功
     */
    public static boolean injectGooglePixelScript(String directoryPath, String awId, String conversionId) {
        try {
            String htmlPath = directoryPath + "/index.html";

            // Step 1: 先移除 HTML 中已存在的 Google gtag 脚本（避免重复注入）
            executeCommand("sed -i '/googletagmanager\\/gtag\\/js/d' " + htmlPath);
            executeCommand("sed -i '/function gtag_report_conversion/,/<\\/script>/d' " + htmlPath);

            // Step 2: 在 </head> 之前插入 gtag 脚本
            // 使用 sed 的 i 命令逐行插入，避免复杂的转义
            executeCommand(
                "sed -i 's|</head>|" +
                "<script async src=\"https://www.googletagmanager.com/gtag/js?id=" + awId + "\"></script>\\n" +
                "</head>|' " + htmlPath
            );
            executeCommand(
                "sed -i 's|</head>|" +
                "<script>window.dataLayer=window.dataLayer||[];function gtag(){dataLayer.push(arguments);}" +
                "gtag(\"js\",new Date());gtag(\"config\",\"" + awId + "\");</script>\\n" +
                "</head>|' " + htmlPath
            );
            executeCommand(
                "sed -i 's|</head>|" +
                "<script>function gtag_report_conversion(url){var callback=function(){" +
                "if(typeof url!=\"undefined\"){window.location=url;}};if(typeof window.gtag===\"function\"){" +
                "gtag(\"event\",\"conversion\",{send_to:\"" + awId + "/" + conversionId + "\"," +
                "event_callback:callback});}return false;}</script>\\n" +
                "</head>|' " + htmlPath
            );

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) {
        copyFiles("/www/wwwroot/projectgame.top/in01","/www/wwwroot/jdxcn.top/1");
    }
}
