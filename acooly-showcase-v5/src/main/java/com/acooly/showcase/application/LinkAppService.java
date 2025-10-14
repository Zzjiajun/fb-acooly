package com.acooly.showcase.application;

import com.acooly.showcase.daliy.entity.Link;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface LinkAppService {
    Link createLinkWithRemoteDir(HttpServletRequest request, HttpServletResponse response) throws Exception;
    Link updateLinkAndRenameRemoteDir(HttpServletRequest request, HttpServletResponse response) throws Exception;
    void deleteLinksAndRemoteDirs(HttpServletRequest request, HttpServletResponse response) throws Exception;
} 