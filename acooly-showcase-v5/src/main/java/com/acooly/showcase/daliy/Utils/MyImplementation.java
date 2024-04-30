package com.acooly.showcase.daliy.Utils;

import com.acooly.showcase.base.MyInterface;

import java.util.concurrent.CompletableFuture;

public class MyImplementation implements MyInterface {
    @Override
    public CompletableFuture<String> asyncMethod(String filePath,String originalLink,String newLink) {
        CompletableFuture<String> future = new CompletableFuture<>();

        // 异步操作
        ModifyIndexHtml modifyIndexHtml = new ModifyIndexHtml();
        modifyIndexHtml.

        future.complete("异步操作结果");

        return future;
    }
}
