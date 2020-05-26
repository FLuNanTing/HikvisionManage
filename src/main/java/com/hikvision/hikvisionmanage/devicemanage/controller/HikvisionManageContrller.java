package com.hikvision.hikvisionmanage.devicemanage.controller;

import com.alibaba.fastjson.JSON;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: HikvisionManage
 * @description: 基本返回接口
 * @author: LuNanTing
 * @create: 2020-05-26 16:23
 **/
@RestController
public class HikvisionManageContrller {

    @RequestMapping("/")
    public Object hikvisionManage(){
        return JSON.toJSON("Hello World");
    }
}
