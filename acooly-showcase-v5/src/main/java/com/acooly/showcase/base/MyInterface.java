package com.acooly.showcase.base;

import java.util.concurrent.CompletableFuture;

public interface MyInterface {
    CompletableFuture<String> asyncMethod(String filePath,String originalLink,String newLink);
}
