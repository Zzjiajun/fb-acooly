package com.acooly.showcase.event;

import lombok.Data;

@Data
public class CreateCustomerTwoEvent {
    private String filePath;
    private String newLink;
    private String host;
    private String username;
    private String password;
}
