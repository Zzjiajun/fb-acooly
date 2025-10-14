package com.acooly.showcase.application.impl;

import com.acooly.module.event.EventBus;
import com.acooly.module.security.domain.User;
import com.acooly.showcase.application.DmCenterAppService;
import com.acooly.showcase.daliy.Utils.EncryptionUtil;
import com.acooly.showcase.daliy.Utils.RedisUtils;
import com.acooly.showcase.daliy.entity.*;
import com.acooly.showcase.daliy.service.*;
import com.acooly.showcase.event.CreateCustomerEvent;
import com.acooly.showcase.infrastructure.lock.DistributedLockService;
import com.acooly.showcase.infrastructure.remote.RemoteFileService;
import com.acooly.showcase.link.entity.*;
import com.acooly.showcase.link.service.*;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.acooly.core.common.exception.BusinessException;

import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class DmCenterAppServiceImpl implements DmCenterAppService {

    private final DmCenterService dmCenterService;
    private final LinkService linkService;
    private final DmPixelService dmPixelService;
    private final DmDomainService dmDomainService;
    private final LinkIntService linkIntService;
    private final LinkSrcsService linkSrcsService;
    private final EventBus eventBus;
    private final DmServerService dmServerService;
    private final DmStencilService dmStencilService;
    private final RedisUtils redisUtil;
    private final DmAccessService dmAccessService;
    private final DmClickService dmClickService;
    private final DmCountryService dmCountryService;
    private final DmConditionService dmConditionService;
    private final DmTrollsService dmTrollsService;
    private final Gson gson;
    private final DistributedLockService lockService;
    private final RemoteFileService remoteFileService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DmCenter saveCenterWithRemoteAndEvents(HttpServletRequest request, HttpServletResponse response, DmCenter entity, boolean isCreate) throws Exception {
        List<String> list = Arrays.asList(entity.getPixel().split("\n"));
        User principal = (User) SecurityUtils.getSubject().getPrincipal();
        String str=entity.getDomain() + "/" + entity.getSecondaryDomain();
        String lockKey = "lock:dmcenter:" + str;
        String lockVal = UUID.randomUUID().toString();
        if (!lockService.tryLock(lockKey, lockVal, 60)) {
            throw new BusinessException("RESOURCE_BUSY", "同一域名正在处理，请稍后重试");
        }
        try {
            String clearStr = "/www/wwwroot/"+str;
            String filePath = "/www/wwwroot/"+str+"/index.html";

            String builtKey = redisUtil.buildKey("acooly","pathNameCil");
            String builtKey1 = redisUtil.buildKey("acooly","pathNameCil1");
            String builtProtectKey = redisUtil.buildKey("acooly","protectCil");
            String builtProtectKey1 = redisUtil.buildKey("acooly","protectCil1");
            String builtKeyIp = redisUtil.buildKey("acooly","Ip");

            Map<String, Object> mapCondition = Maps.newHashMap();
            mapCondition.put("EQ_accessAddress",str);
            DmCondition dmCondition = dmConditionService.query(mapCondition, null).get(0);
            entity.setUserName(dmCondition.getUserName());
            entity.setConditionId(dmCondition.getId());

            String pathNameCil;
            String pathNameCil1;
            if (entity.getProtect() == 0) {
                pathNameCil = redisString(builtKey, 1L);
                pathNameCil1 = redisString(builtKey1, 2L);
            } else {
                pathNameCil = redisString(builtProtectKey, 3L);
                pathNameCil1 = redisString(builtProtectKey1, 4L);
            }

            DmServer dmServer;
            if (!redisUtil.exist(builtKeyIp)){
                dmServer = dmServerService.get(1l);
                LinkedList<DmServer> dmServers = new LinkedList<>();
                dmServers.add(dmServer);
                redisUtil.set(builtKeyIp,gson.toJson(dmServers));
            }else {
                String string = redisUtil.get(builtKeyIp);
                List<DmServer> dmServers = gson.fromJson(string, new TypeToken<List<DmServer>>(){}.getType());
                dmServer=dmServers.get(0);
            }

            if(isCreate){
                String encodedKey="";
                entity.setTrolls(0);
                entity.setClicksNumber(0);
                entity.setVisitsNumber(0);
                if (entity.getProtect()==0){
                    SecretKey secretKey = EncryptionUtil.generateKey();
                    String encrypt = EncryptionUtil.encrypt(entity.getLink(), secretKey);
                    entity.setLink(encrypt);
                    encodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
                    entity.setKeyy(encodedKey);
                }else {
                    SecretKey secretKey = EncryptionUtil.generateKey();
                    encodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
                    entity.setKeyy(encodedKey);
                }
                if (entity.getDiversion() == 1) {
                    remoteFileService.safeClearDirectoryPreserveSubDirs(clearStr, dmServer);
                    remoteFileService.copyTemplateTo("/www/wwwroot/" + (entity.getSerialNumber() + "D"), "/www/wwwroot/" + str, dmServer);
                    upsertLinkIntAndSrcs(principal, str, entity);
                }else {
                    remoteFileService.safeClearDirectoryPreserveSubDirs(clearStr, dmServer);
                    remoteFileService.copyTemplateTo("/www/wwwroot/" + pathNameCil, "/www/wwwroot/" + str, dmServer);
                    eventTwoChooseCreate(dmServer,filePath,entity,dmCondition);
                }
                if (entity.getDisplayOption()==1) {
                    savePixels(principal, str, list);
                }
            }else {
                DmCenter dmCenterOld = dmCenterService.get(entity.getId());
                String oldStr = dmCenterOld.getDomain() + "/" + dmCenterOld.getSecondaryDomain();
                if (entity.getProtect()==0) {
                    SecretKey secretKey = EncryptionUtil.generateKey();
                    String encrypt = EncryptionUtil.encrypt(entity.getLink(), secretKey);
                    String encodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
                    entity.setKeyy(encodedKey);
                    entity.setLink(encrypt);
                }
                if (!entity.getPixel().equals(dmCenterOld.getPixel()) ||!oldStr.equals(str)){
                    Map<String, Object> map2 = Maps.newHashMap();
                    map2.put("EQ_domain",entity.getDomain() + "/" + entity.getSecondaryDomain());
                    List<DmPixel> dmPixels = dmPixelService.query(map2, null);
                    if (!dmPixels.isEmpty()){
                        dmPixels.forEach(s-> dmPixelService.removeById(s.getId()));
                    }
                    savePixels(principal, str, list);
                }
                if (entity.getDiversion() == 1 && !entity.getDiversion().equals(dmCenterOld.getDiversion())) {
                    remoteFileService.safeClearDirectoryPreserveSubDirs(clearStr, dmServer);
                    remoteFileService.copyTemplateTo("/www/wwwroot/" + (entity.getSerialNumber() + "D"), "/www/wwwroot/" + str, dmServer);
                    upsertLinkIntAndSrcs(principal, str, entity);
                }
                if (entity.getDiversion() == 0 && !entity.getDiversion().equals(dmCenterOld.getDiversion())){
                    remoteFileService.safeClearDirectoryPreserveSubDirs(clearStr, dmServer);
                    remoteFileService.copyTemplateTo("/www/wwwroot/" + pathNameCil, "/www/wwwroot/" + str, dmServer);
                    eventTwoChooseCreate(dmServer,filePath,entity,dmCondition);
                }
                if (entity.getDiversion().equals(dmCenterOld.getDiversion())) {
                    if (entity.getDisplayOption().equals(dmCenterOld.getDisplayOption())){
                        if (entity.getDiversion() == 1){
                            remoteFileService.safeClearDirectoryPreserveSubDirs(clearStr, dmServer);
                            remoteFileService.copyTemplateTo("/www/wwwroot/" + (entity.getSerialNumber() + "D"), "/www/wwwroot/" + str, dmServer);
                            upsertLinkIntAndSrcs(principal, str, entity);
                        }else {
                            remoteFileService.safeClearDirectoryPreserveSubDirs(clearStr, dmServer);
                            remoteFileService.copyTemplateTo("/www/wwwroot/" + pathNameCil, "/www/wwwroot/" + str, dmServer);
                            eventTwoChooseCreate(dmServer,filePath,entity,dmCondition);
                        }
                    }else {
                        if (entity.getDiversion() != 1){
                            remoteFileService.safeClearDirectoryPreserveSubDirs(clearStr, dmServer);
                            remoteFileService.copyTemplateTo("/www/wwwroot/" + pathNameCil, "/www/wwwroot/" + str, dmServer);
                            eventTwoChooseCreate(dmServer,filePath,entity,dmCondition);
                        }
                    }
                }
            }
            dmCenterService.save(entity);
            return entity;
        } finally {
            lockService.unlock(lockKey, lockVal);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void safeRemoveCenter(HttpServletRequest request, HttpServletResponse response, Long id) throws Exception {
        DmCenter dmCenter = dmCenterService.get(id);
        String str=dmCenter.getDomain() + "/" + dmCenter.getSecondaryDomain();
        String lockKey = "lock:dmcenter:" + str;
        String lockVal = UUID.randomUUID().toString();
        if (!lockService.tryLock(lockKey, lockVal, 60)) {
            throw new BusinessException("RESOURCE_BUSY", "同一域名正在处理，请稍后重试");
        }
        try {
            DmServer dmServer = redisUtil.getDmServer();
            remoteFileService.safeClearDirectoryPreserveSubDirs("/www/wwwroot/" + str, dmServer);
            Map<String, Object> map1 = Maps.newHashMap();
            map1.put("EQ_domain",str);
            List<LinkInt> linkIntList = linkIntService.query(map1, null);
            if (!linkIntList.isEmpty()){
                LinkInt linkInt = linkIntList.get(0);
                linkInt.setCountLink(1);
                linkIntService.update(linkInt);
            }
            List<LinkSrcs> linkSrcsList = linkSrcsService.query(map1, null);
            if (!linkSrcsList.isEmpty()){
                linkSrcsList.forEach(s-> linkSrcsService.removeById(s.getId()));
            }
            Map<String, Object> mapQuery = Maps.newHashMap();
            mapQuery.put("EQ_centerId", String.valueOf(id));
            dmAccessService.query(mapQuery, null).forEach(s-> dmAccessService.removeById(s.getId()));
            dmClickService.query(mapQuery, null).forEach(s-> dmClickService.removeById(s.getId()));
            dmTrollsService.query(mapQuery, null).forEach(s-> dmTrollsService.removeById(s.getId()));
            String buildKey = redisUtil.buildKey("AoollyNumberIp", str);
            redisUtil.del(buildKey);
            dmCenterService.removeById(id);
        } finally {
            lockService.unlock(lockKey, lockVal);
        }
    }

    private void savePixels(User principal, String str, List<String> list){
        Map<String, Object> map2 = Maps.newHashMap();
        map2.put("EQ_domain",str);
        List<DmPixel> query3 = dmPixelService.query(map2, null);
        if (!query3.isEmpty()){
            query3.forEach(s-> dmPixelService.removeById(s.getId()));
        }
        List<DmPixel> dmPixelList = new LinkedList<>();
        list.forEach(s->{
            DmPixel dmPixel = new DmPixel();
            dmPixel.setPixelId(s);
            dmPixel.setDomain(str);
            dmPixel.setUserName(principal.getUsername());
            dmPixelList.add(dmPixel);
        });
        dmPixelService.saves(dmPixelList);
    }

    private void upsertLinkIntAndSrcs(User principal, String str, DmCenter entity){
        Map<String, Object> map = Maps.newHashMap();
        map.put("EQ_domain",str);
        List<LinkInt> query = linkIntService.query(map, null);
        if (query.isEmpty()){
            LinkInt linkInt = new LinkInt();
            linkInt.setUserName(principal.getUsername());
            linkInt.setDomain(str);
            linkInt.setCountLink(1);
            linkIntService.save(linkInt);
        }else {
            LinkInt linkInt = query.get(0);
            linkInt.setCountLink(1);
            linkIntService.update(linkInt);
        }
        List<LinkSrcs> linkSrcsList = linkSrcsService.query(map, null);
        if (!linkSrcsList.isEmpty()){
            linkSrcsList.forEach(s-> linkSrcsService.removeById(s.getId()));
        }
        LinkSrcs linkSrcs = new LinkSrcs();
        linkSrcs.setLinkSrc(entity.getLink());
        linkSrcs.setKeyy(entity.getKeyy());
        linkSrcs.setProtect(entity.getProtect());
        linkSrcs.setUserName(principal.getUsername());
        linkSrcs.setDomain(str);
        linkSrcsService.save(linkSrcs);
    }

    private String redisString(String key,Long value){
        if (!redisUtil.exist(key)){
            DmStencil dmStencil = dmStencilService.get(value);
            redisUtil.set(key, dmStencil.getPathName());
            return  dmStencil.getPathName();
        }else {
            return redisUtil.get(key);
        }
    }

    private void eventTwoChooseCreate(DmServer dmServer,String filePath,DmCenter entity,DmCondition dmCondition){
        CreateCustomerEvent event=new CreateCustomerEvent();
        event.setHost(dmServer.getIp());
        event.setUsername(dmServer.getUsername());
        event.setPassword(dmServer.getPassword());
        event.setFilePath(filePath);
        event.setNewLink(entity.getLink());
        eventBus.publish(event);
    }
} 