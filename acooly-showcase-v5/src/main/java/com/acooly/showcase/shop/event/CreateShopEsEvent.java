package com.acooly.showcase.shop.event;

import com.acooly.module.event.EventHandler;
import com.acooly.showcase.shop.entity.ShopProducts;
import lombok.Data;

import java.io.Serializable;

@Data
public class CreateShopEsEvent {
    private ShopProducts shopProducts;
    private String action;
    private Serializable[] ids;
}
