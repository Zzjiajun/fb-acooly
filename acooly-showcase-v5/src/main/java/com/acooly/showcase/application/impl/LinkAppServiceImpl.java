package com.acooly.showcase.application.impl;

import com.acooly.core.common.web.AbstractJsonEntityController;
import com.acooly.core.common.web.support.JsonEntityResult;
import com.acooly.core.common.web.support.JsonResult;
import com.acooly.module.security.domain.User;
import com.acooly.showcase.application.LinkAppService;
import com.acooly.showcase.daliy.Utils.RedisUtils;
import com.acooly.showcase.daliy.entity.Link;
import com.acooly.showcase.daliy.service.LinkService;
import com.acooly.showcase.infrastructure.lock.DistributedLockService;
import com.acooly.showcase.infrastructure.remote.RemoteCommandGateway;
import com.acooly.showcase.link.entity.DmCondition;
import com.acooly.showcase.link.entity.DmServer;
import com.acooly.showcase.link.service.DmConditionService;
import com.acooly.showcase.link.service.DmCountryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.acooly.core.common.exception.BusinessException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.google.common.collect.Maps;

@Slf4j
@Service
@RequiredArgsConstructor
public class LinkAppServiceImpl implements LinkAppService {

    private final LinkService linkService;
    private final DmConditionService dmConditionService;
    private final DmCountryService dmCountryService;
    private final RedisUtils redisUtils;
    private final RemoteCommandGateway remoteGateway;
    private final DistributedLockService lockService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Link createLinkWithRemoteDir(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Link entity = loadOrBuildLink(request);
        String str = entity.getRegionName()+"/"+entity.getDomain();
        String lockKey = "lock:link:" + str;
        String lockVal = UUID.randomUUID().toString();
        if (!lockService.tryLock(lockKey, lockVal, 30)){
            throw new BusinessException("RESOURCE_BUSY", "同一路径正在操作，请稍后重试");
        }
        try {
            DmServer dmServer = redisUtils.getDmServer();
            if (!remoteGateway.createDirectory(entity.getRegionName(), entity.getDomain(), dmServer)) {
                throw new BusinessException("REMOTE_DIR_CREATE_FAIL", "新建二级域名目录失败");
            }
           linkService.save(entity);
            createOrUpdateConditionOnCreate(entity);
            dmCountryService.dmConditionRedis();
            return entity;
        } finally {
            lockService.unlock(lockKey, lockVal);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Link updateLinkAndRenameRemoteDir(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String id = request.getParameter("id");
        Link oldLink = linkService.get(Long.parseLong(id));
        Link entity = loadOrBuildLink(request);
        String oldStr = oldLink.getRegionName()+"/"+oldLink.getDomain();
        String newStr = entity.getRegionName()+"/"+entity.getDomain();
        String lockKey = "lock:link:" + newStr;
        String lockVal = UUID.randomUUID().toString();
        if (!lockService.tryLock(lockKey, lockVal, 30)){
            throw new BusinessException("RESOURCE_BUSY", "同一路径正在操作，请稍后重试");
        }
        try {
            DmServer dmServer = redisUtils.getDmServer();
            if (!remoteGateway.renameDirectory(oldLink.getRegionName(), oldLink.getDomain(), entity.getRegionName(), entity.getDomain(), dmServer)) {
                throw new BusinessException("REMOTE_DIR_RENAME_FAIL", "修改二级域名目录失败");
            }
             linkService.update(entity);
            syncConditionOnUpdate(oldLink, entity);
            dmCountryService.dmConditionRedis();
            return entity;
        } finally {
            lockService.unlock(lockKey, lockVal);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteLinksAndRemoteDirs(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String[] idParams = request.getParameterValues("ids");
        if (idParams == null) return;
        for (String idStr : idParams){
            Long id = Long.valueOf(idStr);
            Link link = linkService.get(id);
            String str = link.getRegionName()+"/"+link.getDomain();
            String lockKey = "lock:link:" + str;
            String lockVal = UUID.randomUUID().toString();
            if (!lockService.tryLock(lockKey, lockVal, 30)){
                throw new BusinessException("RESOURCE_BUSY", "同一路径正在操作，请稍后重试");
            }
            try {
                DmServer dmServer = redisUtils.getDmServer();
                if (!remoteGateway.deleteDirectory(link.getRegionName(), link.getDomain(), dmServer)){
                    throw new BusinessException("REMOTE_DIR_DELETE_FAIL", "删除二级域名目录失败");
                }
                removeConditionIfExists(link);
                linkService.removeById(id);
            } finally {
                lockService.unlock(lockKey, lockVal);
            }
        }
        dmCountryService.dmConditionRedis();
    }

    private Link loadOrBuildLink(HttpServletRequest request){
        String id = request.getParameter("id");
        Link entity = (id != null) ? linkService.get(Long.parseLong(id)) : new Link();
        entity.setDomain(request.getParameter("domain"));
        entity.setRegionName(request.getParameter("regionName"));
        User principal = (User) SecurityUtils.getSubject().getPrincipal();
        if (id == null){
            entity.setHolder(principal.getUsername());
            entity.setUserId(Math.toIntExact(principal.getId()));
        }
        entity.setAccessAddress(entity.getRegionName()+"/"+entity.getDomain());
        return entity;
    }

    private void createOrUpdateConditionOnCreate(Link saved){
        User principal = (User) SecurityUtils.getSubject().getPrincipal();
        DmCondition dmCondition = new DmCondition();
        dmCondition.setUserName(principal.getUsername());
        dmCondition.setIsIp(0);
        dmCondition.setIsVpn(0);
        dmCondition.setIsFbclid(0);
        dmCondition.setIsChinese(0);
        dmCondition.setIsMobile(0);
        dmCondition.setTimeZone(0);
        dmCondition.setIsSpecificDevice(0);
        dmCondition.setIpLimits(0);
        dmCondition.setIpWhite(0);
        dmCondition.setIsRobot(0);
        dmCondition.setIsIdentify(0);
        dmCondition.setIsBusiness(0);
        dmCondition.setIsVirtual(0);
        dmCondition.setIosVersion("0.0.0");
        dmCondition.setAndVersion("0.0.0");
        dmCondition.setAccessAddress(saved.getAccessAddress());
        dmConditionService.save(dmCondition);
    }

    private void syncConditionOnUpdate(Link oldLink, Link entity){
        if (!oldLink.getAccessAddress().equals(entity.getAccessAddress())){
            Map<String, Object> mapQuery = Maps.newHashMap();
            mapQuery.put("EQ_accessAddress", oldLink.getAccessAddress());
            List<DmCondition> query = dmConditionService.query(mapQuery, null);
            if (query.size() > 0){
                DmCondition dmCondition = query.get(0);
                dmCondition.setAccessAddress(entity.getAccessAddress());
                dmConditionService.update(dmCondition);
            }
        }
    }

    private void removeConditionIfExists(Link link){
        Map<String, Object> mapQuery = Maps.newHashMap();
        mapQuery.put("EQ_accessAddress", link.getAccessAddress());
        List<DmCondition> query = dmConditionService.query(mapQuery, null);
        if (query.size() > 0) {
            dmConditionService.removeById(query.get(0).getId());
        }
    }
} 