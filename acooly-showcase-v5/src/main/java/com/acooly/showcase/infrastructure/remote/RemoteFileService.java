package com.acooly.showcase.infrastructure.remote;

import com.acooly.showcase.daliy.Utils.RemoteFileOperationsUtil;
import com.acooly.showcase.daliy.Utils.SSHConnectionManager;
import com.acooly.showcase.daliy.entity.Link;
import com.acooly.showcase.daliy.service.LinkService;
import com.acooly.showcase.link.entity.DmServer;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.acooly.core.common.exception.BusinessException;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class RemoteFileService {

    private static final String ROOT = "/www/wwwroot/";

    private final LinkService linkService;

    public void copyTemplateTo(String sourceDir, String destDir, DmServer server){
        boolean ok = RemoteFileOperationsUtil.copyFiles(sourceDir, destDir, server.getUsername(), server.getPassword(), server.getIp());
        if (!ok){
            throw new BusinessException("REMOTE_COPY_TEMPLATE_FAIL", "模版复制失败: " + sourceDir + " -> " + destDir);
        }
    }

    /**
     * 安全清空目录：保留以当前域名为前缀的子目录（依据数据库 Link.accessAddress 推断）
     */
    public void safeClearDirectoryPreserveSubDirs(String clearStr, DmServer server){
        Map<String, String> backupPaths = new HashMap<>();
        SSHConnectionManager ssh = null;
        try {
            ssh = new SSHConnectionManager(server.getUsername(), server.getPassword(), server.getIp());
            ssh.connect();

            List<String> subDirs = getSubDirectoriesFromDb(clearStr);
            if (subDirs.isEmpty()){
                ssh.executeCommand("rm -rf " + clearStr + "/*");
                return;
            }

            backupPaths = backupSubDirectoriesWithConnection(subDirs, ssh);

            String clearMainDirCommand = String.format(
                    "find %s -maxdepth 1 -type f -delete && find %s -maxdepth 1 -type d ! -name '.' ! -name '..' | grep -v '^%s$' | xargs -I {} rm -rf {}",
                    clearStr, clearStr, clearStr
            );
            ssh.executeCommand(clearMainDirCommand);

            restoreSubDirectoriesWithConnection(backupPaths, ssh);
        } catch (Exception e){
            log.error("安全清空目录失败 clearStr={}", clearStr, e);
            throw new BusinessException("SAFE_CLEAR_DIR_FAIL", "安全清空目录失败: " + e.getMessage());
        } finally {
            if (ssh != null){
                ssh.disconnect();
            }
        }
    }

    private List<String> getSubDirectoriesFromDb(String clearStr){
        try {
            String relativePath = clearStr.replace(ROOT, "");
            Map<String, Object> params = Maps.newHashMap();
            params.put("LIKE_accessAddress", relativePath + "/%");
            List<Link> subLinks = linkService.query(params, null);
            List<String> subDirectories = new ArrayList<>();
            for (Link link : subLinks){
                subDirectories.add(ROOT + link.getAccessAddress());
            }
            return subDirectories;
        }catch (Exception e){
            log.warn("查询子目录失败，将采用直接清空策略: {}", clearStr, e);
            return Collections.emptyList();
        }
    }

    private Map<String, String> backupSubDirectoriesWithConnection(List<String> subDirectories, SSHConnectionManager ssh) throws Exception {
        Map<String, String> backupPaths = new HashMap<>();
        for (String subDir : subDirectories) {
            String backupPath = generateBackupPath(subDir);
            ssh.executeCommand("mkdir -p " + backupPath);
            ssh.executeCommand("cp -r " + subDir + "/* " + backupPath + "/");
            backupPaths.put(subDir, backupPath);
        }
        return backupPaths;
    }

    private void restoreSubDirectoriesWithConnection(Map<String, String> backupPaths, SSHConnectionManager ssh) throws Exception {
        for (Map.Entry<String, String> entry : backupPaths.entrySet()) {
            String originalPath = entry.getKey();
            String backupPath = entry.getValue();
            ssh.executeCommand("mkdir -p " + originalPath);
            ssh.executeCommand("cp -r " + backupPath + "/* " + originalPath + "/");
        }
    }

    private String generateBackupPath(String originalPath){
        String timestamp = String.valueOf(System.currentTimeMillis());
        String randomStr = UUID.randomUUID().toString().substring(0, 8);
        String safePath = originalPath.replace("/", "_").replace(" ", "_");
        return ROOT + "acooly/" + timestamp + "_" + randomStr + "/" + safePath;
    }
} 