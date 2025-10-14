package com.acooly.showcase.application;

import com.acooly.showcase.daliy.entity.DmCenter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface DmCenterAppService {
    DmCenter saveCenterWithRemoteAndEvents(HttpServletRequest request, HttpServletResponse response, DmCenter entity, boolean isCreate) throws Exception;
    void safeRemoveCenter(HttpServletRequest request, HttpServletResponse response, Long id) throws Exception;
} 