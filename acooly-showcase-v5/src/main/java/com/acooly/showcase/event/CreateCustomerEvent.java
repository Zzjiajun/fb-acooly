package com.acooly.showcase.event;

import lombok.Data;

@Data
public class CreateCustomerEvent {
    private String filePath;
    private String originalLink;
    private String newLink;
    private String host;
    private String username;
    private String password;
}