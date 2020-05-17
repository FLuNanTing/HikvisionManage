package com.hikvision.hikvisionmanage.vidicon.controller;

import com.alibaba.fastjson.JSON;
import com.hikvision.hikvisionmanage.utils.LoggerUtil;
import com.hikvision.hikvisionmanage.vidicon.service.VidiconService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @program: HikvisionManage
 * @description: 摄像机控制接口
 * @author: LuNanTing
 * @create: 2020-05-12 10:31
 **/
@RestController
@RequestMapping("brakeControl")
public class VidiconController {

    @Autowired
    private VidiconService vidiconService;

    /**
     * @param bean
     * @return 返回类型：String
     * @title {通过指令控制抓拍/道闸摄像头来控制道闸起落}
     * @author 创建人: LuNanTing
     * @date 时间： 2019年5月6日
     */
    @SuppressWarnings("unchecked")
    @RequestMapping("barkControl")
    public String barkeControl(String bean) {
        Map<String, Object> linkInfo = JSON.parseObject(bean, Map.class);
        Integer command = Integer.valueOf(linkInfo.get("command").toString());
        String devIP = linkInfo.get("devIp").toString();
        String password = linkInfo.get("password").toString();
        Integer port = Integer.valueOf(linkInfo.get("devPort").toString());
        Object canRelease = linkInfo.get("canRelease");
        String plateNumber = linkInfo.get("plateNumber").toString();
        LoggerUtil.info("发送指令时间:" + linkInfo.get("date") + "ms");
        Map<String, Object> controlBrakeDev = vidiconService.controlBrakeDev(devIP, port, password, command,canRelease,plateNumber);
        LoggerUtil.info("解析抬杆耗时:" + (System.currentTimeMillis() - Long.valueOf(linkInfo.get("date").toString()) + "ms"));
        return JSON.toJSONString(controlBrakeDev);
    }

    /**
     * @param bean:传JSON
     * @return 返回类型：Map<String,Object>
     * @title {获取设备信息}
     * @author 创建人: LuNanTing
     * @date 时间： 2018年10月9日
     */
    @RequestMapping("getDevManageInfo")
    @SuppressWarnings("unchecked")
    public String getDeviceInformation(String bean) {
        Map<String, Object> linkInfo = JSON.parseObject(bean, Map.class);
        String deviceIp = linkInfo.get("devIp").toString();
        String devicePortStr = linkInfo.get("devPort").toString();
        String deviceId = linkInfo.get("devId").toString();
        String devicePassWord = linkInfo.get("devPassWord").toString();
        Map<String, Object> devManageInfo = vidiconService.getDeviceInformation(deviceIp, devicePortStr, devicePassWord, deviceId);
        return JSON.toJSONString(devManageInfo);
    }

    /**
     * @param bean
     * @return 返回类型：Map<String,Object>
     * @title {设备重新布防}
     * @author 创建人: LuNanTing
     * @date 时间： 2019年6月18日
     */
    @SuppressWarnings("unchecked")
    @RequestMapping("afreshProtection")
    public Map<String, Object> afreshProtection(String bean) {
        Map<String, Object> map = JSON.parseObject(bean, Map.class);
        String deviceIp = map.get("devIp").toString();
        String devicePortStr = map.get("devPort").toString();
        String devicePassword = map.get("password").toString();
        Integer devicePort = Integer.valueOf(devicePortStr);
        String devUserName = "admin";
        Map<String, Object> mapInfo = vidiconService.afreshProtection(deviceIp, devicePort, devUserName, devicePassword);
        return mapInfo;
    }
}
