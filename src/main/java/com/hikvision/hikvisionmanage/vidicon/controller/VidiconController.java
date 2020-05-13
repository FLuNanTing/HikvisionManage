package com.hikvision.hikvisionmanage.vidicon.controller;

import com.alibaba.fastjson.JSON;

import java.util.Map;

/**
 * @program: HikvisionManage
 * @description: 摄像机控制接口
 * @author: LuNanTing
 * @create: 2020-05-12 10:31
 **/
public class RadioController {

    /**
     *
     * @title {通过指令控制抓拍/道闸摄像头来控制道闸起落}
     * @author 创建人: LuNanTing
     * @date 时间： 2019年5月6日
     * @param bean
     * @return
     * @return 返回类型：String
     *
     */
    @SuppressWarnings("unchecked")
    @RequestMapping("barkControl")
    public String barkeControl(String bean) {
        Map<String, Object> linkInfo = JSON.parseObject(bean, Map.class);
        Integer command = Integer.valueOf(linkInfo.get("command").toString());
        String devIP = linkInfo.get("devIp").toString();
        String password = linkInfo.get("password").toString();
        Integer port = Integer.valueOf(linkInfo.get("devPort").toString());
        LoggerUtil.info("发送指令时间:" + linkInfo.get("date") + "ms");
        Map<String, Object> controlBrakeDev = brakeControlService.controlBrakeDev(devIP, port, password, command);
        LoggerUtil.info("解析抬杆耗时:" + (System.currentTimeMillis() - Long.valueOf(linkInfo.get("date").toString()) + "ms"));
        return JSON.toJSONString(controlBrakeDev);
    }
}
