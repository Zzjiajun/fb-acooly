package com.acooly.showcase.infrastructure.remote;

import com.acooly.showcase.daliy.Utils.RemoteFileOperationsUtil;
import com.acooly.showcase.link.entity.DmServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.acooly.core.common.exception.BusinessException;

import java.util.regex.Pattern;

@Slf4j
@Service
public class RemoteCommandGateway {

    private static final String ROOT = "/www/wwwroot/";
    private static final Pattern NAME_SAFE = Pattern.compile("^[a-zA-Z0-9._-]{1,128}$");

    public boolean createDirectory(String regionName, String domain, DmServer server){
        String path = buildSafePath(regionName, domain);
        return RemoteFileOperationsUtil.createDirectory(path, server.getUsername(), server.getPassword(), server.getIp());
    }

    public boolean renameDirectory(String oldRegion, String oldDomain, String newRegion, String newDomain, DmServer server){
        String oldPath = buildSafePath(oldRegion, oldDomain);
        String newPath = buildSafePath(newRegion, newDomain);
        return RemoteFileOperationsUtil.renameDirectory(oldPath, newPath, server.getUsername(), server.getPassword(), server.getIp());
    }

    public boolean deleteDirectory(String regionName, String domain, DmServer server){
        String path = buildSafePath(regionName, domain);
        return RemoteFileOperationsUtil.deleteDirectory(path, server.getUsername(), server.getPassword(), server.getIp());
    }

    private String buildSafePath(String regionName, String domain){
        validateName(regionName);
        validateName(domain);
        String path = ROOT + regionName + "/" + domain;
        validatePath(path);
        return path;
    }

    private void validateName(String name){
        if (name == null || !NAME_SAFE.matcher(name).matches()){
            throw new BusinessException("INVALID_ARGUMENT", "非法名称: " + name);
        }
        if (name.contains("..") || name.contains("/")){
            throw new BusinessException("INVALID_ARGUMENT", "非法名称路径片段: " + name);
        }
    }

    private void validatePath(String path){
        if (path == null || !path.startsWith(ROOT)){
            throw new BusinessException("INVALID_ARGUMENT", "非法路径: " + path);
        }
        if (path.contains("..")){
            throw new BusinessException("INVALID_ARGUMENT", "检测到路径回退: " + path);
        }
    }
} 